package i2f.ai.std.mcp.server;

import i2f.ai.std.tool.definition.ToolDefinition;

/**
 * @author Ice2Faith
 * @date 2026/9/28 19:47
 * @desc
 */
public interface McpServerExposer {
    boolean expose(ToolDefinition definition);
}
