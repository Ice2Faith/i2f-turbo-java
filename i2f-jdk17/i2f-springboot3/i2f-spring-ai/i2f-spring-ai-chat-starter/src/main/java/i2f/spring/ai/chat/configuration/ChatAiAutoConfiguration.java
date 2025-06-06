package i2f.spring.ai.chat.configuration;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.auth.impl.CookieChatAiAuthProvider;
import i2f.spring.ai.chat.properties.ChatAiProperties;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import i2f.spring.ai.chat.session.impl.InMemoryChatAiSessionRepository;
import i2f.spring.ai.chat.tools.DatabaseMetadataTools;
import i2f.spring.ai.chat.tools.DateTools;
import i2f.spring.ai.chat.tools.MathTools;
import i2f.spring.ai.tool.providers.SpringBeanMethodToolCallbackProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:19
 * @desc
 */
@ConditionalOnExpression("${i2f.ai.chat.enable:true}")
@Data
@NoArgsConstructor
@Lazy
@Configuration
@EnableConfigurationProperties({
        ChatAiProperties.class
})
public class ChatAiAutoConfiguration {
    @Autowired
    private ChatAiProperties chatProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext applicationContext;

    // MCP-Client 提供的自动配置的工具
    @Autowired(required = false)
    private SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;

    @ConditionalOnExpression("${i2f.ai.chat.tools.database-metadata.enable:false}")
    @ConditionalOnMissingBean(DatabaseMetadataTools.class)
    @Bean
    public DatabaseMetadataTools databaseMetadataTools(DataSource dataSource) {
        return new DatabaseMetadataTools(dataSource);
    }

    @ConditionalOnExpression("${i2f.ai.chat.tool-callback.spring-bean-provider.enable:true}")
    @ConditionalOnMissingBean(SpringBeanMethodToolCallbackProvider.class)
    @Bean
    public SpringBeanMethodToolCallbackProvider springBeanMethodToolCallbackProvider() {
        return new SpringBeanMethodToolCallbackProvider(applicationContext);
    }

    @ConditionalOnExpression("${i2f.ai.chat.chat-memory.impl.in-memory.enable:true}")
    @ConditionalOnMissingBean(ChatMemoryRepository.class)
    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @ConditionalOnExpression("${i2f.ai.chat.chat-memory.enable:true}")
    @ConditionalOnMissingBean(ChatMemory.class)
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(chatProperties.getMemoryMaxCount())
                .build();
    }

    @ConditionalOnExpression("${i2f.ai.chat.session-repository.enable:true}")
    @ConditionalOnMissingBean(ChatAiSessionRepository.class)
    @Bean
    public ChatAiSessionRepository chatAiSessionRepository() {
        return new InMemoryChatAiSessionRepository();
    }

    @ConditionalOnExpression("${i2f.ai.chat.auth-provider.enable:true}")
    @ConditionalOnMissingBean(ChatAiAuthProvider.class)
    @Bean
    public ChatAiAuthProvider chatAiAuthProvider() {
        return new CookieChatAiAuthProvider();
    }

    @ConditionalOnExpression("${i2f.ai.chat.chat-client.enable:true}")
    @ConditionalOnMissingBean(ChatClient.class)
    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 ChatMemory chatMemory,
                                 @Autowired(required = false) SpringBeanMethodToolCallbackProvider springBeanMethodToolCallbackProvider) {
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
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                );

        if (chatProperties.isSupportTools()) {
            List<Object> toolsList = new ArrayList<>();
            toolsList.add(new DateTools());
            toolsList.add(new MathTools());

            List<String> toolClasses = chatProperties.getToolClasses();
            if (toolClasses != null) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                for (String item : toolClasses) {
                    try {
                        Class<?> clazz = loader.loadClass(item);
                        if (clazz != null) {
                            Object obj = clazz.newInstance();
                            if (obj != null) {
                                toolsList.add(obj);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            builder.defaultTools(toolsList.toArray());
        }
        if (springBeanMethodToolCallbackProvider != null) {
            builder.defaultToolCallbacks(springBeanMethodToolCallbackProvider);
        }
        if (syncMcpToolCallbackProvider != null) {
            builder.defaultToolCallbacks(syncMcpToolCallbackProvider);
        }

        if (system != null && !system.isEmpty()) {
            builder.defaultSystem(system);
        }

        return builder.build();

    }
}
