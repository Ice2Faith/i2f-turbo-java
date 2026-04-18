package i2f.ai.std.mcp.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolBaseDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Map.Entry<ToolBaseDefinition, Map<String, Object>> matchDefinition(ToolBaseCallRequest request) {
        for (McpToolProvider provider : providers) {
            Map.Entry<ToolBaseDefinition, Map<String, Object>> entry = provider.matchDefinition(request);
            if (entry != null && entry.getKey() != null) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public Object callTool(ToolBaseDefinition definition, Map<String, Object> parameterMap, ToolBaseCallRequest request) throws Throwable {
        return callTool(request);
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        for (McpToolProvider provider : providers) {
            Map.Entry<ToolBaseDefinition, Map<String, Object>> entry = provider.matchDefinition(request);
            if (entry != null && entry.getKey() != null) {
                return provider.callTool(entry.getKey(), entry.getValue(), request);
            }
        }
        throw new IllegalStateException("call tool [" + request.getName() + "] is not support!");
    }
}
