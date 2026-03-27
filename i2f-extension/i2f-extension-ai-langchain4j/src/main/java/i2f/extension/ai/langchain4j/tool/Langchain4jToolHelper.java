package i2f.extension.ai.langchain4j.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.*;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.context.std.IContext;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class Langchain4jToolHelper {

    public static Map<String, Langchain4jToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(context);
        Map<String, Langchain4jToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, Langchain4jToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, Langchain4jToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }


    public static List<ToolSpecification> convertTools(Map<String, Langchain4jToolDefinition> list) {
        List<ToolSpecification> ret = new ArrayList<>();
        for (Map.Entry<String, Langchain4jToolDefinition> entry : list.entrySet()) {
            Langchain4jToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ToolSpecification> convertTools(Collection<Langchain4jToolDefinition> list) {
        List<ToolSpecification> ret = new ArrayList<>();
        for (Langchain4jToolDefinition definition : list) {
            ret.add(definition.getFunction());
        }
        return ret;
    }


    public static Langchain4jToolDefinition fromRaw(ToolRawDefinition definition) {
        Langchain4jToolDefinition ret = new Langchain4jToolDefinition();

        Map<String, Object> functionSchema = definition.getFunctionJsonSchema();

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get("parameters");

        ToolSpecification function = ToolSpecification.builder()
                .name(definition.getFunctionName())
                .description(definition.getFunctionDescription())
                .parameters((JsonObjectSchema) functionParameters2ObjectSchema(parametersSchema))
                .build();

        ret.setFunction(function);
        ret.setRawDefinition(definition);

        return ret;
    }

    public static JsonSchemaElement functionParameters2ObjectSchema(Map<String, Object> parameters) {
        String type = (String) parameters.get("type");
        String description = (String) parameters.get("description");
        if ("object".equals(type)) {
            Map<String, JsonSchemaElement> properties = new HashMap<>();
            Map<String, Object> next = (Map<String, Object>) parameters.get("properties");
            for (Map.Entry<String, Object> entry : next.entrySet()) {
                properties.put(entry.getKey(), functionParameters2ObjectSchema((Map<String, Object>) entry.getValue()));
            }
            return JsonObjectSchema.builder()
                    .description(description)
                    .addProperties(properties)
                    .build();
        } else if ("array".equals(type)) {
            Map<String, Object> next = (Map<String, Object>) parameters.get("items");
            return JsonArraySchema.builder()
                    .description(description)
                    .items(functionParameters2ObjectSchema(next))
                    .build();
        } else if ("string".equals(type)) {
            List<String> next = (List<String>) parameters.get("enum");
            if (next != null) {
                return JsonEnumSchema.builder()
                        .description(description)
                        .enumValues(next)
                        .build();
            }
            return JsonStringSchema.builder()
                    .description(description)
                    .build();
        } else if ("number".equals(type)) {
            return JsonNumberSchema.builder()
                    .description(description)
                    .build();
        } else if ("boolean".equals(type)) {
            return JsonBooleanSchema.builder()
                    .description(description)
                    .build();
        } else if ("integer".equals(type)) {
            return JsonIntegerSchema.builder()
                    .description(description)
                    .build();
        }
        return JsonStringSchema.builder()
                .description(description)
                .build();
    }

}
