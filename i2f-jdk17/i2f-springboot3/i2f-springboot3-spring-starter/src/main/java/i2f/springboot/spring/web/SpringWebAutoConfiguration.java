package i2f.springboot.spring.web;

import i2f.spring.web.mapping.MappingUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Ice2Faith
 * @date 2024/6/12 9:44
 * @desc
 */
@ConditionalOnClass(RequestMappingHandlerMapping.class)
@ConditionalOnExpression("${i2f.spring.web.enable:true}")
public class SpringWebAutoConfiguration {


    @ConditionalOnClass(RequestMappingHandlerMapping.class)
    @Bean
    public MappingUtil mappingUtil(RequestMappingHandlerMapping handlerMapping) {
        return new MappingUtil(handlerMapping);
    }
}
