package i2f.ai.std.tool;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/19 14:07
 * @desc
 */
@FunctionalInterface
public interface ToolRawDefinitionsProvider {
    List<ToolRawDefinition> getTools();
}
