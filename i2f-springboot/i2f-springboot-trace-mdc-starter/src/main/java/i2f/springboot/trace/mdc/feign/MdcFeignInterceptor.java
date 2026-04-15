package i2f.springboot.trace.mdc.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * OpenFeign 客户端的 MDC 请求头传递
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.feign.enable:true}")
@ConditionalOnClass(RequestInterceptor.class)
@Component
public class MdcFeignInterceptor implements RequestInterceptor, EnvironmentAware {
    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String appName = environment.getProperty("spring.application.name", "noappname");

        String traceId = MdcTraces.getOrGenTraceId(() -> MdcHolder.get(MdcTraces.TRACE_ID));

        String traceSource = MdcHolder.get(MdcTraces.TRACE_SOURCE);
        if (StringUtils.isEmpty(traceSource)) {
            traceSource = appName;
        }

        MdcHolder.put(MdcTraces.TRACE_ID, traceId);
        MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);
        MdcHolder.put(MdcTraces.TRACE_APP, appName);

        requestTemplate.header(MdcTraces.TRACE_ID, traceId);
        requestTemplate.header(MdcTraces.TRACE_SOURCE, traceSource);
    }
}
