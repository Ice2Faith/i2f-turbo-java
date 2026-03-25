package i2f.extension.ai.openai.tool;

import com.openai.models.chat.completions.ChatCompletionTool;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.context.std.IContext;
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
public class OpenAiToolDefinition {
    protected ChatCompletionTool function;
    protected ToolRawDefinition rawDefinition;

    public static Map<String, OpenAiToolDefinition> parseTools(IContext context) {
        return OpenAiToolHelper.parseTools(context);
    }

    public static Map<String, OpenAiToolDefinition> parseTools(Collection<Object> beans) {
        return OpenAiToolHelper.parseTools(beans);
    }

    public static Map<String, OpenAiToolDefinition> parseTools(Object... beans) {
        return OpenAiToolHelper.parseTools(beans);
    }
}