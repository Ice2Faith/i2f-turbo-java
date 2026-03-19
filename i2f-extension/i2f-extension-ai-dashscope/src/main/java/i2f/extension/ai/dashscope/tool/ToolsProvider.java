package i2f.extension.ai.dashscope.tool;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/19 14:07
 * @desc
 */
@FunctionalInterface
public interface ToolsProvider {
    List<ToolDefinition> getTools();
}
