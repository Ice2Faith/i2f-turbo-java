package i2f.springboot.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.web.mapping.MappingUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Ice2Faith
 * @date 2024/6/12 9:44
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.web.enable:true}")
public class SpringWebAutoConfiguration {

    @ConditionalOnClass(ObjectMapper.class)
    public JacksonJsonSerializer jacksonJsonSerializer(ObjectMapper objectMapper) {
        return new JacksonJsonSerializer(objectMapper);
    }

    @ConditionalOnClass(HandlerMethod.class)
    public MappingUtil mappingUtil(RequestMappingHandlerMapping handlerMapping) {
        return new MappingUtil(handlerMapping);
    }
}
