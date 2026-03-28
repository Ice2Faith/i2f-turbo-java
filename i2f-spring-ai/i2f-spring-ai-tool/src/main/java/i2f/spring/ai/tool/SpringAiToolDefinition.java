package i2f.spring.ai.tool;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.ToolCallback;

import java.util.Collection;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringAiToolDefinition {
    protected ToolCallback function;
    protected ToolRawDefinition rawDefinition;

    public static Map<String, SpringAiToolDefinition> parseTools(IContext context) {
        return SpringAiToolHelper.parseTools(context);
    }

    public static Map<String, SpringAiToolDefinition> parseTools(Collection<Object> beans) {
        return SpringAiToolHelper.parseTools(beans);
    }

    public static Map<String, SpringAiToolDefinition> parseTools(Object... beans) {
        return SpringAiToolHelper.parseTools(beans);
    }
}