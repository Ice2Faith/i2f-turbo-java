package i2f.springboot.spring.web.converter;

import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author Ice2Faith
 * @date 2023/6/16 22:26
 * @desc
 */
@ConditionalOnClass(JacksonJsonHttpMessageConverter.class)
@ConditionalOnExpression("${i2f.spring.jackson.enable:true}")
@Data
public class SpringJacksonMessageConverter {

    @ConditionalOnClass(ObjectMapper.class)
    @Bean
    public JacksonJsonSerializer jacksonJsonSerializer(ObjectMapper objectMapper) {
        return new JacksonJsonSerializer(objectMapper);
    }

    @Bean
    public JacksonJsonHttpMessageConverter mappingJackson2HttpMessageConverter(JsonMapper jsonMapper) {
        JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter(jsonMapper);
        return converter;
    }
}
