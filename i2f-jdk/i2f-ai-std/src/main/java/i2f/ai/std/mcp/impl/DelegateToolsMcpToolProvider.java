package i2f.ai.std.mcp.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolBaseDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/4/10 15:38
 * @desc
 */
@Data
@NoArgsConstructor
public class DelegateToolsMcpToolProvider implements McpToolProvider {
    protected final CopyOnWriteArrayList<McpToolProvider> providers = new CopyOnWriteArrayList<>();

    @Override
    public List<ToolBaseDefinition> getTools() {
        List<ToolBaseDefinition> ret = new ArrayList<>();
        for (McpToolProvider provider : providers) {
            ret.addAll(provider.getTools());
        }
        return ret;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        for (McpToolProvider provider : providers) {
            if (provider.support(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        for (McpToolProvider provider : providers) {
            if (provider.support(request)) {
                return provider.callTool(request);
            }
        }
        throw new IllegalStateException("call tool [" + request.getName() + "] is not support!");
    }
}
