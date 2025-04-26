package i2f.spring.ai.chat.configuration;

import i2f.spring.ai.chat.controller.ChatAiController;
import i2f.spring.ai.chat.controller.ChatAiHistoryController;
import i2f.spring.ai.chat.properties.ChatAiProperties;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:19
 * @desc
 */
@ConditionalOnExpression("${i2f.ai.chat.enable:true}")
@Data
@NoArgsConstructor
@Configuration
@EnableConfigurationProperties({
        ChatAiProperties.class
})
@Import({
        ChatAiMissingBeanAutoConfiguration.class,
        ChatAiController.class,
        ChatAiHistoryController.class,
        ChatAiSessionRepository.class
})
public class ChatAiAutoConfiguration {
    @Autowired
    private ChatAiProperties chatProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @ConditionalOnExpression("${i2f.ai.chat.chat-client.enable:true}")
    @ConditionalOnMissingBean(ChatClient.class)
    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 ChatMemory chatMemory) {
        String system = null;
        String defaultSystemResource = chatProperties.getDefaultSystemResource();
        if (defaultSystemResource != null && !defaultSystemResource.isEmpty()) {
            try {
                Resource resource = resourceLoader.getResource(defaultSystemResource);
                system = resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (Exception e) {

            }
        }
        if (system == null || system.isEmpty()) {
            system = chatProperties.getDefaultSystem();
        }

        ChatClient.Builder builder = ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                );

        if (system != null && !system.isEmpty()) {
            builder.defaultSystem(system);
        }

        return builder.build();

    }
}
