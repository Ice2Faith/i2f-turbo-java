package i2f.spring.ai.chat.configuration;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.auth.impl.CookieChatAiAuthProvider;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import i2f.spring.ai.chat.session.impl.InMemoryChatAiSessionRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:50
 * @desc
 */
public class ChatAiMissingBeanAutoConfiguration {

    @ConditionalOnExpression("${i2f.ai.chat.chat-memory.enable:true}")
    @ConditionalOnMissingBean(ChatMemory.class)
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
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
}
