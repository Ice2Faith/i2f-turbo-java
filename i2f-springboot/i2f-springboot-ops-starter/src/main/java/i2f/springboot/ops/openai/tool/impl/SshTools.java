package i2f.springboot.ops.openai.tool.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.extension.sftp.basic.SftpUtil;
import i2f.extension.sftp.basic.data.SftpMeta;
import i2f.springboot.ops.util.HumanUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2026/11/21 10:00
 * @desc ssh tools driven by named host config, all remote paths are not restricted,
 * local paths reuse the secure policy of {@link LocalFileTools}.
 */
@Slf4j
@McpServerExpose
@ToolIntent(items = @ToolIntentItem(value = "ssh", description = "提供 SSH 主机的命令执行、文件上传与下载"))
@ConditionalOnExpression("${ai.tools.ssh.enable:false}")
@ConditionalOnClass(ChannelSftp.class)
@EnableConfigurationProperties(SshToolsProperties.class)
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class SshTools {

    public static final String PATH_PROMPT = "Local paths are workspace-relative, e.g. \"/build/app.jar\" means \"<workspace>/build/app.jar\".\n" +
            "Use `" + LocalFileTools.ABS_PATH_PREFIX + "` prefix for a real absolute local path, e.g. `" + LocalFileTools.ABS_PATH_PREFIX + "/opt/app.jar`.\n" +
            "Remote paths are used as-is, no policy limit.";

    /**
     * hard upper bound of one tool call, it also keeps the millis conversion away from overflow
     */
    public static final long MAX_WAIT_SECONDS = 3600;
    /**
     * max depth of the cause chain when classifying a thrown exception
     */
    public static final int MAX_CAUSE_DEPTH = 8;

    public static final String AUTH_PASSWORD = "password";
    public static final String AUTH_PRIVATE_KEY = "private-key";
    public static final String NONE_TEXT = "none";
    public static final String HOST_NAME_JOINER = ", ";
    public static final String CURRENT_DIR = ".";
    public static final String ROOT_DIR = "/";
    /**
     * `ll` and interactive `top` do not work well in a non-tty exec channel,
     * so these aliases are rewritten to the non-interactive forms below
     */
    public static final String LIST_DIR_COMMAND = "ls -alh";
    public static final String TOP_COMMAND = "top -c -b -n 1 -w 500";
    public static final Pattern LIST_DIR_ALIAS_PATTERN = Pattern.compile("\\bll\\b");
    public static final Pattern TOP_ALIAS_PATTERN = Pattern.compile("\\btop\\b");
    public static final String CRLF_TEXT = "\r\n";
    public static final String LF_TEXT = "\n";
    public static final String EXIT_STATUS_PREFIX = "exit status:";
    /**
     * the underlying exec always appends `exit status: N` as the last line of the output
     */
    public static final Pattern EXIT_STATUS_PATTERN = Pattern.compile("(?m)^" + EXIT_STATUS_PREFIX + "[ \\t]*(-?\\d+)[ \\t]*$");

    /**
     * shared read-only fallback when the properties bean is absent
     */
    protected static final SshToolsProperties EMPTY_PROPERTIES = new SshToolsProperties();

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Autowired(required = false)
    private SshToolsProperties properties;


    public static final String SSH_HOSTS = "ssh_hosts";

    @Tool(
            value = SSH_HOSTS,
            tags = {
                    AiTags.READONLY_VALUE,
                    AiTags.AUTO_VALUE
            },
            description = "list registered ssh host names; call it first, then pass one returned `host` name to "
                    + SSH_COMMAND + " / " + SSH_UPLOAD + " / " + SSH_DOWNLOAD + "."
    )
    public SshHostsResult ssh_hosts() {
        SshHostsResult ret = new SshHostsResult();
        List<SshHostInfo> list = new ArrayList<>();
        for (SshToolsProperties.SshHostOptions opt : getEffectiveHosts()) {
            SshHostInfo info = new SshHostInfo();
            info.setHost(opt.getName().trim());
            info.setDescription(opt.getDescription());
            info.setAddress(opt.getUsername() + "@" + opt.getHost() + ":" + resolvePort(opt));
            info.setWorkdir(opt.getWorkdir());
            info.setAuth(isEmpty(opt.getPrivateKey()) ? AUTH_PASSWORD : AUTH_PRIVATE_KEY);
            list.add(info);
        }
        ret.setHosts(list);
        ret.setTotal(list.size());
        ret.apply(list.isEmpty() ? SshOutcome.NO_HOST_REGISTERED : SshOutcome.HOSTS_READY);
        return ret;
    }

    public static final String SSH_COMMAND = "ssh_command";

    @Tool(
            value = SSH_COMMAND,
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.COMMAND_VALUE,
                    AiTags.SLOW_EXEC_VALUE
            },
            description = "run one shell command on a registered ssh host, wait for output and return exit status.\n" +
                    "`ll` and `top` are rewritten to non-interactive forms automatically."
    )
    public SshCommandResult ssh_command(
            @ToolParam(value = "host", description = "registered host name, must be one of " + SSH_HOSTS + " results, e.g. `dev`")
            String host,
            @ToolParam(value = "command", description = "one shell command line, e.g. `df -h` or `ls -alh /opt`")
            String command,
            @ToolParam(value = "workdir", description = "remote workdir before command, null means host default or login dir, e.g. /opt/app")
            String workdir,
            @ToolParam(value = "waitForSeconds", description = "max wait seconds, <=0 means config default(120), capped by config(500), e.g. 60")
            long waitForSeconds) {
        SshCommandResult ret = new SshCommandResult();
        ret.setHost(host);
        if (isEmpty(command)) {
            ret.fail(SshOutcome.MISSING_COMMAND, "missing `command` argument");
            return ret;
        }

        try {
            SshToolsProperties.SshHostOptions opt = requireHost(host);
            SshToolsProperties props = getSshProperties();
            String dir = isEmpty(workdir) ? opt.getWorkdir() : workdir.trim();
            long waitSeconds = resolveWaitSeconds(props, waitForSeconds);

            ret.setCommand(normalizeCommand(command));
            ret.setWorkdir(dir);
            ret.setWaitSeconds(waitSeconds);

            String output = execRemote(opt, dir, ret.getCommand(), waitSeconds, props.getCharset());
            applyCommandOutput(ret, output, waitSeconds, props.getMaxOutputChars());
            return ret;
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            applyException(ret, e);
            return ret;
        }
    }

    public static final String SSH_UPLOAD = "ssh_upload";

    @Tool(
            value = SSH_UPLOAD,
            tags = {
                    AiTags.WRITABLE_VALUE,
                    AiTags.FILE_VALUE
            },
            description = "upload one local file to a registered ssh host.\n" + PATH_PROMPT
    )
    public SshTransferResult ssh_upload(
            @ToolParam(value = "host", description = "registered host name, must be one of " + SSH_HOSTS + " results, e.g. `dev`")
            String host,
            @ToolParam(value = "localPath", description = "local file path, e.g. /build/app.jar")
            String localPath,
            @ToolParam(value = "remotePath", description = "remote target file path, e.g. /opt/app/app.jar; end with `/` to keep the local file name")
            String remotePath) {
        SshTransferResult ret = new SshTransferResult();
        ret.setHost(host);

        try {
            SshToolsProperties.SshHostOptions opt = requireHost(host);
            File localFile = requireLocalFile(localPath);
            ret.setLocal(localFile.getAbsolutePath());

            // a directory or a missing path is not an uploadable source
            if (!localFile.isFile()) {
                ret.fail(SshOutcome.LOCAL_FILE_NOT_FOUND, "local file not found: " + ret.getLocal());
                return ret;
            }

            String[] remote = splitRemotePath(remotePath, localFile.getName());
            String remoteDir = remote[0];
            String remoteName = remote[1];
            ret.setRemote(joinPath(remoteDir, remoteName));
            ret.setLocalBytes(localFile.length());
            ret.setHumanSize(HumanUtil.humanFileSize(localFile.length()));

            SftpUtil util = null;
            InputStream is = null;
            try {
                util = open(opt);
                is = new FileInputStream(localFile);
                util.upload(remoteDir, remoteName, is);
                ret.apply(SshOutcome.UPLOADED, remoteDir);
                return ret;
            } finally {
                closeQuietly(is);
                logout(util);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            applyException(ret, e);
            return ret;
        }
    }

    public static final String SSH_DOWNLOAD = "ssh_download";

    @Tool(
            value = SSH_DOWNLOAD,
            tags = {
                    AiTags.WRITABLE_VALUE,
                    AiTags.FILE_VALUE,
                    AiTags.DOWNLOAD_VALUE
            },
            description = "download one remote file from a registered ssh host to local.\n" + PATH_PROMPT
    )
    public SshTransferResult ssh_download(
            @ToolParam(value = "host", description = "registered host name, must be one of " + SSH_HOSTS + " results, e.g. `dev`")
            String host,
            @ToolParam(value = "remotePath", description = "remote file path, e.g. /opt/app/app.log")
            String remotePath,
            @ToolParam(value = "localPath", description = "local target path, e.g. /download/app.log; end with `/` or pass a  directory to keep the remote file name")
            String localPath) {
        SshTransferResult ret = new SshTransferResult();
        ret.setHost(host);
        ret.setRemote(remotePath);

        if (isEmpty(remotePath) || remotePath.trim().endsWith(ROOT_DIR)) {
            ret.fail(SshOutcome.REMOTE_PATH_INVALID, "`remotePath` must be a remote file path");
            return ret;
        }

        String[] remote = splitRemotePath(remotePath, null);
        String remoteDir = remote[0];
        String remoteName = remote[1];
        if (isEmpty(remoteName)) {
            ret.fail(SshOutcome.REMOTE_PATH_INVALID, "`remotePath` must be a remote file path");
            return ret;
        }

        try {
            SshToolsProperties.SshHostOptions opt = requireHost(host);
            File localTarget = requireLocalFile(localPath);

            File saveFile = localTarget;
            if (localTarget.isDirectory() || isEndsWithSlash(localPath)) {
                saveFile = new File(localTarget, remoteName);
            }
            ret.setLocal(saveFile.getAbsolutePath());
            ret.setRemote(joinPath(remoteDir, remoteName));

            SftpUtil util = null;
            try {
                util = open(opt);
                // keep the local write strategy of LocalFileTools: backup before overwrite
                backupLocalFileQuietly(saveFile);
                util.download(remoteDir, remoteName, saveFile);
                ret.setLocalBytes(saveFile.length());
                ret.setHumanSize(HumanUtil.humanFileSize(saveFile.length()));
                ret.apply(SshOutcome.DOWNLOADED);
                return ret;
            } finally {
                logout(util);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            applyException(ret, e);
            return ret;
        }
    }

    // ==========================================
    // result models
    // ==========================================

    @Data
    @NoArgsConstructor
    public static class SshHostInfo {
        protected String host;
        protected String description;
        protected String address;
        protected String workdir;
        protected String auth;
    }

    @Data
    @NoArgsConstructor
    public static class SshResult {
        /**
         * whether the tool call achieved its goal, derived from {@link #code} only
         */
        protected boolean success;
        /**
         * machine readable result code, see {@link SshOutcome#getCode()}
         */
        protected String code;
        /**
         * raw error message, only present when failed
         */
        protected String message;
        /**
         * actionable hint for the next step, derived from {@link #code}
         */
        protected String hints;

        public void apply(SshOutcome outcome, Object... hintArgs) {
            this.success = outcome.isOk();
            this.code = outcome.getCode();
            this.hints = outcome.formatHint(hintArgs);
        }

        public void fail(SshOutcome outcome, String message, Object... hintArgs) {
            apply(outcome, hintArgs);
            this.message = message;
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class SshHostsResult extends SshResult {
        protected List<SshHostInfo> hosts = new ArrayList<>();
        protected int total;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class SshCommandResult extends SshResult {
        protected String host;
        protected String command;
        protected String workdir;
        protected long waitSeconds;
        /**
         * false means the command was still running when the wait time was over
         */
        protected boolean completed;
        protected Integer exitStatus;
        protected boolean truncated;
        protected String output;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class SshTransferResult extends SshResult {
        protected String host;
        protected String local;
        protected long localBytes;
        protected String humanSize;
        protected String remote;
    }

    /**
     * result codes with their default hint template, so that {@code success}, {@code code}
     * and {@code hints} are all derived from one place instead of parsing error messages
     */
    @Getter
    @AllArgsConstructor
    public enum SshOutcome {
        // ===== success =====
        HOSTS_READY(true, "ok", "pick one `host` above, then call " + SSH_COMMAND + " / " + SSH_UPLOAD + " / " + SSH_DOWNLOAD + " with it."),
        COMMAND_OK(true, "ok", "command succeeded."),
        UPLOADED(true, "ok", "uploaded, verify on remote by " + SSH_COMMAND + " `" + LIST_DIR_COMMAND + " %s`."),
        DOWNLOADED(true, "ok", "downloaded, read it by the local file tools, or download another file from the same remote dir."),
        OUTPUT_TRUNCATED(true, "output_truncated", "output truncated, narrow the result by `| tail -n 100` or `| head -n 100`."),

        // ===== argument =====
        NO_HOST_REGISTERED(false, "no_host_registered", "no ssh host registered, set `ai.tools.ssh.enable=true` and fill `ai.tools.ssh.hosts` items with `name,host,port,username,password|private-key`."),
        MISSING_HOST(false, "missing_host", "`host` is required, call " + SSH_HOSTS + " to get available hosts (registered now: %s), then retry with one of its `host` names."),
        UNKNOWN_HOST(false, "unknown_host", "unknown host, call " + SSH_HOSTS + " to get available hosts (registered now: %s), config item is `ai.tools.ssh.hosts[].name`."),
        HOST_DISABLED(false, "host_disabled", "this host is closed, set `enable: true` of the `ai.tools.ssh.hosts` item."),
        HOST_INCOMPLETE(false, "host_incomplete", "incomplete host config, fill `host,port,username,password|private-key` of the `ai.tools.ssh.hosts` item."),
        MISSING_COMMAND(false, "missing_command", "pass one shell command line, e.g. `uname -a`."),
        REMOTE_PATH_INVALID(false, "remote_path_invalid", "pass a remote file path like `/opt/app/app.log`, run " + SSH_COMMAND + " `" + LIST_DIR_COMMAND + " <dir>` to confirm."),

        // ===== local file policy =====
        LOCAL_FILE_DISABLED(false, "local_file_disabled", "local file tools are off, set `ai.tools.file.enable=true` to allow local path access."),
        LOCAL_PATH_DENIED(false, "local_path_denied", "local path blocked by the local-file policy, use a workspace-relative path, or `" + LocalFileTools.ABS_PATH_PREFIX + "` prefix when full access is allowed."),
        LOCAL_FILE_NOT_FOUND(false, "local_file_not_found", "confirm the local file by file_list or file_search, local paths are workspace-relative."),

        // ===== connection =====
        AUTH_FAILED(false, "auth_failed", "ssh auth failed, verify `username` and `password`/`private-key` of this host."),
        REMOTE_UNRESOLVED(false, "remote_unresolved", "remote address can not be resolved, verify `host` of the `ai.tools.ssh.hosts` item."),
        CONNECTION_REFUSED(false, "connection_refused", "ssh port refused, verify `port` and the sshd state of the remote host."),
        CONNECT_TIMEOUT(false, "connect_timeout", "connect timed out, verify network/firewall between this server and the remote host."),

        // ===== remote file =====
        REMOTE_NOT_FOUND(false, "remote_not_found", "remote path not found, run " + SSH_COMMAND + " `" + LIST_DIR_COMMAND + " <dir>` to confirm the path first."),
        REMOTE_PERMISSION_DENIED(false, "remote_permission_denied", "remote permission denied, retry with a writable dir like /tmp, or ask user to grant permission."),

        // ===== command result =====
        COMMAND_TIMEOUT(false, "command_timeout", "command not finished in %ss, retry with larger `waitForSeconds`, or run it in background like `nohup <cmd> > /tmp/x.log 2>&1 &`."),
        COMMAND_FAILED(false, "command_failed", "exit status %s, inspect `output`; `sh` syntax errors or missing files are the common cause."),

        // ===== fallback =====
        UNKNOWN(false, "unknown", "retry later, or verify the host config by " + SSH_HOSTS + ".");

        private final boolean ok;
        private final String code;
        private final String hintTemplate;

        public String formatHint(Object... args) {
            if (args == null || args.length == 0) {
                return hintTemplate;
            }
            return String.format(hintTemplate, args);
        }

        /**
         * classify a thrown exception by its type and protocol code, not by message text
         */
        public static SshOutcome of(Throwable e) {
            if (e == null) {
                return UNKNOWN;
            }
            if (e instanceof SshToolException) {
                return ((SshToolException) e).getOutcome();
            }
            SshOutcome byCause = ofCauseChain(e);
            if (byCause != null) {
                return byCause;
            }
            return ofMessage(e.getMessage());
        }

        protected static SshOutcome ofCauseChain(Throwable e) {
            Throwable current = e;
            int deep = 0;
            while (current != null && deep < MAX_CAUSE_DEPTH) {
                if (current instanceof UnknownHostException) {
                    return REMOTE_UNRESOLVED;
                }
                if (current instanceof ConnectException) {
                    return CONNECTION_REFUSED;
                }
                if (current instanceof SocketTimeoutException) {
                    return CONNECT_TIMEOUT;
                }
                if (current instanceof FileNotFoundException) {
                    return LOCAL_FILE_NOT_FOUND;
                }
                if (current instanceof SftpException) {
                    int id = ((SftpException) current).id;
                    if (id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        return REMOTE_NOT_FOUND;
                    }
                    if (id == ChannelSftp.SSH_FX_PERMISSION_DENIED) {
                        return REMOTE_PERMISSION_DENIED;
                    }
                }
                if (current.getCause() == current) {
                    break;
                }
                current = current.getCause();
                deep++;
            }
            return null;
        }

        /**
         * jsch reports a few states only by message, they are kept in this single place
         */
        protected static SshOutcome ofMessage(String message) {
            if (message == null) {
                return UNKNOWN;
            }
            String msg = message.toLowerCase();
            if (msg.contains("auth fail") || msg.contains("auth cancel") || msg.contains("userauth")) {
                return AUTH_FAILED;
            }
            if (msg.contains("unknownhost") || msg.contains("name or service not known")) {
                return REMOTE_UNRESOLVED;
            }
            if (msg.contains("connection refused")) {
                return CONNECTION_REFUSED;
            }
            if (msg.contains("timed out") || msg.contains("timeout")) {
                return CONNECT_TIMEOUT;
            }
            return UNKNOWN;
        }
    }

    public static class SshToolException extends RuntimeException {
        private final SshOutcome outcome;

        public SshToolException(SshOutcome outcome, String message) {
            super(message);
            this.outcome = outcome;
        }

        public SshToolException(SshOutcome outcome, String message, Throwable cause) {
            super(message, cause);
            this.outcome = outcome;
        }

        public SshOutcome getOutcome() {
            return outcome;
        }
    }

    // ==========================================
    // internals
    // ==========================================

    protected SshToolsProperties getSshProperties() {
        // the injected one is a singleton, the shared fallback keeps this read path free of lazy writes
        return properties == null ? EMPTY_PROPERTIES : properties;
    }

    /**
     * effective hosts: enabled and complete enough to be used by the tools,
     * disabled or incomplete items are ignored with a warn log
     */
    protected List<SshToolsProperties.SshHostOptions> getEffectiveHosts() {
        List<SshToolsProperties.SshHostOptions> ret = new ArrayList<>();
        List<SshToolsProperties.SshHostOptions> hosts = getSshProperties().getHosts();
        if (hosts == null || hosts.isEmpty()) {
            return ret;
        }
        for (SshToolsProperties.SshHostOptions opt : hosts) {
            if (opt == null || !opt.isEnable()) {
                continue;
            }
            if (isEmpty(opt.getName())) {
                log.warn("ssh host ignore, missing `name` of `ai.tools.ssh.hosts` item");
                continue;
            }
            if (isEmpty(opt.getHost()) || isEmpty(opt.getUsername())) {
                log.warn("ssh host ignore, incomplete config: {}", opt.getName());
                continue;
            }
            ret.add(opt);
        }
        return ret;
    }

    protected List<String> getEffectiveHostNames() {
        List<String> ret = new ArrayList<>();
        for (SshToolsProperties.SshHostOptions opt : getEffectiveHosts()) {
            ret.add(opt.getName().trim());
        }
        return ret;
    }

    protected String availableHostNames() {
        List<String> names = getEffectiveHostNames();
        return names.isEmpty() ? NONE_TEXT : String.join(HOST_NAME_JOINER, names);
    }

    protected SshToolsProperties.SshHostOptions requireHost(String host) {
        if (isEmpty(host)) {
            throw new SshToolException(SshOutcome.MISSING_HOST, "missing `host` argument");
        }
        SshToolsProperties.SshHostOptions opt = getSshProperties().host(host);
        if (opt == null) {
            throw new SshToolException(SshOutcome.UNKNOWN_HOST, "unknown ssh host `" + host + "`");
        }
        if (!opt.isEnable()) {
            throw new SshToolException(SshOutcome.HOST_DISABLED, "ssh host `" + host + "` is disabled");
        }
        if (isEmpty(opt.getHost())) {
            throw new SshToolException(SshOutcome.HOST_INCOMPLETE, "ssh host `" + host + "` has no remote address");
        }
        if (isEmpty(opt.getUsername())) {
            throw new SshToolException(SshOutcome.HOST_INCOMPLETE, "ssh host `" + host + "` has no username");
        }
        return opt;
    }

    /**
     * local path access is always resolved by {@link LocalFileTools#getFile(String)}
     */
    protected File requireLocalFile(String localPath) {
        if (localFileTools == null) {
            throw new SshToolException(SshOutcome.LOCAL_FILE_DISABLED, "missing local-file secure control.");
        }
        try {
            return localFileTools.getFile(localPath);
        } catch (IllegalArgumentException e) {
            throw new SshToolException(SshOutcome.LOCAL_PATH_DENIED, messageOf(e), e);
        }
    }

    /**
     * reuse the local file write strategy: backup the old file before overwriting,
     * backup failure must not block the transfer
     */
    protected void backupLocalFileQuietly(File file) {
        if (localFileTools == null || !localFileTools.isBackupFile()) {
            return;
        }
        if (file == null || !file.isFile()) {
            return;
        }
        try {
            localFileTools.backupFile(file);
        } catch (Throwable e) {
            log.warn("local file backup failed: " + file.getAbsolutePath(), e);
        }
    }

    protected void applyException(SshResult ret, Throwable e) {
        SshOutcome outcome = SshOutcome.of(e);
        ret.fail(outcome, messageOf(e), hintArgsOf(outcome));
    }

    protected Object[] hintArgsOf(SshOutcome outcome) {
        switch (outcome) {
            case MISSING_HOST:
            case UNKNOWN_HOST:
                return new Object[]{availableHostNames()};
            default:
                return new Object[0];
        }
    }

    protected static String messageOf(Throwable e) {
        if (e == null) {
            return null;
        }
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }

    /**
     * one short ssh session per tool call: connect, exec, and always logout
     */
    protected String execRemote(SshToolsProperties.SshHostOptions opt, String dir, String cmd, long waitSeconds, String charset) throws Exception {
        SftpUtil util = null;
        try {
            util = open(opt);
            return util.exec(true, TimeUnit.SECONDS.toMillis(waitSeconds), cmd, dir, charset);
        } finally {
            logout(util);
        }
    }

    /**
     * a missing `exit status:` line means the command did not finish within the wait time,
     * so `completed`, `exitStatus` and the outcome are derived from that single line
     */
    protected void applyCommandOutput(SshCommandResult ret, String output, long waitSeconds, int maxOutputChars) {
        boolean completed = false;
        int exitStatus = -1;
        String body = output == null ? "" : output;

        Matcher matcher = EXIT_STATUS_PATTERN.matcher(body);
        int statusIndex = -1;
        while (matcher.find()) {
            completed = true;
            exitStatus = Integer.parseInt(matcher.group(1));
            statusIndex = matcher.start();
        }
        if (statusIndex >= 0) {
            body = body.substring(0, statusIndex);
        }
        body = trimTrailingNewLine(body);

        boolean truncated = maxOutputChars > 0 && body.length() > maxOutputChars;
        if (truncated) {
            body = body.substring(0, maxOutputChars);
        }

        ret.setCompleted(completed);
        ret.setExitStatus(completed ? Integer.valueOf(exitStatus) : null);
        ret.setOutput(body);
        ret.setTruncated(truncated);

        if (!completed) {
            ret.fail(SshOutcome.COMMAND_TIMEOUT, "command not finished in " + waitSeconds + "s", waitSeconds);
        } else if (exitStatus != 0) {
            ret.fail(SshOutcome.COMMAND_FAILED, "command failed with exit status " + exitStatus, exitStatus);
        } else if (truncated) {
            ret.apply(SshOutcome.OUTPUT_TRUNCATED);
        } else {
            ret.apply(SshOutcome.COMMAND_OK);
        }
    }

    protected SftpUtil open(SshToolsProperties.SshHostOptions opt) throws Exception {
        SftpMeta meta = new SftpMeta();
        meta.setHost(opt.getHost().trim());
        meta.setPort(resolvePort(opt));
        meta.setUsername(opt.getUsername());
        if (!isEmpty(opt.getPassword())) {
            meta.setPassword(opt.getPassword());
        }
        if (!isEmpty(opt.getPrivateKey())) {
            meta.setPrivateKey(opt.getPrivateKey());
        }
        meta.setConfig(toJschConfig(opt));
        return new SftpUtil(meta).login();
    }

    protected static Properties toJschConfig(SshToolsProperties.SshHostOptions opt) {
        Properties ret = new Properties();
        Map<String, String> cusConfig = opt.getConfig();
        if (cusConfig == null) {
            return ret;
        }
        for (Map.Entry<String, String> item : cusConfig.entrySet()) {
            if (item.getKey() == null || item.getValue() == null) {
                continue;
            }
            ret.setProperty(item.getKey(), item.getValue());
        }
        return ret;
    }

    protected static int resolvePort(SshToolsProperties.SshHostOptions opt) {
        return opt.getPort() <= 0 ? SshToolsProperties.DEFAULT_SSH_PORT : opt.getPort();
    }

    /**
     * wait time precedence: argument -> config default -> fallback, then capped by
     * config max and the hard upper bound
     */
    protected static long resolveWaitSeconds(SshToolsProperties props, long waitForSeconds) {
        long wait = waitForSeconds;
        if (wait <= 0) {
            wait = props.getDefaultWaitSeconds();
        }
        if (wait <= 0) {
            wait = SshToolsProperties.DEFAULT_WAIT_SECONDS;
        }
        long max = props.getMaxWaitSeconds();
        if (max > 0) {
            wait = Math.min(wait, max);
        }
        return Math.min(wait, MAX_WAIT_SECONDS);
    }

    protected static String normalizeCommand(String command) {
        // precompiled patterns avoid re-parsing the regex on every tool call
        String ret = LIST_DIR_ALIAS_PATTERN.matcher(command.trim()).replaceAll(LIST_DIR_COMMAND);
        return TOP_ALIAS_PATTERN.matcher(ret).replaceAll(TOP_COMMAND);
    }

    /**
     * split a remote path into [dir, name],
     * a trailing slash keeps the given fallback name
     */
    protected static String[] splitRemotePath(String remotePath, String fallbackName) {
        String path = remotePath == null ? "" : remotePath.trim();
        if (path.isEmpty() || path.endsWith(ROOT_DIR)) {
            return new String[]{path.isEmpty() ? CURRENT_DIR : path, fallbackName};
        }
        int idx = path.lastIndexOf(ROOT_DIR);
        if (idx < 0) {
            return new String[]{CURRENT_DIR, path};
        }
        String dir = path.substring(0, idx);
        if (dir.isEmpty()) {
            dir = ROOT_DIR;
        }
        return new String[]{dir, path.substring(idx + 1)};
    }

    protected static String joinPath(String dir, String name) {
        if (isEmpty(dir) || CURRENT_DIR.equals(dir)) {
            return name;
        }
        if (dir.endsWith(ROOT_DIR)) {
            return dir + name;
        }
        return dir + ROOT_DIR + name;
    }

    protected static String trimTrailingNewLine(String text) {
        String ret = text.replace(CRLF_TEXT, LF_TEXT);
        while (ret.endsWith(LF_TEXT)) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    protected static boolean isEndsWithSlash(String path) {
        return path != null && (path.endsWith("/") || path.endsWith("\\"));
    }

    protected static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    protected static void logout(SftpUtil util) {
        if (util == null) {
            return;
        }
        try {
            util.logout();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }

    protected static void closeQuietly(InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }
}
