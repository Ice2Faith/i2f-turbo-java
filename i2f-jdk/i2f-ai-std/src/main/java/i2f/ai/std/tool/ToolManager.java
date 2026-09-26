package i2f.ai.std.tool;

import i2f.ai.std.tool.definition.ToolDefinition;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:40
 * @desc
 */
public interface ToolManager {
    List<ToolDefinition> getTools();

    boolean support(ToolBaseCallRequest request);

    Object callTool(ToolBaseCallRequest request) throws Throwable;
}
