package i2f.springboot.trace.mdc.httpclient;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * HttpClient 客户端的 MDC 请求头传递拦截器
 * 需要在 RestTemplate/RestClient 中使用
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.http-client.enable:true}")
@ConditionalOnClass(ClientHttpRequestInterceptor.class)
@Component
public class MdcHttpClientInterceptor implements ClientHttpRequestInterceptor, EnvironmentAware {

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String appName = environment.getProperty("spring.application.name", "noappname");

        String traceId = MdcTraces.getOrGenTraceId(() -> MdcHolder.get(MdcTraces.TRACE_ID));

        String traceSource = MdcHolder.get(MdcTraces.TRACE_SOURCE);
        if (StringUtils.isEmpty(traceSource)) {
            traceSource = appName;
        }

        MdcHolder.put(MdcTraces.TRACE_ID, traceId);
        MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);
        MdcHolder.put(MdcTraces.TRACE_APP, appName);

        request.getHeaders().set(MdcTraces.TRACE_ID, traceId);
        request.getHeaders().set(MdcTraces.TRACE_SOURCE, traceSource);

        return execution.execute(request, body);
    }
}
