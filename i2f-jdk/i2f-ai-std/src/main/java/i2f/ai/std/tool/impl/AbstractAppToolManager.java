package i2f.ai.std.tool.impl;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 16:13
 * @desc
 */
public abstract class AbstractAppToolManager implements ToolManager {

    public abstract IJsonSerializer getJsonSerializer();

    public abstract IProxyInvocationHandler getInvocationHandler();

    @Override
    public boolean support(ToolBaseCallRequest request) {
        List<ToolDefinition> tools = getTools();
        String name = request.getName();
        for (ToolDefinition tool : tools) {
            if (!(tool instanceof ToolRawDefinition)) {
                continue;
            }
            if (tool.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        List<ToolDefinition> tools = getTools();
        String name = request.getName();
        for (ToolDefinition tool : tools) {
            if (!(tool instanceof ToolRawDefinition)) {
                continue;
            }
            if (tool.getName().equals(name)) {
                ToolRawDefinition rawTool = (ToolRawDefinition) tool;
                IJsonSerializer jsonSerializer = getJsonSerializer();
                IProxyInvocationHandler invocationHandler = getInvocationHandler();
                Map<String, Object> parameterMap = jsonSerializer.deserializeAsMap(request.getArguments());
                return ToolRawHelper.invokeTool(rawTool, parameterMap, invocationHandler);
            }
        }
        throw new IllegalStateException("un-support tool call request!");
    }
}
