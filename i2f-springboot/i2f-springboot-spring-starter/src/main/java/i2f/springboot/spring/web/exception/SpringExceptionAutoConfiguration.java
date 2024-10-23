package i2f.springboot.spring.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.spring.web.api.StandardApiExceptionConverter;
import i2f.springboot.spring.web.api.impl.ApiRespExceptionConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/6/12 9:44
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.exception.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.spring.exception")
public class SpringExceptionAutoConfiguration {

    private int httpStatus = 200;

    @ConditionalOnMissingBean(StandardApiExceptionConverter.class)
    @Bean
    public StandardApiExceptionConverter<?> standardApiExceptionConverter() {
        return new ApiRespExceptionConverter();
    }

    @ConditionalOnExpression("${i2f.spring.exception.print-exception.enable:true}")
    @Bean
    public GlobalExceptionPrintHandler globalExceptionPrintHandler(StandardApiExceptionConverter<?> standardApiExceptionConverter) {
        return new GlobalExceptionPrintHandler();
    }

    @ConditionalOnExpression("${i2f.spring.exception.response-exception.enable:true}")
    @Bean
    public GlobalExceptionStandardApiConverterHandler<?> globalExceptionStandardApiConverterHandler(StandardApiExceptionConverter<?> standardApiExceptionConverter,
                                                                                                    ObjectMapper objectMapper) {
        GlobalExceptionStandardApiConverterHandler<?> ret = new GlobalExceptionStandardApiConverterHandler<>(standardApiExceptionConverter, objectMapper);
        ret.setHttpStatus(httpStatus);
        return ret;
    }
}
