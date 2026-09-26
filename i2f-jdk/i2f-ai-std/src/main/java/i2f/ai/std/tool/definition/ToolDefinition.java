package i2f.ai.std.tool.definition;

import i2f.ai.std.tool.schema.data.FunctionJsonSchema;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:47
 * @desc
 */
public interface ToolDefinition {
    String getName();

    String getDescription();

    FunctionJsonSchema getJsonSchema();

    Set<String> getTags();
}
