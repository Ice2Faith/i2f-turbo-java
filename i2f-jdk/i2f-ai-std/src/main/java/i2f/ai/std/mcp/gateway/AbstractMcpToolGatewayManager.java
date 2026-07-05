package i2f.ai.std.mcp.gateway;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.data.McpNameDelegateToolBaseCallRequest;
import i2f.ai.std.mcp.gateway.data.McpNameDelegateToolDefinition;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.definition.ToolDefinition;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:44
 * @desc
 */
public abstract class AbstractMcpToolGatewayManager implements ToolManager {

    public abstract List<McpToolProvider> getMcpProviders();

    public String wrapPrefixName(String prefix, String name) {
        if (prefix == null || prefix.isEmpty()) {
            return name;
        }
        return prefix + "." + name;
    }

    public Map.Entry<String, String> unwrapPrefixName(String name) {
        String prefix = "";
        String realName = name;
        int idx = name.indexOf(".");
        if (idx >= 0) {
            prefix = name.substring(0, idx);
            realName = name.substring(idx + 1);
        }
        return new AbstractMap.SimpleEntry<>(prefix, realName);
    }

    public List<ToolDefinition> getProviderTools(McpToolProvider mcpProvider) {
        List<ToolDefinition> ret = new ArrayList<>();
        String prefix = mcpProvider.getName();
        List<ToolDefinition> tools = mcpProvider.getTools();
        for (ToolDefinition tool : tools) {
            ret.add(new McpNameDelegateToolDefinition(wrapPrefixName(prefix, tool.getName()), tool));
        }
        return ret;
    }

    @Override
    public List<ToolDefinition> getTools() {
        List<ToolDefinition> ret = new ArrayList<>();
        List<McpToolProvider> mcpProviders = getMcpProviders();
        for (McpToolProvider mcpProvider : mcpProviders) {
            ret.addAll(getProviderTools(mcpProvider));
        }
        return ret;
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        String name = request.getName();
        Map.Entry<String, String> entry = unwrapPrefixName(name);
        List<McpToolProvider> mcpProviders = getMcpProviders();
        for (McpToolProvider mcpProvider : mcpProviders) {
            String prefix = mcpProvider.getName();
            if (prefix.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        String name = request.getName();
        Map.Entry<String, String> entry = unwrapPrefixName(name);
        List<McpToolProvider> mcpProviders = getMcpProviders();
        for (McpToolProvider mcpProvider : mcpProviders) {
            String prefix = mcpProvider.getName();
            if (prefix.equals(entry.getKey())) {
                McpNameDelegateToolBaseCallRequest unwrapRequest = new McpNameDelegateToolBaseCallRequest(entry.getValue(), request);
                return mcpProvider.callTool(unwrapRequest);
            }
        }
        throw new IllegalStateException("un-support tool call request!");
    }
}
