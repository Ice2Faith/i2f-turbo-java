package i2f.extension.ai.langchain4j.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4jToolDefinition {
    protected ToolSpecification function;
    protected ToolRawDefinition rawDefinition;

    public static Map<String, Langchain4jToolDefinition> parseTools(IContext context) {
        return Langchain4jToolHelper.parseTools(context);
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Collection<Object> beans) {
        return Langchain4jToolHelper.parseTools(beans);
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Object... beans) {
        return Langchain4jToolHelper.parseTools(beans);
    }
}