package i2f.springboot.trace.mdc.spring.gateway;

import i2f.trace.mdc.TraceMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Ice2Faith
 * @date 2024/11/12 15:35
 */
@Slf4j
@Component
public class GatewayTraceSlf4jMdcGlobalFilter implements GlobalFilter, Ordered {
    public static final int ORDER = -9000;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = TraceMdcUtil.fetchOrGenTraceIdWithSet(request, (req) -> {
            for (String name : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                String str = req.getHeaders().getFirst(name);
                if (StringUtils.hasLength(str)) {
                    return str;
                }
            }
            for (String name : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                String str = req.getQueryParams().getFirst(name);
                if (StringUtils.hasLength(str)) {
                    return str;
                }
            }
            return null;
        });

        TraceMdcUtil.TRACE_SOURCE.set(String.valueOf(request.getURI()));
        log.info("trace:[" + traceId + "] " + TraceMdcUtil.TRACE_SOURCE.get());

        MDC.put(TraceMdcUtil.MDC_TRACE_ID_NAME, traceId);
        MDC.put(TraceMdcUtil.MDC_TRACE_SOURCE_NAME, TraceMdcUtil.TRACE_SOURCE.get());

        return chain.filter(exchange.mutate().request(
                        request.mutate()
                                .header(TraceMdcUtil.MDC_TRACE_ID_NAME, traceId)
                                .build()
                ).build())
                .then(Mono.defer(() -> {
                    MDC.remove(TraceMdcUtil.MDC_TRACE_ID_NAME);
                    MDC.remove(TraceMdcUtil.MDC_TRACE_SOURCE_NAME);

                    TraceMdcUtil.TRACE_ID.remove();
                    TraceMdcUtil.TRACE_SOURCE.remove();
                    return Mono.empty();
                }));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
