package i2f.ai.std.tool.definition;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:47
 * @desc
 */
public interface ToolDefinition {
    String getName();

    String getDescription();

    Map<String, Object> getJsonSchema();
}
