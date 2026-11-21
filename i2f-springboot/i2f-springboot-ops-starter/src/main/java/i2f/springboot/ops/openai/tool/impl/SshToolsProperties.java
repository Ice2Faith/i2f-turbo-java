package i2f.springboot.ops.openai.tool.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/11/21 10:00
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "ai.tools.ssh")
public class SshToolsProperties {
    /**
     * default ssh port of a host item
     */
    public static final int DEFAULT_SSH_PORT = 22;
    /**
     * default wait seconds of ssh_command, when argument is absent
     */
    public static final int DEFAULT_WAIT_SECONDS = 120;
    /**
     * default upper limit of wait seconds of ssh_command
     */
    public static final int DEFAULT_MAX_WAIT_SECONDS = 500;
    /**
     * default max chars of remote command output
     */
    public static final int DEFAULT_MAX_OUTPUT_CHARS = 20000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * ssh tools switch, also controlled by `@ConditionalOnExpression`
     */
    protected boolean enable = false;

    /**
     * default wait seconds of ssh_command, when argument is absent
     */
    protected int defaultWaitSeconds = DEFAULT_WAIT_SECONDS;

    /**
     * upper limit of wait seconds of ssh_command
     */
    protected int maxWaitSeconds = DEFAULT_MAX_WAIT_SECONDS;

    /**
     * charset of remote command output
     */
    protected String charset = DEFAULT_CHARSET;

    /**
     * max chars of remote command output, excess part will be truncated
     */
    protected int maxOutputChars = DEFAULT_MAX_OUTPUT_CHARS;

    /**
     * named ssh hosts, the `name` of each item is the host name used by the tools
     */
    protected List<SshHostOptions> hosts = new ArrayList<>();

    public SshHostOptions host(String name) {
        if (name == null || name.trim().isEmpty() || hosts == null || hosts.isEmpty()) {
            return null;
        }
        String key = name.trim();
        for (SshHostOptions item : hosts) {
            if (item == null || item.getName() == null) {
                continue;
            }
            if (item.getName().trim().equalsIgnoreCase(key)) {
                return item;
            }
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    public static class SshHostOptions {
        /**
         * unique host name, used as the `host` argument of ssh tools
         */
        protected String name;

        protected boolean enable = true;

        /**
         * human readable usage of this host, shown by ssh_hosts
         */
        protected String description;

        protected String host;

        protected int port = DEFAULT_SSH_PORT;

        protected String username;

        @ToString.Exclude
        protected String password;

        @ToString.Exclude
        protected String privateKey;

        /**
         * default remote workdir of ssh_command
         */
        protected String workdir;

        /**
         * extra jsch session config, e.g. StrictHostKeyChecking/PreferredAuthentications
         */
        protected Map<String, String> config = new LinkedHashMap<>();
    }

}
