package i2f.web.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/10/23 21:25
 * @desc
 */
@Data
@NoArgsConstructor
public class SecurityFilter extends OncePerHttpServletFilter {

    public static final String[] BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS;
    public static final String BAD_URL_CHARS = ";:'\",<.>?[]\\|{}`~!$#^()";
    public static final String[] BAD_URL_ENCODED_URL_CHARS;
    public static final String[] BAD_SUFFIXES = {
            ".java", ".jsp", ".jsf", ".properties", ".yaml", ".yml", ".xml", ".json", ".drl", ".class", ".jar", ".war",
            ".py", ".pyc",
            ".php",
            ".sql",
            ".sh", ".bat", ".exe", ".vbs", ".cmd", ".ps", ".elf", ".msi", ".repo",
            ".conf", ".d", ".cnf", ".ini", ".inf",
            ".log", ".out", ".err",
            ".hprof", ".dump", ".dmp", ".bak",
            ".lock",
    };

    static {
        List<String> badInvisibleUrlEncodedAsciiChars = new ArrayList<String>();
        for (int i = 0; i < 32; i++) {
            if (i == '\t') {
                continue;
            }
            badInvisibleUrlEncodedAsciiChars.add(String.format("%%%02x", i));
        }
        BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS = badInvisibleUrlEncodedAsciiChars.toArray(new String[0]);
        List<String> badChars = new ArrayList<String>();
        for (int i = 0; i < BAD_URL_CHARS.length(); i++) {
            int ch = BAD_URL_CHARS.charAt(i);
            badChars.add(String.format("%%%02x", ch));
        }
        BAD_URL_ENCODED_URL_CHARS = badChars.toArray(new String[0]);
    }

    protected final CopyOnWriteArrayList<String> whiteListOriginRegexList = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<String> whiteListRefererRegexList = new CopyOnWriteArrayList<>();

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
        if ("TRACE".equalsIgnoreCase(method)) {
            return false;
        }
        String requestUrl = request.getRequestURL().toString().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(requestUrl)) {
            return false;
        }
        String requestURI = request.getRequestURI().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(requestUrl)) {
            return false;
        }
        if (containsBadChar(requestURI)) {
            return false;
        }
        if (isBadSuffix(requestURI)) {
            return false;
        }
        String servletPath = request.getServletPath().toLowerCase();
        if (containsInvisibleUrlEncodedAsciiChar(requestUrl)) {
            return false;
        }
        if (containsBadChar(servletPath)) {
            return false;
        }
        if (isBadSuffix(servletPath)) {
            return false;
        }
        if (isExceedRootPath(servletPath)) {
            return false;
        }
        String origin = request.getHeader("Origin");
        if (containsInvisibleUrlEncodedAsciiChar(origin)) {
            return false;
        }
        if (containsBadChar(origin)) {
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
        return false;
    }


    public boolean containsInvisibleUrlEncodedAsciiChar(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (String item : BAD_INVISIBLE_URL_ENCODED_ASCII_CHARS) {
            if (str.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsBadChar(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < BAD_URL_CHARS.length(); i++) {
            char ch = str.charAt(i);
            if (str.indexOf(ch) >= 0) {
                return true;
            }
        }
        for (String item : BAD_URL_ENCODED_URL_CHARS) {
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
        for (String item : BAD_SUFFIXES) {
            if (Objects.equals(suffix, item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExceedRootPath(String path) {
        if (path == null || path.isEmpty()) {
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
        for (String regex : whiteListOriginRegexList) {
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
        for (String regex : whiteListRefererRegexList) {
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
            if (containsInvisibleUrlEncodedAsciiChar(fileName)) {
                return true;
            }
            if (isBadSuffix(fileName)) {
                return true;
            }
            if (isExceedRootPath(fileName)) {
                return true;
            }
        }
        return false;
    }
}
