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
        if (!isSafeRequest(request)) {
            onUnSafeRejectRequest(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    public void onUnSafeRejectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(403);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.write("bad request!");
        writer.flush();
    }

    public boolean isSafeRequest(HttpServletRequest request) throws ServletException, IOException {
        String method = request.getMethod();
        if (isBadMethod(method)) {
            return false;
        }
        String requestUrl = request.getRequestURL().toString().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(requestUrl)) {
            return false;
        }
        if (isBadUrl(requestUrl)) {
            return false;
        }
        if (isBadSuffix(requestUrl)) {
            return false;
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
            if (isBadStrictPath(path)) {
                return false;
            }
        }
        String requestURI = request.getRequestURI().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(requestURI)) {
            return false;
        }
        if (isBadUrl(requestURI)) {
            return false;
        }
        if (isBadSuffix(requestURI)) {
            return false;
        }
        if (strictPath.get()) {
            if (isBadStrictPath(requestURI)) {
                return false;
            }
        }
        if (matchIllegalFileAccessFilePath(requestURI)) {
            return false;
        }
        if (matchIllegalFileAccessFileName(requestURI)) {
            return false;
        }
        String servletPath = request.getServletPath().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(servletPath)) {
            return false;
        }
        if (isBadUrl(servletPath)) {
            return false;
        }
        if (isBadSuffix(servletPath)) {
            return false;
        }
        if (isExceedRootPath(servletPath)) {
            return false;
        }
        if (strictPath.get()) {
            if (isBadStrictPath(servletPath)) {
                return false;
            }
        }
        if (matchIllegalFileAccessFilePath(servletPath)) {
            return false;
        }
        if (matchIllegalFileAccessFileName(servletPath)) {
            return false;
        }
        String origin = request.getHeader("Origin");
        if (containsInvisibleUrlEncodedAsciiChar(origin)) {
            return false;
        }
        if (isBadUrl(origin)) {
            return false;
        }
        if (isBadOrigin(origin)) {
            return false;
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
        if (containsInvisibleUrlEncodedAsciiChar(referer)) {
            return false;
        }
        if (isBadReferer(referer)) {
            return false;
        }
        if (isBadParts(request)) {
            return false;
        }
        if (invisibleHeaderCheck.get() || sqlInjectHeaderCheck.get()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (invisibleHeaderCheck.get()) {
                    if (containsInvisibleUrlEncodedAsciiChar(name)) {
                        return false;
                    }
                }
                if (sqlInjectHeaderCheck.get()) {
                    if (matchSqlInject(name)) {
                        return false;
                    }
                }
                Enumeration<String> headers = request.getHeaders(name);
                while (headers.hasMoreElements()) {
                    String value = headers.nextElement();
                    if (invisibleHeaderCheck.get()) {
                        if (containsInvisibleUrlEncodedAsciiChar(value)) {
                            return false;
                        }
                    }
                    if (sqlInjectHeaderCheck.get()) {
                        if (matchSqlInject(value)) {
                            return false;
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
                    if (containsInvisibleUrlEncodedAsciiChar(name)) {
                        return false;
                    }
                }
                if (sqlInjectParameterCheck.get()) {
                    if (matchSqlInject(URLDecoder.decode(name, "UTF-8"))) {
                        return false;
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
                        if (containsInvisibleUrlEncodedAsciiChar(item)) {
                            return false;
                        }
                    }
                    String decode = null;
                    if (sqlInjectParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if (matchSqlInject(decode)) {
                            return false;
                        }
                    }
                    if (illegalFileAccessParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if (matchIllegalFileAccessFilePath(decode)) {
                            return false;
                        }
                        if (matchIllegalFileAccessFileName(decode)) {
                            return false;
                        }
                    }
                    if (illegalCommandExecuteParameterCheck.get()) {
                        if (decode == null) {
                            decode = URLDecoder.decode(item, "UTF-8");
                        }
                        if (matchIllegalCommandExecuteCommands(decode)) {
                            return false;
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
                        if (containsInvisibleUrlEncodedAsciiChar(name)) {
                            return false;
                        }
                    }
                    if (sqlInjectCookieCheck.get()) {
                        if (matchSqlInject(name)) {
                            return false;
                        }
                    }
                    String value = item.getValue();
                    if (invisibleCookieCheck.get()) {
                        if (containsInvisibleUrlEncodedAsciiChar(value)) {
                            return false;
                        }
                    }
                    if (sqlInjectCookieCheck.get()) {
                        if (matchSqlInject(value)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isBadMethod(String method) {
        if (method == null || method.isEmpty()) {
            return false;
        }
        if (denyMethods.isEmpty()) {
            return false;
        }
        for (String item : denyMethods) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (item.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }


    public boolean containsInvisibleUrlEncodedAsciiChar(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < 32; i++) {
            if (i == '\t') {
                continue;
            }
            if (str.indexOf(i) >= 0) {
                return true;
            }
        }
        if (!str.contains("%")) {
            return false;
        }
        for (String item : BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS) {
            if (str.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBadUrl(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (String item : denyUrlStrings) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (str.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBadSuffix(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int idx = str.lastIndexOf(".");
        if (idx < 0) {
            return false;
        }
        String suffix = str.substring(idx);
        for (String item : allowSuffixes) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (Objects.equals(suffix, item)) {
                return false;
            }
        }
        for (String item : denySuffixes) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (Objects.equals(suffix, item)) {
                return true;
            }
        }
        return defaultAllowSuffix.get();
    }

    public boolean isExceedRootPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        if (!path.contains("..")
                && !path.contains(".")
                && !path.contains("~")
                && !path.contains("-")
        ) {
            return false;
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
                return true;
            }
            if ("-".equals(item)) {
                return true;
            }
            if ("..".equals(item)) {
                if (stack.isEmpty()) {
                    return true;
                } else {
                    stack.pop();
                }
            }
            stack.push(item);
        }
        return false;
    }

    public boolean isBadOrigin(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (allowOriginRegexList.isEmpty()) {
            return false;
        }
        for (String regex : allowOriginRegexList) {
            if (str.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBadReferer(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (allowRefererRegexList.isEmpty()) {
            return false;
        }
        for (String regex : allowRefererRegexList) {
            if (str.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBadParts(HttpServletRequest request) throws ServletException, IOException {
        if (request == null) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        contentType = contentType.toLowerCase();
        if (!contentType.contains("multipart/form-data")) {
            return false;
        }
        Collection<Part> parts = request.getParts();
        if (parts == null || parts.isEmpty()) {
            return false;
        }
        for (Part part : parts) {
            String fileName = part.getSubmittedFileName();
            if (fileName == null || fileName.isEmpty()) {
                continue;
            }
            fileName = fileName.replaceAll("\\\\", "/");
            if (containsInvisibleUrlEncodedAsciiChar(fileName)) {
                return true;
            }
            if (isBadUrl(fileName)) {
                return true;
            }
            if (isBadSuffix(fileName)) {
                return true;
            }
            if (isExceedRootPath(fileName)) {
                return true;
            }
            if (strictPath.get()) {
                if (isBadStrictPath(fileName)) {
                    return true;
                }
            }
            if (sqlInjectParameterCheck.get()) {
                if (matchSqlInject(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBadStrictPath(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if (str.contains("\\")) {
            return true;
        }
        if (!str.startsWith("/")) {
            str = "/" + str;
        }
        if (str.contains("//")) {
            return true;
        }
        if (str.contains("/../") || str.endsWith("/..")) {
            return true;
        }
        if (str.contains("/./") || str.endsWith("/.")) {
            return true;
        }
        if (str.contains("/~/") || str.endsWith("/~")) {
            return true;
        }
        if (str.contains("/-/") || str.endsWith("/-")) {
            return true;
        }
        return false;
    }

    public boolean matchSqlInject(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (String item : sqlInjectRegexList) {
            if (str.matches(item)) {
                return true;
            }
        }
        for (Pattern pattern : sqlInjectPatternList) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }


    public boolean matchIllegalFileAccessFilePath(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        boolean isFirst = true;
        for (String item : illegalFileAccessPaths) {
            if (isFirst) {
                str = str.replaceAll("\\\\", "/");
            }
            if (str.contains("/" + item + "/")
                    || str.endsWith("/" + item)
                    || str.startsWith(item + "/")
                    || str.equals(item)
            ) {
                return true;
            }
            isFirst = false;
        }
        return false;
    }

    public boolean matchIllegalFileAccessFileName(String str) {
        if (str == null || str.isEmpty()) {
            return false;
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
                    return false;
                }
            }
            if (name.equals(item)) {
                return true;
            }
            isFirst = false;
        }
        return false;
    }

    public boolean matchIllegalCommandExecuteCommands(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        String cmd = str;
        boolean isFirst = true;
        for (String item : illegalCommandExecuteCommands) {
            if (isFirst) {
                str = str.trim();
                if (str.isEmpty()) {
                    return false;
                }
                String[] arr = str.split("\\s+", 2);
                cmd = arr[0].toLowerCase();
                if (cmd.isEmpty()) {
                    return false;
                }
            }
            if (cmd.equals(item)) {
                return true;
            }
            isFirst = false;
        }
        return false;
    }
}
