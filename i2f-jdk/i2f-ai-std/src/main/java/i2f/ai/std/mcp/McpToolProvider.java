package i2f.ai.std.mcp;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolBaseDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:22
 * @desc
 */
public interface McpToolProvider {
    List<ToolBaseDefinition> getTools();

    Map.Entry<ToolBaseDefinition, Map<String, Object>> matchDefinition(ToolBaseCallRequest request);

    Object callTool(ToolBaseDefinition definition, Map<String, Object> parameterMap, ToolBaseCallRequest request) throws Throwable;

    default Object callTool(ToolBaseCallRequest request) throws Throwable {
        Map.Entry<ToolBaseDefinition, Map<String, Object>> entry = matchDefinition(request);
        if (entry == null || entry.getKey() == null) {
            throw new IllegalArgumentException("provider not found tool [" + request.getName() + "]");
        }
        return callTool(entry.getKey(), entry.getValue(), request);
    }
}
