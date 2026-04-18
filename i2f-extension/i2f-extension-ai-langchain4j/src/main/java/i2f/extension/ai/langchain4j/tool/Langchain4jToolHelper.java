package i2f.extension.ai.langchain4j.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.*;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.context.std.IContext;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class Langchain4jToolHelper {

    public static Map<String, Langchain4jToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(Langchain4jJsonSchemaAnnotationResolver.INSTANCE, context);
        Map<String, Langchain4jToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(Langchain4jJsonSchemaAnnotationResolver.INSTANCE, beans);
        Map<String, Langchain4jToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4jToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(Langchain4jJsonSchemaAnnotationResolver.INSTANCE, beans);
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

        Map<String, Object> functionSchema = definition.getJsonSchema();

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get(JsonSchema.SchemaField.PARAMETERS);

        ToolSpecification function = ToolSpecification.builder()
                .name(definition.getName())
                .description(definition.getDescription())
                .parameters((JsonObjectSchema) functionParameters2ObjectSchema(parametersSchema))
                .build();

        ret.setFunction(function);
        ret.setRawDefinition(definition);

        return ret;
    }

    public static JsonSchemaElement functionParameters2ObjectSchema(Map<String, Object> parameters) {
        String type = (String) parameters.get(JsonSchema.SchemaField.TYPE);
        String description = (String) parameters.get(JsonSchema.SchemaField.DESCRIPTION);
        if (JsonSchema.SchemaType.OBJECT.equals(type)) {
            Map<String, JsonSchemaElement> properties = new HashMap<>();
            Map<String, Object> next = (Map<String, Object>) parameters.get(JsonSchema.SchemaField.PROPERTIES);
            for (Map.Entry<String, Object> entry : next.entrySet()) {
                properties.put(entry.getKey(), functionParameters2ObjectSchema((Map<String, Object>) entry.getValue()));
            }
            return JsonObjectSchema.builder()
                    .description(description)
                    .addProperties(properties)
                    .build();
        } else if (JsonSchema.SchemaType.ARRAY.equals(type)) {
            Map<String, Object> next = (Map<String, Object>) parameters.get(JsonSchema.SchemaField.ITEMS);
            return JsonArraySchema.builder()
                    .description(description)
                    .items(functionParameters2ObjectSchema(next))
                    .build();
        } else if (JsonSchema.SchemaType.STRING.equals(type)) {
            List<String> next = (List<String>) parameters.get(JsonSchema.SchemaField.ENUM);
            if (next != null) {
                return JsonEnumSchema.builder()
                        .description(description)
                        .enumValues(next)
                        .build();
            }
            return JsonStringSchema.builder()
                    .description(description)
                    .build();
        } else if (JsonSchema.SchemaType.NUMBER.equals(type)) {
            return JsonNumberSchema.builder()
                    .description(description)
                    .build();
        } else if (JsonSchema.SchemaType.BOOLEAN.equals(type)) {
            return JsonBooleanSchema.builder()
                    .description(description)
                    .build();
        } else if (JsonSchema.SchemaType.INTEGER.equals(type)) {
            return JsonIntegerSchema.builder()
                    .description(description)
                    .build();
        }
        return JsonStringSchema.builder()
                .description(description)
                .build();
    }

}
