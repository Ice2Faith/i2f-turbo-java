package i2f.springboot.trace.mdc.gateway;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Gateway 的 MDC 请求头传递
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.gateway.enable:true}")
@ConditionalOnClass(GlobalFilter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Component
public class MdcGatewayFilter implements GlobalFilter, Ordered, EnvironmentAware {
    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String appName = environment.getProperty("spring.application.name", "noappname");

        String traceId = MdcTraces.getOrGenTraceId(() -> {
            for (String header : MdcTraces.TRACE_ID_HEADERS) {
                String value = request.getHeaders().getFirst(header);
                if (!StringUtils.isEmpty(value)) {
                    return value;
                }
            }
            return null;
        });


        String traceSource = null;
        for (String header : MdcTraces.TRACE_SOURCE_HEADERS) {
            traceSource = request.getHeaders().getFirst(header);
            if (!StringUtils.isEmpty(traceSource)) {
                break;
            }
        }
        if (StringUtils.isEmpty(traceSource)) {
            traceSource = appName;
        }

        MdcHolder.put(MdcTraces.TRACE_ID, traceId);
        MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);
        MdcHolder.put(MdcTraces.TRACE_URL, request.getURI().toString());
        MdcHolder.put(MdcTraces.TRACE_IP, getIp(request));
        MdcHolder.put(MdcTraces.TRACE_APP, appName);

        return chain.filter(exchange.mutate().request(
                        request.mutate()
                                .header(MdcTraces.TRACE_ID, traceId)
                                .header(MdcTraces.TRACE_SOURCE, traceSource)
                                .build()
                ).build())
                .then(Mono.defer(() -> {
                    MdcHolder.remove(MdcTraces.TRACE_ID);
                    MdcHolder.remove(MdcTraces.TRACE_SOURCE);

                    return Mono.empty();
                }));
    }

    @Override
    public int getOrder() {
        return -990;
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