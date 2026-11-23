package i2f.springboot.ai.mcp.client.creator.components;

import i2f.ai.std.mcp.McpToolProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:39
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class McpClientCreatorMcpToolProviderFactoryBean implements FactoryBean<McpToolProvider> {
    private McpToolProvider provider;
    private ApplicationContext context;

    @Override
    public McpToolProvider getObject() throws Exception {
        return provider;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return McpToolProvider.class;
    }
}
