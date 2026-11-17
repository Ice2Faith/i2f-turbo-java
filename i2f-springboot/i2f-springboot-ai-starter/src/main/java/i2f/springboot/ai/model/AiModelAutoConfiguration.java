package i2f.springboot.ai.model;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.openai.client.OpenAIClient;
import i2f.ai.rest.openai.model.HttpOpenAiAiModel;
import i2f.ai.std.model.AiModel;
import i2f.extension.ai.dashscope.model.DashScopeModel;
import i2f.extension.ai.openai.model.OpenAiModel;
import i2f.spring.web.rest.SpringWebRestClient;
import i2f.springboot.ai.model.properties.AiModelDashScopeProperties;
import i2f.springboot.ai.model.properties.AiModelOpenAiProperties;
import i2f.springboot.ai.model.properties.AiModelRestOpenAiProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2026/11/17 16:31
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.model.enable:true}")
@Configuration
@Slf4j
@Data
@EnableConfigurationProperties({
        AiModelDashScopeProperties.class,
        AiModelRestOpenAiProperties.class,
        AiModelOpenAiProperties.class
})
public class AiModelAutoConfiguration {
    @ConditionalOnExpression("${i2f.springboot.ai.model.rest-openai.enable:true}")
    @ConditionalOnMissingBean(AiModel.class)
    @Bean
    public AiModel aiModel(@Autowired AiModelRestOpenAiProperties aiModelRestOpenAiProperties) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(10))
                .build();
        return new HttpOpenAiAiModel().toMutator()
                .set(u -> u::setRestClient, new SpringWebRestClient(restTemplate))
                .set(u -> u::setBaseUrl, aiModelRestOpenAiProperties.getBaseUrl())
                .set(u -> u::setApiKey, aiModelRestOpenAiProperties.getApiKey())
                .set(u -> u::setModel, aiModelRestOpenAiProperties.getModel())
                .done();
    }

    @ConditionalOnExpression("${i2f.springboot.ai.model.dashscope.enable:false}")
    @ConditionalOnClass({
            Generation.class,
            DashScopeModel.class
    })
    @ConditionalOnMissingBean(AiModel.class)
    @Bean
    public AiModel aiModel(@Autowired AiModelDashScopeProperties aiModelDashScopeProperties) {
        return new DashScopeModel()
                .apiKey(aiModelDashScopeProperties.getApiKey())
                .model(aiModelDashScopeProperties.getModel());
    }

    @ConditionalOnExpression("${i2f.springboot.ai.model.openai.enable:false}")
    @ConditionalOnClass({
            OpenAIClient.class,
            OpenAiModel.class
    })
    @ConditionalOnMissingBean(AiModel.class)
    @Bean
    public AiModel aiModel(@Autowired AiModelOpenAiProperties aiModelOpenAiProperties) {
        return OpenAiModel.builder()
                .baseUrl(aiModelOpenAiProperties.getBaseUrl())
                .apiKey(aiModelOpenAiProperties.getApiKey())
                .model(aiModelOpenAiProperties.getModel())
                .build();
    }
}
