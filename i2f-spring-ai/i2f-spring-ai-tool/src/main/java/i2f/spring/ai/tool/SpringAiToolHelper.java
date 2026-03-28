package i2f.spring.ai.tool;


import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.context.std.IContext;
import i2f.spring.ai.model.SpringAiJsonSerializer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 19:43
 * @desc
 */
public class SpringAiToolHelper {


    public static Map<String, SpringAiToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(SpringAiJsonSchemaAnnotationResolver.INSTANCE, context);
        Map<String, SpringAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, SpringAiToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(SpringAiJsonSchemaAnnotationResolver.INSTANCE, beans);
        Map<String, SpringAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, SpringAiToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(SpringAiJsonSchemaAnnotationResolver.INSTANCE, beans);
        Map<String, SpringAiToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }


    public static List<ToolCallback> convertTools(Map<String, SpringAiToolDefinition> list) {
        List<ToolCallback> ret = new ArrayList<>();
        for (Map.Entry<String, SpringAiToolDefinition> entry : list.entrySet()) {
            SpringAiToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ToolCallback> convertTools(Collection<SpringAiToolDefinition> list) {
        List<ToolCallback> ret = new ArrayList<>();
        for (SpringAiToolDefinition definition : list) {
            ret.add(definition.getFunction());
        }
        return ret;
    }


    public static SpringAiToolDefinition fromRaw(ToolRawDefinition definition) {
        SpringAiToolDefinition ret = new SpringAiToolDefinition();

        Map<String, Object> functionSchema = definition.getFunctionJsonSchema();

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get(JsonSchema.SchemaField.PARAMETERS);


        ToolDefinition def = ToolDefinition.builder()
                .name(definition.getFunctionName())
                .description(definition.getFunctionDescription())
                .inputSchema(SpringAiJsonSerializer.INSTANCE.serialize(parametersSchema))
                .build();

        ToolCallback callback = new SpringAiToolCallback(def, definition);

        ret.setFunction(callback);
        ret.setRawDefinition(definition);

        return ret;
    }

}
