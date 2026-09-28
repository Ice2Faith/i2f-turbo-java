package i2f.ai.std.mcp.server;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/28 19:46
 * @desc
 */
public interface McpServerProvider {
    List<ToolDefinition> getTools();

    Object callTool(ToolBaseCallRequest request) throws Throwable;
}
