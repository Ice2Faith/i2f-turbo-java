package i2f.springboot.jdbc.bql.procedure.ai.openai;

import com.alibaba.dashscope.aigc.generation.Generation;
import i2f.extension.ai.dashscope.impl.DashScopeAi;
import i2f.extension.ai.openai.impl.OpenAiAi;
import i2f.extension.ai.openai.impl.OpenAiChatAiProvider;
import i2f.extension.ai.openai.impl.OpenAiRoleChatAiProvider;
import i2f.spring.core.SpringContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/3/19 9:35
 * @desc
 */
@ConditionalOnClass(Generation.class)
@ConditionalOnExpression("${openai.ai.enable:true}")
@Data
@NoArgsConstructor
@Configuration
public class OpenAiAiAutoConfiguration {

    @Autowired
    private ApplicationContext context;

    @Value("${openai.ai.api-key:}")
    private String apiKey;

    @Value("${openai.ai.baseUrl:}")
    private String baseUrl;

    @ConditionalOnExpression("${openai.ai.chat-ai.enable:true}")
    @Bean
    public OpenAiChatAiProvider openAiChatAiProvider() {
        OpenAiChatAiProvider ret = new OpenAiChatAiProvider();
        ret.setApiKey(DashScopeAi.getPossibleApiKey(apiKey));
        ret.setBaseUrl(OpenAiAi.getPossibleBaseUrl(baseUrl));
        ret.setContext(new SpringContext(context));
        return ret;
    }

    @ConditionalOnExpression("${openai.ai.role-chat-ai.enable:true}")
    @Bean
    public OpenAiRoleChatAiProvider openAiRoleChatAiProvider() {
        OpenAiRoleChatAiProvider ret = new OpenAiRoleChatAiProvider();
        ret.setApiKey(DashScopeAi.getPossibleApiKey(apiKey));
        ret.setBaseUrl(OpenAiAi.getPossibleBaseUrl(baseUrl));
        ret.setContext(new SpringContext(context));
        return ret;
    }
}
