package i2f.extension.ai.langchain4j8.tool;

import i2f.context.std.IContext;
import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4j8ToolDefinition {
    protected ToolSpecification function;
    protected String functionName;
    protected List<String> functionParameterNames;
    protected Method bindMethod;
    protected Class<?> bindClass;
    protected Object bindTarget;

    public static Map<String, Langchain4j8ToolDefinition> parseTools(IContext context) {
        return Langchain4j8ToolHelper.parseTools(context);
    }

    public static Map<String, Langchain4j8ToolDefinition> parseTools(Collection<Object> beans) {
        return Langchain4j8ToolHelper.parseTools(beans);
    }

    public static Map<String, Langchain4j8ToolDefinition> parseTools(Object... beans) {
        return Langchain4j8ToolHelper.parseTools(beans);
    }
}