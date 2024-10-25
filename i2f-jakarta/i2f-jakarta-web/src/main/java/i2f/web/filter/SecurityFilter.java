package i2f.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;
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
            "\\s*;\\s*(select|update|insert|delete|create|drop|grant|revoke|alter|comment|show|load|show)\\s+",
            "\\s*;\\s*(begin|rollback|commit)\\s*(;)?",
            "\\s*;\\s*(updatexml|load)\\s*(\\()?",
            "\\s+--\\s*"
    };
    public static final Pattern[] SQL_INJECT_PATTERNS;

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
    }


    protected final CopyOnWriteArrayList<String> denyMethods = new CopyOnWriteArrayList<>();

    protected final AtomicBoolean defaultAllowSuffix = new AtomicBoolean(true);
    protected final CopyOnWriteArrayList<String> allowSuffixes = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> denySuffixes = new CopyOnWriteArrayList<>();

    protected final CopyOnWriteArrayList<String> denyUrlStrings = new CopyOnWriteArrayList<>();

    protected final AtomicBoolean strictPath = new AtomicBoolean(true);

    protected final CopyOnWriteArrayList<String> allowOriginRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> allowRefererRegexList = new CopyOnWriteArrayList<>();

    protected final AtomicBoolean invisibleHeaderCheck = new AtomicBoolean(true);
    protected final AtomicBoolean invisibleParameterCheck = new AtomicBoolean(true);
    protected final AtomicBoolean invisibleCookieCheck = new AtomicBoolean(true);

    protected final AtomicBoolean sqlInjectHeaderCheck = new AtomicBoolean(true);
    protected final AtomicBoolean sqlInjectParameterCheck = new AtomicBoolean(true);
    protected final AtomicBoolean sqlInjectCookieCheck = new AtomicBoolean(true);


    protected final CopyOnWriteArrayList<String> sqlInjectRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<Pattern> sqlInjectPatternList = new CopyOnWriteArrayList<>();

    protected final AtomicBoolean illegalFileAccessParameterCheck = new AtomicBoolean(true);
    protected final CopyOnWriteArrayList<String> illegalFileAccessPaths = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> illegalFileAccessFileNames = new CopyOnWriteArrayList<>();

    protected final AtomicBoolean illegalCommandExecuteParameterCheck = new AtomicBoolean(true);
    protected final CopyOnWriteArrayList<String> illegalCommandExecuteCommands = new CopyOnWriteArrayList<>();

    public SecurityFilter() {
        init();
    }

    public void init() {
        denyMethods.clear();
        denyMethods.addAll(Arrays.asList(BAD_METHODS));

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
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String reason = isSafeRequest(request);
        if (reason != null) {
            onUnSafeRejectRequest(reason, request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    public void onUnSafeRejectRequest(String reason, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(403);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.write("bad request! cause reason is : " + reason);
        writer.flush();
    }

    public String isSafeRequest(HttpServletRequest request) throws ServletException, IOException {
        String method = request.getMethod();
        String reason = null;
        if ((reason = isBadMethod(method)) != null) {
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
        String origin = request.getHeader("Origin");
        if ((reason = containsInvisibleUrlEncodedAsciiChar(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
        }
        if ((reason = isBadUrl(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
        }
        if ((reason = isBadOrigin(origin)) != null) {
            return String.format("bad http header origin, reason of %s", reason);
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
        if ((reason = containsInvisibleUrlEncodedAsciiChar(referer)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if ((reason = isBadReferer(referer)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if ((reason = isBadParts(request)) != null) {
            return String.format("bad http header referer, reason of %s", reason);
        }
        if (invisibleHeaderCheck.get() || sqlInjectHeaderCheck.get()) {
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
                }
            }
        }
        if (invisibleParameterCheck.get() || sqlInjectParameterCheck.get()
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
        if (invisibleCookieCheck.get() || sqlInjectCookieCheck.get()) {
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
                }
            }
        }
        return null;
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
}
