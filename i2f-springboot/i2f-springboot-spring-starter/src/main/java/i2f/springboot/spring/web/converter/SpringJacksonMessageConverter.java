package i2f.springboot.spring.web.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author Ice2Faith
 * @date 2023/6/16 22:26
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.jackson.enable:true}")
@Data
public class SpringJacksonMessageConverter {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
