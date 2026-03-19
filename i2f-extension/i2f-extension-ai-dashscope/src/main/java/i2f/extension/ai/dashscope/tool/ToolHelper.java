package i2f.extension.ai.dashscope.tool;

import com.alibaba.dashscope.tools.FunctionDefinition;
import com.alibaba.dashscope.tools.ToolBase;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.JsonObject;
import i2f.context.std.IContext;
import i2f.extension.ai.dashscope.schema.JsonSchema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class ToolHelper {

    public static List<Object> filterToolsObject(Collection<Object> list) {
        List<Object> ret = new ArrayList<>();
        for (Object obj : list) {
            if (isToolsObject(obj)) {
                ret.add(obj);
            }
        }
        return ret;
    }

    public static boolean isToolsObject(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return isToolsClass(clazz);
    }

    public static boolean isToolsClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        Tools ann = clazz.getAnnotation(Tools.class);
        if (ann == null) {
            return false;
        }
        return true;
    }

    public static List<ToolBase> convertTools(Map<String, ToolDefinition> list) {
        List<ToolBase> ret = new ArrayList<>();
        for (Map.Entry<String, ToolDefinition> entry : list.entrySet()) {
            ToolDefinition definition = entry.getValue();
            ret.add(definition.getFunction());
        }
        return ret;
    }

    public static List<ToolBase> convertTools(Collection<ToolDefinition> list) {
        List<ToolBase> ret = new ArrayList<>();
        for (ToolDefinition item : list) {
            ret.add(item.getFunction());
        }
        return ret;
    }

    public static Map<String, ToolDefinition> parseTools(IContext context) {
        Map<String, ToolDefinition> ret = new LinkedHashMap<>();
        List<Object> list = context.getAllBeans();
        for (Object item : list) {
            if (item instanceof ToolsProvider) {
                ToolsProvider provider = (ToolsProvider) item;
                List<ToolDefinition> tools = provider.getTools();
                for (ToolDefinition definition : tools) {
                    ret.put(definition.getFunctionName(), definition);
                }
            } else if (isToolsObject(item)) {
                Map<String, ToolDefinition> map = parseTools(item);
                ret.putAll(map);
            }
        }
        return ret;
    }

    public static Map<String, ToolDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolDefinition> next = parseTools(bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolDefinition> parseTools(Object... beans) {
        Map<String, ToolDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolDefinition> next = parseTools(bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolDefinition> parseTools(Object bean) {
        Class<?> clazz = bean.getClass();
        Map<String, ToolDefinition> ret = parseTools(clazz);
        for (Map.Entry<String, ToolDefinition> entry : ret.entrySet()) {
            ToolDefinition definition = entry.getValue();
            definition.setBindTarget(bean);
        }
        return ret;
    }

    public static Map<String, ToolDefinition> parseTools(Class<?> clazz) {
        Map<String, ToolDefinition> ret = new LinkedHashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            ToolDefinition definition = getToolDefinition(method);
            if (definition == null) {
                continue;
            }
            definition.setBindClass(clazz);
            ret.put(definition.getFunctionName(), definition);
        }
        return ret;
    }

    public static ToolDefinition getToolDefinition(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return null;
        }
        List<String> parameterNames = new ArrayList<>();
        Map<String, Object> functionSchema = JsonSchema.getFunctionJsonSchema(method, parameterNames);

        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get("parameters");
        String json = JsonUtils.toJson(parametersSchema);
        JsonObject jsonObject = JsonUtils.parseString(json).getAsJsonObject();
        ToolFunction function = ToolFunction.builder().function(
                FunctionDefinition.builder()
                        .name((String) functionSchema.get("name"))
                        .description((String) functionSchema.get("description"))
                        .parameters(jsonObject)
                        .build()
        ).build();

        ToolDefinition definition = new ToolDefinition();
        definition.setFunctionName(function.getFunction().getName());
        definition.setFunctionParameterNames(parameterNames);
        definition.setFunction(function);
        definition.setBindMethod(method);
        definition.setBindClass(method.getDeclaringClass());
        definition.setBindTarget(null);
        return definition;
    }

}
