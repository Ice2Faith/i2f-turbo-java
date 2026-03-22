package i2f.springboot.jdbc.bql.procedure.ai.dashscope;

import com.alibaba.dashscope.aigc.generation.Generation;
import i2f.extension.ai.dashscope.impl.DashScopeAi;
import i2f.extension.ai.dashscope.impl.DashScopeChatAiProvider;
import i2f.extension.ai.dashscope.impl.DashScopeRoleChatAiProvider;
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
@ConditionalOnExpression("${dashscope.ai.enable:true}")
@Data
@NoArgsConstructor
@Configuration
public class DashScopeAiAutoConfiguration {

    @Autowired
    private ApplicationContext context;

    @Value("${dashscope.ai.api-key:}")
    private String apiKey;

    @ConditionalOnExpression("${dashscope.ai.chat-ai.enable:true}")
    @Bean
    public DashScopeChatAiProvider dashScopeChatAiProvider() {
        DashScopeChatAiProvider ret = new DashScopeChatAiProvider();
        ret.setApiKey(DashScopeAi.getPossibleApiKey(apiKey));
        ret.setContext(new SpringContext(context));
        return ret;
    }

    @ConditionalOnExpression("${dashscope.ai.role-chat-ai.enable:true}")
    @Bean
    public DashScopeRoleChatAiProvider dashScopeRoleChatAiProvider() {
        DashScopeRoleChatAiProvider ret = new DashScopeRoleChatAiProvider();
        ret.setApiKey(DashScopeAi.getPossibleApiKey(apiKey));
        ret.setContext(new SpringContext(context));
        return ret;
    }
}
