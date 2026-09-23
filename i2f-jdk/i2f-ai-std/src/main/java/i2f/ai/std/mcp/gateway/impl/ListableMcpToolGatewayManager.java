package i2f.ai.std.mcp.gateway.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/7/5 15:45
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableMcpToolGatewayManager extends AbstractMcpToolGatewayManager {
    protected final CopyOnWriteArrayList<McpToolProvider> providers = new CopyOnWriteArrayList<>();

    @Override
    public List<McpToolProvider> getMcpProviders() {
        return new ArrayList<>(providers);
    }
}
