package i2f.springboot.trace.mdc.httpclient;

import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2026/4/15 15:57
 * @desc
 */
@Data
@ConditionalOnExpression("${i2f.springboot.trace.mdc.rest-client.enable:true}")
@AutoConfigureAfter(MdcHttpClientInterceptor.class)
@ConditionalOnClass(RestClientCustomizer.class)
public class MdcRestClientConfiguration {
    @Bean
    public RestClientCustomizer restClientCustomizer(MdcHttpClientInterceptor mdcHttpClientInterceptor) {
        return restClient -> restClient.requestInterceptor(mdcHttpClientInterceptor);
    }
}
