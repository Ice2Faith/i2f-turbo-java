package i2f.ai.std.mcp;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolBaseDefinition;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:22
 * @desc
 */
public interface McpToolProvider {
    String getName();

    String getDescription();

    List<ToolBaseDefinition> getTools();

    boolean support(ToolBaseCallRequest request);

    Object callTool(ToolBaseCallRequest request) throws Throwable;
}
