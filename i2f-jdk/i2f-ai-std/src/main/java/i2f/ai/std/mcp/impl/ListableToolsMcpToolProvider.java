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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:26
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableToolsMcpToolProvider implements McpToolProvider {
    public static final String DEFAULT_NAME = "list";
    public static final String DEFAULT_DESCRIPTION = "default tools set";

    protected final CopyOnWriteArrayList<ToolRawDefinition> tools = new CopyOnWriteArrayList<>();
    protected IProxyInvocationHandler invocationHandler;
    protected volatile IJsonSerializer jsonSerializer;
    protected String name = DEFAULT_NAME;
    protected String description = DEFAULT_DESCRIPTION;

    @Override
    public List<ToolBaseDefinition> getTools() {
        return new ArrayList<>(tools);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ToolRawDefinition getTool(ToolBaseCallRequest request) {
        String requestName = request.getName();
        String prefix = getName();
        if (prefix == null) {
            prefix = "";
        }
        prefix = prefix + ".";
        if (requestName.startsWith(prefix)) {
            requestName = requestName.substring(prefix.length());
        }
        for (ToolRawDefinition tool : tools) {
            String name = tool.getName();
            if (name.equals(requestName)) {
                return tool;
            }
        }
        return null;
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        return getTool(request) != null;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        ToolRawDefinition rawDefinition = getTool(request);
        Map<String, Object> parameterMap = jsonSerializer.deserializeAsMap(request.getArguments());
        return ToolRawHelper.invokeTool(rawDefinition, parameterMap, invocationHandler);
    }
}
