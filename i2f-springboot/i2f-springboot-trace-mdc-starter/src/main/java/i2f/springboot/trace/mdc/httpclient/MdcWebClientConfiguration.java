package i2f.springboot.trace.mdc.httpclient;

import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2026/4/15 15:57
 * @desc
 */
@Data
@ConditionalOnExpression("${i2f.springboot.trace.mdc.web-client.enable:true}")
@AutoConfigureAfter(MdcExchangeFilterFunction.class)
@ConditionalOnClass(WebClientCustomizer.class)
public class MdcWebClientConfiguration {

    @Bean
    public WebClientCustomizer restClientCustomizer(MdcExchangeFilterFunction mdcHttpClientInterceptor) {
        return webClient -> webClient.filter(mdcHttpClientInterceptor);
    }
}
