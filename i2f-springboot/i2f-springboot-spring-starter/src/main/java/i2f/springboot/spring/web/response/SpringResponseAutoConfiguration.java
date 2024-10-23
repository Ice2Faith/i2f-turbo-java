package i2f.springboot.spring.web.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.spring.web.api.StandardApiResponseConverter;
import i2f.springboot.spring.web.api.impl.ApiRespResponseConverter;
import i2f.springboot.spring.web.response.impl.ApiRespNotFoundResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:38
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.response.enable:true}")
@Slf4j
@ConfigurationProperties(prefix = "i2f.spring.response")
public class SpringResponseAutoConfiguration {

    @ConditionalOnMissingBean(StandardApiResponseConverter.class)
    @Bean
    public StandardApiResponseConverter<?> standardApiResponseConverter() {
        return new ApiRespResponseConverter();
    }

    @ConditionalOnMissingBean(StandardApiNotFoundResponseConvertor.class)
    @Bean
    public StandardApiNotFoundResponseConvertor<?> standardApiNotFoundResponseConvertor() {
        return new ApiRespNotFoundResponseConverter();
    }

    @Bean
    public GlobalResponseStandardApiControllerAdvice globalResponseStandardApiControllerAdvice(StandardApiResponseConverter<?> standardApiResponseConverter,
                                                                                               StandardApiNotFoundResponseConvertor<?> standardApiNotFoundResponseConvertor,
                                                                                               ObjectMapper objectMapper) {
        return new GlobalResponseStandardApiControllerAdvice(standardApiResponseConverter,
                standardApiNotFoundResponseConvertor,
                objectMapper);
    }
}
