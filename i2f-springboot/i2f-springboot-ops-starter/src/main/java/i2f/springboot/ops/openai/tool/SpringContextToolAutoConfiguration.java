package i2f.springboot.ops.openai.tool;

import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.springboot.ai.tools.AiToolsAutoConfiguration;
import i2f.springboot.ops.openai.tool.impl.McpProviderTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/7/5 16:46
 * @desc
 */
@ConditionalOnExpression("${ai.tools.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@AutoConfigureAfter(AiToolsAutoConfiguration.class)
public class SpringContextToolAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @ConditionalOnExpression("${ai.tools.mcp-gateway.enable:true}")
    @Bean
    public McpProviderTools mcpProviderTools(@Autowired AbstractMcpToolGatewayManager manager) {
        return new McpProviderTools(manager);
    }
}
