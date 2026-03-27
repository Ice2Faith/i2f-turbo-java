package i2f.extension.ai.dashscope.tool;

import com.alibaba.dashscope.tools.FunctionDefinition;
import com.alibaba.dashscope.tools.ToolBase;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.JsonObject;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.context.std.IContext;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class DashScopeToolHelper {

    public static Map<String, DashScopeToolDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(context);
        Map<String, DashScopeToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, DashScopeToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, DashScopeToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }

    public static Map<String, DashScopeToolDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> rawMap = ToolRawHelper.parseTools(beans);
        Map<String, DashScopeToolDefinition> ret = new LinkedHashMap<>();
        for (Map.Entry<String, ToolRawDefinition> entry : rawMap.entrySet()) {
            ret.put(entry.getKey(), fromRaw(entry.getValue()));
        }
        return ret;
    }


    public static List<ToolBase> convertTools(Map<String, DashScopeToolDefinition> list) {
        List<ToolBase> ret = new ArrayList<>();
        for (Map.Entry<String, DashScopeToolDefinition> entry : list.entrySet()) {
            DashScopeToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ToolBase> convertTools(Collection<DashScopeToolDefinition> list) {
        List<ToolBase> ret = new ArrayList<>();
        for (DashScopeToolDefinition definition : list) {
            ret.add(definition.getFunction());
        }
        return ret;
    }


    public static DashScopeToolDefinition fromRaw(ToolRawDefinition definition) {
        DashScopeToolDefinition ret = new DashScopeToolDefinition();

        Map<String, Object> functionSchema = definition.getFunctionJsonSchema();

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get("parameters");
        String json = JsonUtils.toJson(parametersSchema);
        JsonObject jsonObject = JsonUtils.parseString(json).getAsJsonObject();
        ToolFunction function = ToolFunction.builder().function(
                FunctionDefinition.builder()
                        .name(definition.getFunctionName())
                        .description(definition.getFunctionDescription())
                        .parameters(jsonObject)
                        .build()
        ).build();

        ret.setFunction(function);
        ret.setRawDefinition(definition);

        return ret;
    }

}
