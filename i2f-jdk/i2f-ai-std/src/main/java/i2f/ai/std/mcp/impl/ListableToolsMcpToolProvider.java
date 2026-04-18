package i2f.ai.std.mcp.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolBaseDefinition;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:26
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableToolsMcpToolProvider implements McpToolProvider {
    protected final CopyOnWriteArrayList<ToolRawDefinition> tools = new CopyOnWriteArrayList<>();
    protected IProxyInvocationHandler invocationHandler;
    protected volatile IJsonSerializer jsonSerializer;

    @Override
    public List<ToolBaseDefinition> getTools() {
        return new ArrayList<>(tools);
    }

    @Override
    public Map.Entry<ToolBaseDefinition, Map<String, Object>> matchDefinition(ToolBaseCallRequest request) {
        Map<String, Object> map = jsonSerializer.deserializeAsMap(request.getArguments());
        String name = request.getName();
        ToolBaseDefinition firstDefinition = null;
        ToolBaseDefinition definition = null;
        for (ToolBaseDefinition tool : tools) {
            String toolName = tool.getName();
            if (Objects.equals(toolName, name)) {
                if (firstDefinition == null) {
                    firstDefinition = definition;
                }
                List<String> parameterNames = tool.getParameterNames();
                boolean isMatch = true;
                for (String parameterName : parameterNames) {
                    boolean ok = map.containsKey(parameterName);
                    if (!ok) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    definition = tool;
                    break;
                }
            }
        }
        if (definition == null) {
            definition = firstDefinition;
        }
        return new AbstractMap.SimpleEntry<>(definition, map);
    }

    @Override
    public Object callTool(ToolBaseDefinition definition, Map<String, Object> parameterMap, ToolBaseCallRequest request) throws Throwable {
        if (!(definition instanceof ToolRawDefinition)) {
            throw new IllegalStateException("un-support tool definition type: " + definition.getClass());
        }
        ToolRawDefinition rawDefinition = (ToolRawDefinition) definition;
        return ToolRawHelper.invokeTool(rawDefinition, parameterMap, invocationHandler);
    }
}
