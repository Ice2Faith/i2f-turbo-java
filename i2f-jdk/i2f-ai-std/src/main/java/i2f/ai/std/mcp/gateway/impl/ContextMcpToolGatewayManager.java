package i2f.ai.std.mcp.gateway.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.context.impl.ListableNamingContext;
import i2f.context.std.IContext;
import i2f.context.std.INamingContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/5 15:46
 * @desc
 */
@Data
@NoArgsConstructor
public class ContextMcpToolGatewayManager extends AbstractMcpToolGatewayManager {
    protected IContext context = new ListableNamingContext();

    public ContextMcpToolGatewayManager(INamingContext context) {
        this.context = context;
    }

    @Override
    public List<McpToolProvider> getMcpProviders() {
        List<McpToolProvider> ret = context.getBeans(McpToolProvider.class);
        return ret;
    }
}
