package i2f.springboot.ops.openai.controller;

import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.net.http.data.HttpHeaders;
import i2f.web.servlet.ServletContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/11/9 11:52
 * @desc
 */
public class HttpServletToolContextUtil {
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
}
