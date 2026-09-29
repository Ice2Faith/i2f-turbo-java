package i2f.ai.std.mcp.server.impl;

import i2f.ai.std.mcp.server.McpServerExposer;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.definition.ToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/9/28 19:47
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicMcpServerProvider implements McpServerProvider {

    protected ToolManager toolManager;
    protected McpServerExposer exposer;

    @Override
    public List<ToolDefinition> getTools() {
        List<ToolDefinition> tools = toolManager.listTools();
        if (exposer == null) {
            return tools;
        }
        return tools.stream().filter(e -> exposer.expose(e)).collect(Collectors.toList());
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        return toolManager.callTool(request);
    }

}
