package i2f.springboot.trace.mdc.httpclient;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * @author Ice2Faith
 * @date 2026/4/15 16:04
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.exchange-filter.enable:true}")
@ConditionalOnClass(ExchangeFilterFunction.class)
@Component
public class MdcExchangeFilterFunction implements ExchangeFilterFunction, EnvironmentAware {
    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        String appName = environment.getProperty("spring.application.name", "noappname");

        String traceId = MdcTraces.getOrGenTraceId(() -> MdcHolder.get(MdcTraces.TRACE_ID));

        String traceSource = MdcHolder.get(MdcTraces.TRACE_SOURCE);
        if (StringUtils.isEmpty(traceSource)) {
            traceSource = appName;
        }
        return next.exchange(ClientRequest.from(request)
                .header(MdcTraces.TRACE_ID, traceId)
                .header(MdcTraces.TRACE_SOURCE, traceSource)
                .build());
    }
}
