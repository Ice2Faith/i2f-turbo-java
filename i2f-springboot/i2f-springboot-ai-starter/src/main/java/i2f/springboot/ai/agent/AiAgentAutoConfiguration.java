package i2f.springboot.ai.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/11/17 17:36
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.agent.enable:true}")
@Configuration
@Slf4j
@Data
public class AiAgentAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @ConditionalOnExpression("${i2f.springboot.ai.json-serializer.enable:true}")
    @ConditionalOnMissingBean(IJsonSerializer.class)
    @Bean
    public IJsonSerializer jsonSerializer(@Autowired ObjectMapper objectMapper) {
        return new JacksonJsonSerializer(objectMapper);
    }

    @ConditionalOnExpression("${i2f.springboot.ai.json-schema-annotation-resolver.enable:true}")
    @ConditionalOnMissingBean(JsonSchemaAnnotationResolver.class)
    public JsonSchemaAnnotationResolver jsonSchemaAnnotationResolver() {
        return JsonSchemaAnnotationResolver.INSTANCE;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.agent.enable:true}")
    @ConditionalOnClass(AiAgent.class)
    @Bean
    public AiAgent aiAgent(@Autowired AiModel aiModel,
                           @Autowired IJsonSerializer jsonSerializer,
                           @Autowired(required = false) RagWorker ragWorker,
                           @Autowired(required = false) JsonSchemaAnnotationResolver jsonSchemaAnnotationResolver) {
        return new AiAgent()
                .model(aiModel)
                .jsonSerializer(jsonSerializer)
                .ragWorker(ragWorker)
                .jsonSchemaAnnotationResolver(jsonSchemaAnnotationResolver);
    }
}