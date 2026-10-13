package i2f.springboot.ai.mcp.server.provider;

import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.net.http.data.HttpHeaders;
import i2f.web.servlet.ServletContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/10/13 9:27
 * @desc
 */
public class McpServerRequestContextUtil {
    public static final String HTTP_HEADERS = "http_headers";
    public static final String HTTP_PARAMETERS = "http_parameters";
    public static final String HTTP_CLIENT_IP = "http_client_ip";
    public static final String HTTP_REMOTE_ADDR = "http_remote_addr";

    public static void fillToolContext(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpHeaders requestHeaders = HttpHeaders.create();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                String value = headers.nextElement();
                requestHeaders.add(name, value);
            }
        }

        ToolCallContextHolder.put(HTTP_HEADERS, requestHeaders);

        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, List<String>> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            params.put(entry.getKey(), new ArrayList<>(Arrays.asList(entry.getValue())));
        }

        ToolCallContextHolder.put(HTTP_PARAMETERS, params);

        String ip = ServletContextUtil.getIp(request);
        ToolCallContextHolder.put(HTTP_CLIENT_IP, ip);

        String remoteAddr = request.getRemoteAddr();
        ToolCallContextHolder.put(HTTP_REMOTE_ADDR, remoteAddr);
    }

    public static void fillToolContext(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (request == null) {
            return;
        }
        HttpHeaders requestHeaders = HttpHeaders.create();
        io.netty.handler.codec.http.HttpHeaders nettyHeaders = request.headers();
        Set<String> names = nettyHeaders.names();
        for (String name : names) {
            List<String> values = nettyHeaders.getAll(name);
            requestHeaders.add(name, values);
        }
        ToolCallContextHolder.put(HTTP_HEADERS, requestHeaders);

        Map<String, List<String>> params = new QueryStringDecoder(request.uri()).parameters();
        ToolCallContextHolder.put(HTTP_PARAMETERS, params);

        String ip = getIp(ctx, request);
        ToolCallContextHolder.put(HTTP_CLIENT_IP, ip);

        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteAddr = addr.getAddress().getHostAddress();
        ToolCallContextHolder.put(HTTP_REMOTE_ADDR, remoteAddr);
    }

    public static String getIp(ChannelHandlerContext ctx, FullHttpRequest request) {
        String ip = request.headers().get("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.headers().get("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.headers().get("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.headers().get("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
            ip = addr.getAddress().getHostAddress();
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
