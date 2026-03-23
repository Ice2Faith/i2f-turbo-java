package i2f.extension.ai.langchain4j8.tool;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.context.std.IContext;
import dev.langchain4j.agent.tool.ToolParameters;
import dev.langchain4j.agent.tool.ToolSpecification;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class Langchain4j8ToolHelper {

    public static Map<String, Langchain4j8ToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(context);
        Map<String, Langchain4j8ToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4j8ToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, Langchain4j8ToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, Langchain4j8ToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, Langchain4j8ToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }


    public static List<ToolSpecification> convertTools(Map<String, Langchain4j8ToolDefinition> list) {
        List<ToolSpecification> ret = new ArrayList<>();
        for (Map.Entry<String, Langchain4j8ToolDefinition> entry : list.entrySet()) {
            Langchain4j8ToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ToolSpecification> convertTools(Collection<Langchain4j8ToolDefinition> list) {
        List<ToolSpecification> ret = new ArrayList<>();
        for (Langchain4j8ToolDefinition definition : list) {
            ret.add(definition.getFunction());
        }
        return ret;
    }


    public static Langchain4j8ToolDefinition fromRaw(ToolRawDefinition definition) {
        Langchain4j8ToolDefinition ret = new Langchain4j8ToolDefinition();

        Map<String, Object> functionSchema = definition.getFunctionJsonSchema();

        Map<String, Map<String, Object>> parametersSchema = (Map<String, Map<String, Object>>) functionSchema.get("parameters");

        ToolSpecification function = ToolSpecification.builder()
                .name(definition.getFunctionName())
                .description(definition.getFunctionDescription())
                .parameters(ToolParameters.builder()
                        .properties(parametersSchema)
                        .build())
                .build();

        ret.setFunction(function);
        ret.setFunctionName(definition.getFunctionName());
        ret.setFunctionParameterNames(definition.getFunctionParameterNames());
        ret.setBindMethod(definition.getBindMethod());
        ret.setBindClass(definition.getBindClass());
        ret.setBindTarget(definition.getBindTarget());

        return ret;
    }

}
