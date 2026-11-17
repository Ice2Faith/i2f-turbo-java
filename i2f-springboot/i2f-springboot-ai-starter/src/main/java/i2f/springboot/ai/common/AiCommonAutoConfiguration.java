package i2f.springboot.ai.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/3/30 14:29
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.enable:true}")
@Configuration
@Slf4j
@Data
public class AiCommonAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    @ConditionalOnExpression("${i2f.springboot.ai.json-serializer.enable:true}")
    @ConditionalOnMissingBean(IJsonSerializer.class)
    @Bean
    public IJsonSerializer jsonSerializer(@Autowired ObjectMapper objectMapper) {
        return new JacksonJsonSerializer(objectMapper);
    }

    @ConditionalOnExpression("${i2f.springboot.ai.json-schema-annotation-resolver.enable:true}")
    @ConditionalOnMissingBean(JsonSchemaAnnotationResolver.class)
    @Bean
    public JsonSchemaAnnotationResolver jsonSchemaAnnotationResolver() {
        return JsonSchemaAnnotationResolver.INSTANCE;
    }

}
