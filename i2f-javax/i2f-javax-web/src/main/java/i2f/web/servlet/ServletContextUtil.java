package i2f.web.servlet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/26 16:19
 * @desc
 */
public class ServletContextUtil {
    public static final String UserAgent = "User-Agent";
    public static final String FORWARD_DATA_ATTR_KEY = "forward-data";

    public static String getToken(HttpServletRequest request, String tokenName) {
        String token = "";
        token = request.getHeader(tokenName);
        if (token == null || "".equals(token)) {
            token = request.getParameter(tokenName);
        }
        if (token == null || "".equals(token)) {
            token = (String) request.getSession().getAttribute(tokenName);
        }
        if (token == null || "".equals(token)) {
            token = (String) request.getAttribute(tokenName);
        }
        return token;
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    public static Cookie[] getCookie(HttpServletRequest request) {
        return request.getCookies();
    }

    public static List<String> getHeaders(HttpServletRequest request, String key) {
        Enumeration<String> headers = request.getHeaders(key);
        List<String> ret = new ArrayList<>();
        while (headers.hasMoreElements()) {
            ret.add(headers.nextElement());
        }
        return ret;
    }

    public static ServletContext getServletContext(HttpSession session) {
        return session.getServletContext();
    }

    public static File getContextPath(ServletContext context, String relativePath) {
        String rootPath = context.getRealPath("");
        if (relativePath == null || relativePath.isEmpty()) {
            return new File(rootPath);
        }
        return new File(rootPath, relativePath);
    }

    public static RequestDispatcher getDispatcher(HttpServletRequest request, String target) {
        return request.getRequestDispatcher(target);
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        getDispatcher(request, target).forward(request, response);
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, String target, Object attrObj) throws ServletException, IOException {
        request.setAttribute(FORWARD_DATA_ATTR_KEY, attrObj);
        getDispatcher(request, target).forward(request, response);
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, String target, String attrKey, Object attrObj) throws ServletException, IOException {
        request.setAttribute(attrKey, attrObj);
        getDispatcher(request, target).forward(request, response);
    }

    public static void include(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        getDispatcher(request, target).include(request, response);
    }

    public static void redirect(HttpServletResponse response, String target) throws IOException {
        response.sendRedirect(target);
    }

    public static void sessionSet(HttpSession session, String key, Object val) {
        session.setAttribute(key, val);
    }

    public static Object sessionGet(HttpSession session, String key) {
        return session.getAttribute(key);
    }

    public static void requestSet(HttpServletRequest request, String key, Object val) {
        request.setAttribute(key, val);
    }

    public static Object requestGet(HttpServletRequest request, String key) {
        return request.getAttribute(key);
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(UserAgent);
        if (userAgent == null) {
            userAgent = "";
        }
        return userAgent;
    }

    public static String getPossibleValue(String key, HttpServletRequest request) {
        String ret = request.getHeader(key);
        if (ret == null || ret.isEmpty()) {
            ret = request.getParameter(key);
        }
        if (ret == null || ret.isEmpty()) {
            Object val = request.getAttribute(key);
            if (val != null) {
                ret = String.valueOf(val);
            }
        }
        return ret;
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
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

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * <p>
     * 举例：http://127.0.0.1:8080
     * 如果是部署在Tomcat中，指定了上下文：test
     * 那么：
     * http://127.0.0.1:8080/test
     *
     * @return 服务地址
     */
    public static String getContextBaseUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }


}

