package i2f.web.filter;

import i2f.web.wrapper.HttpServletRequestProxyWrapper;
import lombok.Data;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/10/23 21:25
 * @desc
 */
@Data
public class SecurityFilter extends OncePerHttpServletFilter {

    public static final String[] BAD_METHODS = {"TRACE", "BATCH"};

    public static final String[] ALLOWED_CONTENT_TYPE = {
            "application/json",
            "application/x-www-form-urlencoded",
            "multipart/form-data",
            "text/xml",
            "binary"
    };
    public static final String[] BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS;
    public static final String BAD_URL_CHARS = ";'\"<>\\|{}`~!$^";
    public static final String[] BAD_URL_ENCODED_URL_CHARS;
    public static final String[] BAD_SUFFIXES = {
            ".java", ".jsp", ".jsf", ".properties", ".yaml", ".yml", ".xml", ".json", ".drl", ".class", ".jar", ".war",
            ".py", ".pyc",
            ".php",
            ".sql",
            ".sh", ".bat", ".exe", ".vbs", ".cmd", ".ps", ".elf", ".msi", ".repo", ".apk",
            ".conf", ".d", ".cnf", ".ini", ".inf", ".env",
            ".log", ".out", ".err",
            ".hprof", ".dump", ".dmp", ".bak",
            ".lock",
    };
    public static final String[] PASS_SUFFIXES = {
            ".html", ".htm",
            ".css", ".sass", ".less",
            ".js", ".ts",
            ".vue",
            ".ttf", ".woff", ".woff2",
            ".svg", ".ico", ".icon",
            ".jpg", ".jpeg", ".webp", ".png", ".gif", ".tiff", ".bmp",
            ".mp3", ".aac", ".ogg", ".wav",
            ".mp4", ".flv", ".m3u8", ".hls", ".yuv", ".mkv", ".rmvb", ".avi", ".mpeg", ".ts",
            ".zip", ".tar", ".gz", ".tgz", ".rar", ".7z",
            ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            ".pdf",
            ".wps",
    };

    public static final String[] BAD_FILE_PATHS = {
            ".ssh", ".git", ".gitee", ".gitlab", ".github", ".subversion",
            ".bash_history", ".bashrc", ".bash_profile", ".bash_logout", ".vim",
            "cron.d", "php.d", "profile.d", "sudoers.d", "init.d", "sysctl.d", "yum.repos.d",
            "Windows/System32",
            "Windows/SysWOW64",
    };

    public static final String[] BAD_FILE_NAMES = {
            ".bashrc", ".bash_history", ".bash_logout", ".bash_profile",
            ".viminfo",
            "id_rsa", "id_rsa.pub", "known_hosts",
            "hosts",
            "bashrc", "hostname",
            "passwd", "passwd-",
            "profile",
            "shadow", "shadow-",
            "sudo.conf", "sudoers",
            "yum.conf"
    };

    public static final String[] BAD_COMMANDS = {
            "rm", "chmod", "chown", "chroot", "chattr", "chgrp",
            "halt", "init", "poweroff", "reboot", "shutdown", "setup",
            "iptables", "firewalld", "ip6tables",
            "systemctl", "su", "sudo", "bash", "sh", "crontab",
            "yum", "wget", "curl", "apt-install", "apt-get", "nc", "route",
            "sshd", "ssh", "scp", "rsync", "ssh-add", "users",
            "who", "whoami", "w",
            "adduser", "chpasswd", "passwd", "lpasswd", "luseradd", "luserdel", "lusermod",
            "useradd", "userdel", "usermod",
            "newusers",
            "groupadd", "groupdel", "groupmod", "lgroupadd", "lgroupdel", "lgroupmod",
            "mount", "unmount",
            "crond",
            "httpd",
            "nohup", "kill",
            "python", "java",
            "rd", "del", "cmd", "start", "rmdir", "taskkill",
    };

    public static final String[] SQL_INJECT_REGEXES = {
            "\\s+or\\s+.+=.+",
            "\\s+union\\s+.*select.+",
            "\\s*;\\s*(select|update|insert|delete|create|drop|truncate|grant|revoke|alter|comment|show|load|show)\\s+",
            "\\s*;\\s*(begin|rollback|commit)\\s*(;)?",
            "\\s*;\\s*(updatexml|load)\\s*(\\()?",
            "\\s+--\\s*"
    };
    public static final Pattern[] SQL_INJECT_PATTERNS;

    public static final String[] REMOTE_INVOKE_REGEXES = {
            "rmi://",
            "jndi:[a-zA-Z0-9-_$]://",
            "ldap://",
            "jdbc://",

    };
    public static final Pattern[] REMOTE_INVOKE_PATTERNS;

    public static final Pattern[] XML_XXE_PATTERN = {
            Pattern.compile("\\<\\!ENTITY(\\s+|\\>)".toLowerCase()),
            Pattern.compile("\\<\\!ELEMENT(\\s+|\\>)".toLowerCase())
    };

    public static final Pattern[] HTML_XSS_PATTERN = {
            Pattern.compile("\\<script(\\s+|\\>)".toLowerCase()),
            Pattern.compile("\\<iframe(\\s+|\\>)".toLowerCase()),
    };

