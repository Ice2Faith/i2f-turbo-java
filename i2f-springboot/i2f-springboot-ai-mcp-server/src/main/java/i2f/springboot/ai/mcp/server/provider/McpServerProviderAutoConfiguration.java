package i2f.springboot.ai.mcp.server.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.server.McpServerExposer;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.ai.std.mcp.server.impl.BasicMcpServerExposer;
import i2f.ai.std.mcp.server.impl.BasicMcpServerProvider;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.impl.ContextAppToolManager;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.mcp.server.provider.properties.McpServerProviderProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/9/28 20:10
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.provider.enable:true}")
@Configuration
@AutoConfigureOrder(6000)
@EnableConfigurationProperties(McpServerProviderProperties.class)
@Slf4j
@Data
public class McpServerProviderAutoConfiguration {

    @Autowired
    private McpServerProviderProperties mcpServerProviderProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.provider.manager.enable:true}")
    @ConditionalOnMissingBean(ToolManager.class)
    @Bean
    public ToolManager toolManager() {
        ContextAppToolManager ret = new ContextAppToolManager().toMutator()
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .done();
        return ret;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.provider.exposer.enable:true}")
    @ConditionalOnMissingBean(McpServerExposer.class)
    @Bean
    public McpServerExposer mcpServerExposer() {
        BasicMcpServerExposer ret = new BasicMcpServerExposer();
        ret.setDefaultExpose(mcpServerProviderProperties.isDefaultExpose());
        ret.setRules(mcpServerProviderProperties.getRules());
        return ret;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.provider.provider.enable:true}")
    @ConditionalOnMissingBean(McpServerProvider.class)
    @Bean
    public McpServerProvider mcpServerProvider(@Autowired ToolManager toolManager,
                                               @Autowired(required = false) McpServerExposer mcpServerExposer) {
        BasicMcpServerProvider ret = new BasicMcpServerProvider();
        ret.setToolManager(toolManager);
        ret.setExposer(mcpServerExposer);
        return ret;
    }
}
