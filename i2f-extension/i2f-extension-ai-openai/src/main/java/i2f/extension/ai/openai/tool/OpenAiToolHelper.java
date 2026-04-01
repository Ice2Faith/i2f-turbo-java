package i2f.extension.ai.openai.tool;


import com.openai.core.JsonValue;
import com.openai.models.FunctionDefinition;
import com.openai.models.chat.completions.ChatCompletionFunctionTool;
import com.openai.models.chat.completions.ChatCompletionTool;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 19:43
 * @desc
 */
public class OpenAiToolHelper {


    public static Map<String, OpenAiToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(JsonSchemaAnnotationResolver.INSTANCE, context);
        Map<String, OpenAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, OpenAiToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(JsonSchemaAnnotationResolver.INSTANCE, beans);
        Map<String, OpenAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, OpenAiToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(JsonSchemaAnnotationResolver.INSTANCE, beans);
        Map<String, OpenAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }


    public static List<ChatCompletionTool> convertTools(Map<String, OpenAiToolDefinition> list) {
        List<ChatCompletionTool> ret = new ArrayList<>();
        for (Map.Entry<String, OpenAiToolDefinition> entry : list.entrySet()) {
            OpenAiToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ChatCompletionTool> convertTools(Collection<OpenAiToolDefinition> list) {
        List<ChatCompletionTool> ret = new ArrayList<>();
        for (OpenAiToolDefinition definition : list) {
            ret.add(definition.getFunction());
        }
        return ret;
    }


    public static OpenAiToolDefinition fromRaw(ToolRawDefinition definition) {
        OpenAiToolDefinition ret = new OpenAiToolDefinition();

        Map<String, Object> functionSchema = definition.getJsonSchema();

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get(JsonSchema.SchemaField.PARAMETERS);

        ChatCompletionTool tool = ChatCompletionTool.ofFunction(ChatCompletionFunctionTool.builder()
                .function(FunctionDefinition.builder()
                        .name(definition.getName())
                        .description(definition.getDescription())
                        .parameters(JsonValue.from(parametersSchema)) // 将 Map 转为 JsonValue
                        .build()
                )
                .build());

        ret.setFunction(tool);
        ret.setRawDefinition(definition);

        return ret;
    }

}
