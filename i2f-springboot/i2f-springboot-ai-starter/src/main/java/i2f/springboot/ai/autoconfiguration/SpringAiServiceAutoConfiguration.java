package i2f.springboot.ai.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.extension.ai.dashscope.model.DashScopeModel;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.springboot.ai.json.JacksonAiJsonSerializer;
import i2f.springboot.ai.properties.SpringAiModelDashScopeProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({
        SpringAiModelDashScopeProperties.class
})
public class SpringAiServiceAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @ConditionalOnExpression("${i2f.springboot.ai.model.dashscope.enable:true}")
    @ConditionalOnMissingBean(AiModel.class)
    @Bean
    public AiModel aiModel(@Autowired SpringAiModelDashScopeProperties springAiModelDashScopeProperties) {
        return new DashScopeModel()
                .apiKey(springAiModelDashScopeProperties.getApiKey())
                .model(springAiModelDashScopeProperties.getModel());
    }

    @ConditionalOnExpression("${i2f.springboot.ai.json-serializer.enable:true}")
    @ConditionalOnMissingBean(IJsonSerializer.class)
    @Bean
    public IJsonSerializer jsonSerializer(@Autowired ObjectMapper objectMapper) {
        return new JacksonAiJsonSerializer(objectMapper);
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
