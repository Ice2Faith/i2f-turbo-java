package i2f.springboot.ops.openai.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.ai.std.mcp.gateway.impl.ContextMcpToolGatewayManager;
import i2f.ai.std.mcp.impl.ContextAppMcpToolProvider;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.impl.ContextAppToolManager;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.core.SpringContext;
import i2f.springboot.ops.openai.tool.impl.McpProviderTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class SpringContextToolAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @ConditionalOnExpression("${ai.tools.mcp.app.enable:true}")
    @ConditionalOnMissingBean(ContextAppMcpToolProvider.class)
    @Bean
    public ContextAppMcpToolProvider appMcpToolProvider() {
        ContextAppMcpToolProvider ret = new ContextAppMcpToolProvider().toMutator()
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .done();
        return ret;
    }

    @ConditionalOnExpression("${ai.tools.mcp.manager.enable:true}")
    @ConditionalOnMissingBean(ToolManager.class)
    @Bean
    public ToolManager mcpToolGatewayManager() {
        ContextMcpToolGatewayManager ret = new ContextMcpToolGatewayManager(new SpringContext(applicationContext));
        return ret;
    }

    @ConditionalOnExpression("${ai.tools.app.manager.enable:true}")
    @ConditionalOnMissingBean(ToolManager.class)
    @Bean
    public ToolManager appToolManager() {
        ContextAppToolManager ret = new ContextAppToolManager().toMutator()
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .done();
        return ret;
    }

    @ConditionalOnExpression("${ai.tools.mcp-gateway.enable:true}")
    @ConditionalOnBean(AbstractMcpToolGatewayManager.class)
    @Bean
    public McpProviderTools mcpProviderTools(@Autowired AbstractMcpToolGatewayManager manager) {
        return new McpProviderTools(manager);
    }
}