    public static final Pattern[] JAVA_DESERIALIZE_PATTERN = {
            Pattern.compile("java\\.lang\\.Runtime"),
            Pattern.compile("java\\.lang\\.Process"),
            Pattern.compile("java\\.lang\\.ProcessImpl"),
            Pattern.compile("java\\.lang\\.ProcessBuilder"),
            Pattern.compile("java\\.rmi\\.Naming"),
            Pattern.compile("java\\.rmi\\.registry\\.Registry"),
            Pattern.compile("sun\\.rmi\\.registry\\.RegistryImpl"),
            Pattern.compile("java\\.lang\\.System"),
            Pattern.compile("java\\.lang\\.Shutdown"),
            Pattern.compile("java\\.lang\\.Thread"),
            Pattern.compile("java\\.net\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("sun\\.net\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("java\\.rmi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("sun\\.rmi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("java\\.sql\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("javax\\.sql\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("jakarta\\.sql\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("javax\\.rmi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("jakarta\\.rmi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("javax\\.naming\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("jakarta\\.naming\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("javax\\.net\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("jakarta\\.net\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("javax\\.transaction\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("jakarta\\.transaction\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.sun.\\.jndi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.sun.\\.rmi\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.sun.\\.rowset\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.sun.\\.net\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.sun.\\.jmx\\.[a-zA-Z0-9_$]+"),
            Pattern.compile("com\\.oracle.\\.net\\.[a-zA-Z0-9_$]+"),
    };

    static {
        List<String> badInvisibleUrlEncodedAsciiChars = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (i == '\t') {
                continue;
            }
            badInvisibleUrlEncodedAsciiChars.add(String.format("%%%02x", i));
        }
        BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS = badInvisibleUrlEncodedAsciiChars.toArray(new String[0]);

        List<String> badUrlChars = new ArrayList<String>();
        for (int i = 0; i < BAD_URL_CHARS.length(); i++) {
            int ch = BAD_URL_CHARS.charAt(i);
            badUrlChars.add(String.format("%%%02x", ch));
        }
        BAD_URL_ENCODED_URL_CHARS = badUrlChars.toArray(new String[0]);

        List<Pattern> sqlInjectPatterns = new ArrayList<>();
        for (String item : SQL_INJECT_REGEXES) {
            sqlInjectPatterns.add(Pattern.compile(item));
        }
        SQL_INJECT_PATTERNS = sqlInjectPatterns.toArray(new Pattern[0]);

        List<Pattern> remoteInvokePatterns = new ArrayList<>();
        for (String item : SQL_INJECT_REGEXES) {
            remoteInvokePatterns.add(Pattern.compile(item));
        }
        REMOTE_INVOKE_PATTERNS = remoteInvokePatterns.toArray(new Pattern[0]);
    }

    /**
     * 拒绝哪些 http method
     */
    protected final CopyOnWriteArrayList<String> denyMethods = new CopyOnWriteArrayList<>();

    /**
     * 只允许哪些 http content-type
     */
    protected final CopyOnWriteArrayList<String> allowedContentTypes = new CopyOnWriteArrayList<>();

    /**
     * 默认情况下，是否跳过后缀判断
     */
    protected final AtomicBoolean defaultAllowSuffix = new AtomicBoolean(true);
    /**
     * 允许访问的后缀和禁止访问的后缀
     */
    protected final CopyOnWriteArrayList<String> allowSuffixes = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> denySuffixes = new CopyOnWriteArrayList<>();

    /**
     * 禁止URL中包含哪些字符串
     */
    protected final CopyOnWriteArrayList<String> denyUrlStrings = new CopyOnWriteArrayList<>();

    /**
     * 是否启用严格路径限制，将会禁止相对路径访问
     */
    protected final AtomicBoolean strictPath = new AtomicBoolean(true);

    /**
     * 是否允许空的Origin和referer请求头
     */
    protected final AtomicBoolean allowEmptyOrigin = new AtomicBoolean(true);
    protected final AtomicBoolean allowEmptyReferer = new AtomicBoolean(true);
    /**
     * 允许哪些Origin和referer请求头的请求
     * 全等于或者正则匹配
     */
    protected final CopyOnWriteArrayList<String> allowOriginRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> allowRefererRegexList = new CopyOnWriteArrayList<>();

    /**
     * 不可见字符的URL编码检查哪些部分，header,parameter,cookie
     */
    protected final AtomicBoolean invisibleHeaderCheck = new AtomicBoolean(true);
    protected final AtomicBoolean invisibleParameterCheck = new AtomicBoolean(true);
    protected final AtomicBoolean invisibleCookieCheck = new AtomicBoolean(true);

    /**
     * 简单SQL注入的检查哪些部分，header,parameter,cookie
     */
    protected final AtomicBoolean sqlInjectHeaderCheck = new AtomicBoolean(true);
    protected final AtomicBoolean sqlInjectParameterCheck = new AtomicBoolean(true);
    protected final AtomicBoolean sqlInjectCookieCheck = new AtomicBoolean(true);

    /**
     * 简单远程调用的检查哪些部分，header,parameter,cookie
     */
    protected final AtomicBoolean remoteInvokeHeaderCheck = new AtomicBoolean(true);
    protected final AtomicBoolean remoteInvokeParameterCheck = new AtomicBoolean(true);
    protected final AtomicBoolean remoteInvokeCookieCheck = new AtomicBoolean(true);

    /**
     * SQL注入检查的判定正则表达式
     */
    protected final CopyOnWriteArrayList<String> sqlInjectRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<Pattern> sqlInjectPatternList = new CopyOnWriteArrayList<>();

    /**
     * 远程调用检查的判定正则表达式
     */
    protected final CopyOnWriteArrayList<String> remoteInvokeRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<Pattern> remoteInvokePatternList = new CopyOnWriteArrayList<>();

    /**
     * 简单文件访问的检查哪些部分，parameter
     */
    protected final AtomicBoolean illegalFileAccessParameterCheck = new AtomicBoolean(true);
    /**
     * 哪些文件访问路径包含或者文件名不合规列表
     */
    protected final CopyOnWriteArrayList<String> illegalFileAccessPaths = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> illegalFileAccessFileNames = new CopyOnWriteArrayList<>();

    /**
     * 简单的命令执行的检查哪些部分，parameter
     */
    protected final AtomicBoolean illegalCommandExecuteParameterCheck = new AtomicBoolean(true);
    /**
     * 哪些命令的不合规命令名列表
     */
    protected final CopyOnWriteArrayList<String> illegalCommandExecuteCommands = new CopyOnWriteArrayList<>();

    /**
     * 哪些IP将会拒绝访问
     * 全等于或者正则匹配
     */
    protected final CopyOnWriteArrayList<String> denyIpRegexList = new CopyOnWriteArrayList<>();

    /**
     * 哪些路径只允许在匹配的ip/origin/referer的情况下进行访问
     * 全等于或者正则匹配
     */
    protected final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> servletPathRegexAllowIpRegexMap = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> servletPathRegexAllowOriginRegexMap = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> servletPathRegexAllowRefererRegexMap = new ConcurrentHashMap<>();


    /**
     * 全局需要哪些名称的请求头或请求URL参数
     * 可用于例如authorization,token,origin,referer等请求头的要求检查
     */
    protected final CopyOnWriteArrayList<String> globalRequireHeadersParametersNameList = new CopyOnWriteArrayList<>();
    /**
     * 全局哪些路径可以允许不携带上面指定的参数列表
     * 可以作为上面定义的白名单使用
     * 可用于，例如authorization,token等登录参数的场景中，排除某些白名单路径
     */
    protected final CopyOnWriteArrayList<String> globalAllowMissingHeadersParametersNameServletPathRegexList = new CopyOnWriteArrayList<>();

    /**
     * 是否检测请求体
     */
    protected final AtomicBoolean requestBodyCheck = new AtomicBoolean(false);

    /**
     * xxe 外部实体注入攻击
     */
    protected final CopyOnWriteArrayList<Pattern> xmlXxePatternList = new CopyOnWriteArrayList<>();

    /**
     * html 跨站脚本攻击
     */
    protected final CopyOnWriteArrayList<Pattern> htmlXssPatternList = new CopyOnWriteArrayList<>();

    /**
     * Java反序列化攻击
     */
    protected final CopyOnWriteArrayList<Pattern> javaDeserializePatternList = new CopyOnWriteArrayList<>();

    public SecurityFilter() {
        init();
    }

    public void init() {
        denyMethods.clear();
        denyMethods.addAll(Arrays.asList(BAD_METHODS));

        allowedContentTypes.clear();
        allowedContentTypes.addAll(Arrays.asList(ALLOWED_CONTENT_TYPE));

        allowSuffixes.clear();
        allowSuffixes.addAll(Arrays.asList(PASS_SUFFIXES));

        denySuffixes.clear();
        denySuffixes.addAll(Arrays.asList(BAD_SUFFIXES));

        denyUrlStrings.clear();
        for (int i = 0; i < BAD_URL_CHARS.length(); i++) {
            char ch = BAD_URL_CHARS.charAt(i);
            denyUrlStrings.add("" + ch);
        }
        denyUrlStrings.addAll(Arrays.asList(BAD_URL_ENCODED_URL_CHARS));

        sqlInjectRegexList.clear();
        sqlInjectRegexList.addAll(Arrays.asList(SQL_INJECT_REGEXES));

        sqlInjectPatternList.clear();
        sqlInjectPatternList.addAll(Arrays.asList(SQL_INJECT_PATTERNS));

        illegalFileAccessPaths.clear();
        illegalFileAccessPaths.addAll(Arrays.asList(BAD_FILE_PATHS));

        illegalFileAccessFileNames.clear();
        illegalFileAccessFileNames.addAll(Arrays.asList(BAD_FILE_NAMES));

        illegalCommandExecuteCommands.clear();
        illegalCommandExecuteCommands.addAll(Arrays.asList(BAD_COMMANDS));

        remoteInvokeRegexList.clear();
        remoteInvokeRegexList.addAll(Arrays.asList(REMOTE_INVOKE_REGEXES));

        remoteInvokePatternList.clear();
        remoteInvokePatternList.addAll(Arrays.asList(REMOTE_INVOKE_PATTERNS));

        xmlXxePatternList.clear();
        xmlXxePatternList.addAll(Arrays.asList(XML_XXE_PATTERN));

        htmlXssPatternList.clear();
        htmlXssPatternList.addAll(Arrays.asList(HTML_XSS_PATTERN));

        javaDeserializePatternList.clear();
        javaDeserializePatternList.addAll(Arrays.asList(JAVA_DESERIALIZE_PATTERN));
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String reason = isSafeRequest(request);
        if (reason != null) {
            onUnSafeRejectRequest(reason, request, response);
            return;
        }
        if (requestBodyCheck.get()) {
            try {
                request = wrapSafeRequest(request);
            } catch (IllegalArgumentException e) {
                reason = e.getMessage();
                onUnSafeRejectRequest(reason, request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public void onUnSafeRejectRequest(String reason, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.err.println(String.format(">>>>>>>>>>> reject request 403 on [%5s] [%20s] %s, reason of %s", request.getMethod(), request.getContentType(), String.valueOf(request.getRequestURL()), reason));
        response.setStatus(403);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.write("bad request! cause reason is : " + reason);
        writer.flush();
    }

    public HttpServletRequest wrapSafeRequest(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        if (method == null) {
            method = "GET";
        }
        if ("GET".equalsIgnoreCase(method)) {
            return request;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return request;
        }
        contentType = contentType.toLowerCase();
        boolean isJson = false;
        boolean isForm = false;
        boolean isXml = false;
        if (contentType.contains("application/json")) {
            isJson = true;
        } else if (contentType.contains("application/x-www-form-urlencoded")) {
            isForm = true;
        } else if (contentType.contains("text/xml")) {
            isXml = true;
        }
        if (!isJson && !isForm && !isXml) {
            return request;
        }
        String characterEncoding = request.getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = "UTF-8";
        }
        HttpServletRequestProxyWrapper wrapper = new HttpServletRequestProxyWrapper(request);
        byte[] bodyBytes = wrapper.getBodyBytes();
        if (bodyBytes == null || bodyBytes.length == 0) {
            return wrapper;
        }

        String reason = null;
        String body = new String(bodyBytes, characterEncoding);
        if (isXml) {
            if ((reason = isSafeXml(body)) != null) {
                throw new IllegalArgumentException(reason);
            }
        }

        if (isForm) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] arr = pair.split("=", 2);
                if (arr.length != 2) {
                    continue;
                }
                String value = arr[1];
                if (illegalCommandExecuteParameterCheck.get()) {
                    if ((reason = matchIllegalCommandExecuteCommands(value)) != null) {
                        throw new IllegalArgumentException(reason);
                    }
                }
            }
        }

        if (sqlInjectParameterCheck.get()) {
            if ((reason = matchSqlInject(body)) != null) {
                throw new IllegalArgumentException(reason);
            }
        }

        if (remoteInvokeParameterCheck.get()) {
            if ((reason = matchRemoteInvoke(body)) != null) {
                throw new IllegalArgumentException(reason);
            }
        }

        if ((reason = matchJavaDeserialize(body)) != null) {
            throw new IllegalArgumentException(reason);
        }

        return wrapper;
    }

    public String matchJavaDeserialize(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (javaDeserializePatternList.isEmpty()) {
            return null;
        }
        str = str.toLowerCase();
        str = str.replaceAll("\\&lt;", "<");
        str = str.replaceAll("\\&gt;", ">");
        // 反序列化 判定
        for (Pattern pattern : javaDeserializePatternList) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return "bad body, maybe exists deserialize execute!";
            }
        }

        return null;
    }


    public String isSafeXml(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (xmlXxePatternList.isEmpty()) {
            return null;
        }
        str = str.toLowerCase();
        str = str.replaceAll("\\&lt;", "<");
        str = str.replaceAll("\\&gt;", ">");
        // xxe 判定
        for (Pattern pattern : xmlXxePatternList) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return "bad body, maybe exists xxe!";
            }
        }

        return null;
    }

    public String isSafeRequest(HttpServletRequest request) throws ServletException, IOException {
        String method = request.getMethod();
        String reason = null;
        if ((reason = isBadMethod(method)) != null) {
            return reason;
        }
        String contentType = request.getContentType();
        if ((reason = isBadContentType(contentType)) != null) {
            return reason;
        }
        String requestUrl = request.getRequestURL().toString().toLowerCase();
        if ((reason = containsInvisibleUrlEncodedAsciiChar(requestUrl)) != null) {
            return String.format("bad request url, reason of %s", reason);
        }
        if ((reason = isBadUrl(requestUrl)) != null) {
            return String.format("bad request url, reason of %s", reason);
        }
        if ((reason = isBadSuffix(requestUrl)) != null) {
            return String.format("bad request url, reason of %s", reason);
        }
        if (strictPath.get()) {
            String path = requestUrl;
            int idx = path.indexOf("://");
            if (idx >= 0) {
                path = path.substring(idx + "://".length());
            }
            idx = path.indexOf("/");
            if (idx >= 0) {
                path = path.substring(idx);
            }
            if ((reason = isBadStrictPath(path)) != null) {
                return String.format("bad request url path, reason of %s", reason);
            }
        }
        String requestURI = request.getRequestURI().toLowerCase();
        if ((reason = containsInvisibleUrlEncodedAsciiChar(requestURI)) != null) {
            return String.format("bad request uri, reason of %s", reason);
        }
        if ((reason = isBadUrl(requestURI)) != null) {
            return String.format("bad request uri, reason of %s", reason);
        }
        if ((reason = isBadSuffix(requestURI)) != null) {
            return String.format("bad request uri, reason of %s", reason);
        }
        if (strictPath.get()) {
            if ((reason = isBadStrictPath(requestURI)) != null) {
                return String.format("bad request uri, reason of %s", reason);
            }
        }
        if ((reason = matchIllegalFileAccessFilePath(requestURI)) != null) {
            return String.format("bad request uri, reason of %s", reason);
        }
        if ((reason = matchIllegalFileAccessFileName(requestURI)) != null) {
            return String.format("bad request uri, reason of %s", reason);
        }
        String servletPath = request.getServletPath().toLowerCase();
        if ((reason = containsInvisibleUrlEncodedAsciiChar(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = isBadUrl(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = isBadSuffix(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = isExceedRootPath(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if (strictPath.get()) {
            if ((reason = isBadStrictPath(servletPath)) != null) {
                return String.format("bad servlet path, reason of %s", reason);
            }
        }
        if ((reason = matchIllegalFileAccessFilePath(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = matchIllegalFileAccessFileName(servletPath)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        String ip = getIp(request);
        if ((reason = matchDenyForIp(ip)) != null) {
            return String.format("ban ip, reason of %s", reason);
        }
        if ((reason = matchNotAllowedPathForIp(servletPath, ip)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = isMissingHeadersParameters(servletPath, request)) != null) {
            return reason;
        }
        String origin = request.getHeader("Origin");
        if (origin == null || origin.isEmpty()) {
            if (!allowEmptyOrigin.get()) {
                reason = "not allow empty origin";
                return String.format("bad http header origin, reason of %s", reason);
            }
        }
        if ((reason = containsInvisibleUrlEncodedAsciiChar(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
        }
        if ((reason = isBadUrl(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
        }
        if ((reason = isBadOrigin(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
        }
        if ((reason = matchNotAllowedPathForOrigin(servletPath, origin)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        String referer = request.getHeader("Referer");
        if (referer != null) {
            int qIdx = referer.lastIndexOf("?");
            int hIdx = referer.lastIndexOf("#");
            int idx = -1;
            if (qIdx >= 0 && hIdx >= 0) {
                idx = Math.min(qIdx, hIdx);
            } else if (qIdx >= 0) {
                idx = qIdx;
            } else if (hIdx >= 0) {
                idx = hIdx;
            }
            if (idx >= 0) {
                referer = referer.substring(0, idx);
            }
        }
        if (referer == null || referer.isEmpty()) {
            if (!allowEmptyReferer.get()) {
                reason = "not allow empty referer";
                return String.format("bad http header referer, reason of %s", reason);
            }
        }
        if ((reason = containsInvisibleUrlEncodedAsciiChar(referer)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if ((reason = isBadReferer(referer)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if ((reason = matchNotAllowedPathForReferer(servletPath, referer)) != null) {
            return String.format("bad servlet path, reason of %s", reason);
        }
        if ((reason = isBadParts(request)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if (invisibleHeaderCheck.get() || sqlInjectHeaderCheck.get() || remoteInvokeHeaderCheck.get()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (invisibleHeaderCheck.get()) {
                    if ((reason = containsInvisibleUrlEncodedAsciiChar(name)) != null) {
                        return String.format("bad http header name [%s], reason of %s", name, reason);
                    }
                }
                if (sqlInjectHeaderCheck.get()) {
                    if ((reason = matchSqlInject(name)) != null) {
                        return String.format("bad http header name [%s], reason of %s", name, reason);
                    }
                }
                if (remoteInvokeHeaderCheck.get()) {
                    if ((reason = matchRemoteInvoke(name)) != null) {
                        return String.format("bad http header name [%s], reason of %s", name, reason);
                    }
                }
                Enumeration<String> headers = request.getHeaders(name);
                while (headers.hasMoreElements()) {
                    String value = headers.nextElement();
                    if (invisibleHeaderCheck.get()) {
                        if ((reason = containsInvisibleUrlEncodedAsciiChar(value)) != null) {
                            return String.format("bad http header name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (sqlInjectHeaderCheck.get()) {
                        if ((reason = matchSqlInject(value)) != null) {
                            return String.format("bad http header name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (remoteInvokeHeaderCheck.get()) {
                        if ((reason = matchRemoteInvoke(value)) != null) {
                            return String.format("bad http header name [%s] value, reason of %s", name, reason);
                        }
                    }
                }
            }
        }
        if (invisibleParameterCheck.get() || sqlInjectParameterCheck.get() || remoteInvokeParameterCheck.get()
                || illegalFileAccessParameterCheck.get() || illegalCommandExecuteParameterCheck.get()) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String name = entry.getKey();
                if (invisibleParameterCheck.get()) {
                    if ((reason = containsInvisibleUrlEncodedAsciiChar(name)) != null) {
                        return String.format("bad http query parameter name [%s], reason of %s", name, reason);
                    }
                }
                if (sqlInjectParameterCheck.get()) {
                    if ((reason = matchSqlInject(URLDecoder.decode(name, "UTF-8"))) != null) {
                        return String.format("bad http query parameter name [%s], reason of %s", name, reason);
                    }
                }
                if (remoteInvokeParameterCheck.get()) {
                    if ((reason = matchRemoteInvoke(URLDecoder.decode(name, "UTF-8"))) != null) {
                        return String.format("bad http query parameter name [%s], reason of %s", name, reason);
                    }
                }
                String[] value = entry.getValue();
                if (value == null || value.length == 0) {
                    continue;
                }
                for (String item : value) {
                    if (item == null) {
                        continue;
                    }
                    if (invisibleParameterCheck.get()) {
                        if ((reason = containsInvisibleUrlEncodedAsciiChar(item)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                    }
                    String decode = null;
                    if (sqlInjectParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if ((reason = matchSqlInject(decode)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (remoteInvokeParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if ((reason = matchRemoteInvoke(decode)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (illegalFileAccessParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if ((reason = matchIllegalFileAccessFilePath(decode)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                        if ((reason = matchIllegalFileAccessFileName(decode)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (illegalCommandExecuteParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if ((reason = matchIllegalCommandExecuteCommands(decode)) != null) {
                            return String.format("bad http query parameter name [%s] value, reason of %s", name, reason);
                        }
                    }
                }
            }
        }
        if (invisibleCookieCheck.get() || sqlInjectCookieCheck.get() || remoteInvokeCookieCheck.get()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie item : cookies) {
                    if (item == null) {
                        continue;
                    }
                    String name = item.getName();
                    if (invisibleCookieCheck.get()) {
                        if ((reason = containsInvisibleUrlEncodedAsciiChar(name)) != null) {
                            return String.format("bad http cookie name [%s], reason of %s", name, reason);
                        }
                    }
                    if (sqlInjectCookieCheck.get()) {
                        if ((reason = matchSqlInject(name)) != null) {
                            return String.format("bad http cookie name [%s], reason of %s", name, reason);
                        }
                    }
                    if (remoteInvokeCookieCheck.get()) {
                        if ((reason = matchRemoteInvoke(name)) != null) {
                            return String.format("bad http cookie name [%s], reason of %s", name, reason);
                        }
                    }
                    String value = item.getValue();
                    if (invisibleCookieCheck.get()) {
                        if ((reason = containsInvisibleUrlEncodedAsciiChar(value)) != null) {
                            return String.format("bad http cookie name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (sqlInjectCookieCheck.get()) {
                        if ((reason = matchSqlInject(value)) != null) {
                            return String.format("bad http cookie name [%s] value, reason of %s", name, reason);
                        }
                    }
                    if (remoteInvokeCookieCheck.get()) {
                        if ((reason = matchRemoteInvoke(value)) != null) {
                            return String.format("bad http cookie name [%s] value, reason of %s", name, reason);
                        }
                    }
                }
            }
        }
        return null;
    }

    public String matchDenyForIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return null;
        }

        if (denyIpRegexList.isEmpty()) {
            return null;
        }

        for (String regex : denyIpRegexList) {
            if (regex == null || regex.isEmpty()) {
                continue;
            }
            if (ip.equals(regex) || ip.matches(regex)) {
                return String.format("ip has been denied access [%s]", regex);
            }

        }

        return null;
    }

    public String matchNotAllowedPathForIp(String servletPath, String ip) {
        if (servletPath == null || servletPath.isEmpty()) {
            return null;
        }
        if (ip == null || ip.isEmpty()) {
            return null;
        }
        if (servletPathRegexAllowIpRegexMap.isEmpty()) {
            return null;
        }
        boolean matched = false;
        boolean allowed = false;
        for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : servletPathRegexAllowIpRegexMap.entrySet()) {
            String key = entry.getKey();
            if (servletPath.equals(key) || servletPath.matches(key)) {
                CopyOnWriteArrayList<String> value = entry.getValue();
                if (value == null || value.isEmpty()) {
                    continue;
                }
                for (String regex : value) {
                    if (regex == null || regex.isEmpty()) {
                        continue;
                    }
                    matched = true;
                    if (ip.equals(regex) || ip.matches(regex)) {
                        allowed = true;
                    }
                    if (allowed) {
                        break;
                    }
                }
            }
            if (allowed) {
                break;
            }
        }
        if (!matched) {
            return null;
        }
        return allowed ? null : String.format("ip not allowed [%s]", ip);
    }

    public String matchNotAllowedPathForOrigin(String servletPath, String origin) {
        if (servletPath == null || servletPath.isEmpty()) {
            return null;
        }
        if (origin == null || origin.isEmpty()) {
            return null;
        }
        if (servletPathRegexAllowOriginRegexMap.isEmpty()) {
            return null;
        }
        boolean matched = false;
        boolean allowed = false;
        for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : servletPathRegexAllowOriginRegexMap.entrySet()) {
            String key = entry.getKey();
            if (servletPath.equals(key) || servletPath.matches(key)) {
                CopyOnWriteArrayList<String> value = entry.getValue();
                if (value == null || value.isEmpty()) {
                    continue;
                }
                for (String regex : value) {
                    if (regex == null || regex.isEmpty()) {
                        continue;
                    }
                    matched = true;
                    if (origin.equals(regex) || origin.matches(regex)) {
                        allowed = true;
                    }
                    if (allowed) {
                        break;
                    }
                }
            }
            if (allowed) {
                break;
            }
        }
        if (!matched) {
            return null;
        }
        return allowed ? null : String.format("origin not allowed [%s]", origin);
    }

    public String matchNotAllowedPathForReferer(String servletPath, String referer) {
        if (servletPath == null || servletPath.isEmpty()) {
            return null;
        }
        if (referer == null || referer.isEmpty()) {
            return null;
        }
        if (servletPathRegexAllowRefererRegexMap.isEmpty()) {
            return null;
        }
        boolean matched = false;
        boolean allowed = false;
        for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : servletPathRegexAllowRefererRegexMap.entrySet()) {
            String key = entry.getKey();
            if (servletPath.equals(key) || servletPath.matches(key)) {
                CopyOnWriteArrayList<String> value = entry.getValue();
                if (value == null || value.isEmpty()) {
                    continue;
                }
                for (String regex : value) {
                    if (regex == null || regex.isEmpty()) {
                        continue;
                    }
                    matched = true;
                    if (referer.equals(regex) || referer.matches(regex)) {
                        allowed = true;
                    }
                    if (allowed) {
                        break;
                    }
                }
            }
            if (allowed) {
                break;
            }
        }
        if (!matched) {
            return null;
        }
        return allowed ? null : String.format("referer not allowed [%s]", referer);
    }

    public String isBadMethod(String method) {
        if (method == null || method.isEmpty()) {
            return null;
        }
        if (denyMethods.isEmpty()) {
            return null;
        }
        for (String item : denyMethods) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (item.equalsIgnoreCase(method)) {
                return String.format("un-support http method [%s]", item);
            }
        }
        return null;
    }

    public String isBadContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return null;
        }
        if (allowedContentTypes.isEmpty()) {
            return null;
        }
        contentType = contentType.toLowerCase();
        for (String item : allowedContentTypes) {
            if (contentType.contains(item)) {
                return null;
            }
        }
        return String.format("not allowed http content-type %s", contentType);
    }


    public String containsInvisibleUrlEncodedAsciiChar(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (int i = 0; i < 32; i++) {
            if (i == '\t') {
                continue;
            }
            if (str.indexOf(i) >= 0) {
                return String.format("contains invisible char [%d]", i);
            }
        }
        if (!str.contains("%")) {
            return null;
        }
        for (String item : BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS) {
            if (str.contains(item)) {
                return String.format("contains invisible url encoded char [%s]", item);
            }
        }
        return null;
    }

    public String isBadUrl(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (String item : denyUrlStrings) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (str.contains(item)) {
                return String.format("contains bad url string [%s]", item);
            }
        }
        return null;
    }

    public String isBadSuffix(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        int idx = str.lastIndexOf(".");
        if (idx < 0) {
            return null;
        }
        String suffix = str.substring(idx);
        for (String item : allowSuffixes) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (Objects.equals(suffix, item)) {
                return null;
            }
        }
        for (String item : denySuffixes) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (Objects.equals(suffix, item)) {
                return String.format("with bad suffix [%s]", item);
            }
        }
        return defaultAllowSuffix.get() ? String.format("default reject not allowed suffix [%s]", suffix) : null;
    }

    public String isExceedRootPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (!path.contains("..")
                && !path.contains(".")
                && !path.contains("~")
                && !path.contains("-")
        ) {
            return null;
        }
        String[] arr = path.split("[/\\\\]");
        Stack<String> stack = new Stack<>();
        for (String item : arr) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (".".equals(item)) {
                continue;
            }
            if ("~".equals(item)) {
                return "path exceed root path by [~]";
            }
            if ("-".equals(item)) {
                return "path exceed root path by [-]";
            }
            if ("..".equals(item)) {
                if (stack.isEmpty()) {
                    return "path exceed root path by [..]";
                } else {
                    stack.pop();
                }
            }
            stack.push(item);
        }
        return null;
    }

    public String isBadOrigin(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (allowOriginRegexList.isEmpty()) {
            return null;
        }
        for (String regex : allowOriginRegexList) {
            if (str.matches(regex)) {
                return null;
            }
        }
        return "origin not match any allowed";
    }

    public String isBadReferer(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (allowRefererRegexList.isEmpty()) {
            return null;
        }
        for (String regex : allowRefererRegexList) {
            if (str.matches(regex)) {
                return null;
            }
        }
        return "referer not match any allowed";
    }

    public String isBadParts(HttpServletRequest request) throws ServletException, IOException {
        String reason = null;
        if (request == null) {
            return null;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return null;
        }
        contentType = contentType.toLowerCase();
        if (!contentType.contains("multipart/form-data")) {
            return null;
        }
        Collection<Part> parts = request.getParts();
        if (parts == null || parts.isEmpty()) {
            return null;
        }
        for (Part part : parts) {
            String name = part.getName();
            String fileName = part.getSubmittedFileName();
            if (fileName == null || fileName.isEmpty()) {
                continue;
            }
            fileName = fileName.replaceAll("\\\\", "/");
            if ((reason = containsInvisibleUrlEncodedAsciiChar(fileName)) != null) {
                return String.format("part name [%s] not safe, reason of %s", name, reason);
            }
            if ((reason = isBadUrl(fileName)) != null) {
                return String.format("part name [%s] not safe, reason of %s", name, reason);
            }
            if ((reason = isBadSuffix(fileName)) != null) {
                return String.format("part name [%s] not safe, reason of %s", name, reason);
            }
            if ((reason = isExceedRootPath(fileName)) != null) {
                return String.format("part name [%s] not safe, reason of %s", name, reason);
            }
            if (strictPath.get()) {
                if ((reason = isBadStrictPath(fileName)) != null) {
                    return String.format("part name [%s] not safe, reason of %s", name, reason);
                }
            }
            if (sqlInjectParameterCheck.get()) {
                if ((reason = matchSqlInject(fileName)) != null) {
                    return String.format("part name [%s] not safe, reason of %s", name, reason);
                }
            }
            if (remoteInvokeParameterCheck.get()) {
                if ((reason = matchRemoteInvoke(fileName)) != null) {
                    return String.format("part name [%s] not safe, reason of %s", name, reason);
                }
            }
        }
        return null;
    }

    public String isBadStrictPath(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (str.contains("\\")) {
            return "path contains [\\]";
        }
        if (!str.startsWith("/")) {
            str = "/" + str;
        }
        if (str.contains("//")) {
            return "path contains [//]";
        }
        if (str.contains("/../") || str.endsWith("/..")) {
            return "path contains [/../]";
        }
        if (str.contains("/./") || str.endsWith("/.")) {
            return "path contains [/./]";
        }
        if (str.contains("/~/") || str.endsWith("/~")) {
            return "path contains [/~/]";
        }
        if (str.contains("/-/") || str.endsWith("/-")) {
            return "path contains [/-/]";
        }
        return null;
    }

    public String matchSqlInject(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (String item : sqlInjectRegexList) {
            if (str.matches(item)) {
                return String.format("string matched sql inject pattern [%s]", item);
            }
        }
        for (Pattern pattern : sqlInjectPatternList) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return String.format("string contains sql inject pattern [%s]", pattern.pattern());
            }
        }
        return null;
    }


    public String matchIllegalFileAccessFilePath(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        boolean isFirst = true;
        for (String item : illegalFileAccessPaths) {
            if (isFirst) {
                str = str.replaceAll("\\\\", "/");
            }
            if (str.equals(item)) {
                return String.format("illegal file path [%s]", item);
            }
            if (str.contains("/" + item + "/")) {
                return String.format("illegal file path [/%s/]", item);
            }
            if (str.endsWith("/" + item)) {
                return String.format("illegal file path [/%s]", item);
            }
            if (str.startsWith(item + "/")) {
                return String.format("illegal file path [%s/]", item);
            }
            isFirst = false;
        }
        return null;
    }

    public String matchIllegalFileAccessFileName(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String name = str;
        boolean isFirst = true;
        for (String item : illegalFileAccessFileNames) {
            if (isFirst) {
                int idx = -1;
                for (int i = name.length() - 1; i >= 0; i--) {
                    if (name.charAt(i) == '/' || name.charAt(i) == '\\') {
                        idx = i;
                        break;
                    }
                }
                if (idx >= 0) {
                    name = name.substring(0, idx);
                }
                if (name.isEmpty()) {
                    return null;
                }
            }
            if (name.equals(item)) {
                return String.format("illegal file name [%s]", item);
            }
            isFirst = false;
        }
        return null;
    }

    public String matchIllegalCommandExecuteCommands(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String cmd = str;
        boolean isFirst = true;
        for (String item : illegalCommandExecuteCommands) {
            if (isFirst) {
                str = str.trim();
                if (str.isEmpty()) {
                    return null;
                }
                String[] arr = str.split("\\s+", 2);
                cmd = arr[0].toLowerCase();
                if (cmd.isEmpty()) {
                    return null;
                }
            }
            if (cmd.equals(item)) {
                return String.format("illegal command [%s]", item);
            }
            isFirst = false;
        }
        return null;
    }

    public String matchRemoteInvoke(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (String item : remoteInvokeRegexList) {
            if (str.matches(item)) {
                return String.format("string matched remote invoke pattern [%s]", item);
            }
        }
        for (Pattern pattern : remoteInvokePatternList) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return String.format("string contains remote invoke pattern [%s]", pattern.pattern());
            }
        }
        return null;
    }

    public String isMissingHeadersParameters(String servletPath, HttpServletRequest request) {
        if (servletPath == null || servletPath.isEmpty()) {
            return null;
        }
        if (globalRequireHeadersParametersNameList.isEmpty()) {
            return null;
        }
        if (globalAllowMissingHeadersParametersNameServletPathRegexList.isEmpty()) {
            return null;
        }
        for (String key : globalAllowMissingHeadersParametersNameServletPathRegexList) {
            if (servletPath.equals(key) || servletPath.matches(key)) {
                return null;
            }
        }

        for (String name : globalRequireHeadersParametersNameList) {
            if (request.getHeader(name) == null && request.getParameter(name) == null) {
                return String.format("require header/parameter [%s]", name);
            }
        }

        return null;
    }


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
}
