package i2f.springboot.trace.mdc.springmvc;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Servlet 请求的 MDC 请求头传递
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.servlet.enable:true}")
@ConditionalOnClass(OncePerRequestFilter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Component
public class MdcWebFilter extends OncePerRequestFilter implements EnvironmentAware, Ordered {
    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String appName = environment.getProperty("spring.application.name", "noappname");

        String traceId = MdcTraces.getOrGenTraceId(() -> {
            for (String header : MdcTraces.TRACE_ID_HEADERS) {
                String value = request.getHeader(header);
                if (!StringUtils.isEmpty(value)) {
                    return value;
                }
            }
            for (String header : MdcTraces.TRACE_ID_HEADERS) {
                Object value = request.getAttribute(header);
                if(value instanceof String) {
                    if (!StringUtils.isEmpty(value)) {
                        return (String)value;
                    }
                }
            }
            return null;
        });


        String traceSource = null;
        for (String header : MdcTraces.TRACE_SOURCE_HEADERS) {
            traceSource = request.getHeader(header);
            if (!StringUtils.isEmpty(traceSource)) {
                break;
            }
        }
        if(StringUtils.isEmpty(traceSource)){
            for (String header : MdcTraces.TRACE_SOURCE_HEADERS) {
                Object attribute = request.getAttribute(header);
                if(attribute instanceof String) {
                    traceSource = (String) attribute;
                    if (!StringUtils.isEmpty(traceSource)) {
                        break;
                    }
                }
            }
        }
        if (StringUtils.isEmpty(traceSource)) {
            traceSource = appName;
        }

        request.setAttribute(MdcTraces.TRACE_ID, traceId);
        request.setAttribute(MdcTraces.TRACE_SOURCE, traceSource);

        MdcHolder.put(MdcTraces.TRACE_ID, traceId);
        MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);
        MdcHolder.put(MdcTraces.TRACE_URL, request.getRequestURL().toString());
        MdcHolder.put(MdcTraces.TRACE_IP, getIp(request));
        MdcHolder.put(MdcTraces.TRACE_APP, appName);

        try {
            chain.doFilter(request, response);
        } finally {
            MdcHolder.remove(MdcTraces.TRACE_ID);
            MdcHolder.remove(MdcTraces.TRACE_SOURCE);
            MdcHolder.remove(MdcTraces.TRACE_URL);
            MdcHolder.remove(MdcTraces.TRACE_IP);
            MdcHolder.remove(MdcTraces.TRACE_APP);
        }
    }

    @Override
    public int getOrder() {
        return -990;
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
