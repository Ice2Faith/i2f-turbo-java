package i2f.spring.web.webflux;


import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/3/26 16:19
 * @desc
 */
public class WebfluxContextUtil {

    public static final String UserAgent = "User-Agent";

    public static String getToken(ServerHttpRequest request, String tokenName) {
        String token = "";
        token = request.getHeaders().getFirst(tokenName);
        if (token == null || token.isEmpty()) {
            token = request.getQueryParams().getFirst(tokenName);
        }
        return token;
    }


    public static MultiValueMap<String, HttpCookie> getCookie(ServerHttpRequest request) {
        return request.getCookies();
    }

    public static List<String> getHeaders(ServerHttpRequest request, String key) {
        List<String> headers = request.getHeaders().get(key);
        return new ArrayList<>(headers);
    }


    public static String getUserAgent(ServerHttpRequest request) {
        String userAgent = request.getHeaders().getFirst(UserAgent);
        if (userAgent == null) {
            userAgent = "";
        }
        return userAgent;
    }

    public static String getPossibleValue(String key, ServerHttpRequest request) {
        String ret = request.getHeaders().getFirst(key);
        if (ret == null || ret.isEmpty()) {
            ret = request.getQueryParams().getFirst(key);
        }
        return ret;
    }

    public static String getIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null) {
                ip = remoteAddress.getAddress().getHostAddress();
            } else {
                ip = "0.0.0.0";
            }
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

