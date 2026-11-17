package i2f.springboot.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.gateway.impl.ContextMcpToolGatewayManager;
import i2f.ai.std.mcp.impl.ContextAppMcpToolProvider;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.impl.ContextAppToolManager;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.core.SpringContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/11/17 16:33
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.tools.enable:true}")
@Configuration
@AutoConfigureOrder(5000)
@Slf4j
@Data
public class AiToolsAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @ConditionalOnExpression("${i2f.springboot.ai.tools.mcp.app.enable:true}")
    @ConditionalOnMissingBean(ContextAppMcpToolProvider.class)
    @Bean
    public ContextAppMcpToolProvider appMcpToolProvider() {
        ContextAppMcpToolProvider ret = new ContextAppMcpToolProvider().toMutator()
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .done();
        return ret;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.tools.mcp.manager.enable:true}")
    @ConditionalOnMissingBean(ToolManager.class)
    @Bean
    public ToolManager mcpToolGatewayManager() {
        ContextMcpToolGatewayManager ret = new ContextMcpToolGatewayManager(new SpringContext(applicationContext));
        return ret;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.tools.app.manager.enable:true}")
    @ConditionalOnMissingBean(ToolManager.class)
    @Bean
    public ToolManager appToolManager() {
        ContextAppToolManager ret = new ContextAppToolManager().toMutator()
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .done();
        return ret;
    }

}
