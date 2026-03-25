package i2f.ai.std.tool;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.context.std.IContext;
import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.RichConverter;
import i2f.typeof.TypeOf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/3/19 10:56
 * @desc
 */
public class ToolRawHelper {

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

    public static boolean isToolMethod(Method method) {
        if (method == null) {
            return false;
        }
        Tool ann = method.getAnnotation(Tool.class);
        if (ann == null) {
            return false;
        }
        return true;
    }

    public static <T, R> List<R> convertTools(Map<String, ToolRawDefinition> list, Function<ToolRawDefinition, R> convertor) {
        List<R> ret = new ArrayList<>();
        for (Map.Entry<String, ToolRawDefinition> entry : list.entrySet()) {
            ToolRawDefinition definition = entry.getValue();
            R item = convertor.apply(definition);
            if (item != null) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static <T, R> List<R> convertTools(Collection<ToolRawDefinition> list, Function<ToolRawDefinition, R> convertor) {
        List<R> ret = new ArrayList<>();
        for (ToolRawDefinition definition : list) {
            R item = convertor.apply(definition);
            if (item != null) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(IContext context) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        List<Object> list = context.getAllBeans();
        for (Object item : list) {
            if (item instanceof ToolRawDefinitionsProvider) {
                ToolRawDefinitionsProvider provider = (ToolRawDefinitionsProvider) item;
                List<ToolRawDefinition> tools = provider.getTools();
                for (ToolRawDefinition definition : tools) {
                    ret.put(definition.getFunctionName(), definition);
                }
            } else if (isToolsObject(item)) {
                Map<String, ToolRawDefinition> map = parseTools(item);
                ret.putAll(map);
            }
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(Collection<Object> beans) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolRawDefinition> next = parseTools(bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(Object... beans) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolRawDefinition> next = parseTools(bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(Object bean) {
        Class<?> clazz = bean.getClass();
        Map<String, ToolRawDefinition> ret = parseTools(clazz);
        for (Map.Entry<String, ToolRawDefinition> entry : ret.entrySet()) {
            ToolRawDefinition definition = entry.getValue();
            definition.setBindTarget(bean);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(Class<?> clazz) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!isToolMethod(method)) {
                continue;
            }
            ToolRawDefinition definition = getToolDefinition(method);
            if (definition == null) {
                continue;
            }
            definition.setBindClass(clazz);
            ret.put(definition.getFunctionName(), definition);
        }
        return ret;
    }

    public static ToolRawDefinition getToolDefinition(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return null;
        }
        List<String> parameterNames = new ArrayList<>();
        Map<String, Object> functionSchema = JsonSchema.getFunctionJsonSchema(method, parameterNames);

        String name = (String) functionSchema.get("name");
        String description = (String) functionSchema.get("description");
        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get("parameters");

        ToolRawDefinition definition = new ToolRawDefinition();
        definition.setFunctionJsonSchema(functionSchema);
        definition.setFunctionName(name);
        definition.setFunctionDescription(description);
        definition.setFunctionParametersJsonSchema(parametersSchema);
        definition.setFunctionParameterNames(parameterNames);
        definition.setBindMethod(method);
        definition.setBindClass(method.getDeclaringClass());
        definition.setBindTarget(null);
        return definition;
    }

    public static Object invokeTool(ToolRawDefinition rawDefinition, Map<String, Object> argumentsMap) throws Throwable {
        try {
            Method bindMethod = rawDefinition.getBindMethod();
            Object[] args = new Object[rawDefinition.getFunctionParameterNames().size()];
            for (int i = 0; i < args.length; i++) {
                Parameter[] parameters = bindMethod.getParameters();
                Object value = argumentsMap.get(rawDefinition.getFunctionParameterNames().get(i));
                if (i <= parameters.length) {
                    if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                        if (ObjectConvertor.isDateType(parameters[i].getType())) {
                            Date date = ObjectConvertor.tryParseDate(String.valueOf(value));
                            if (date != null) {
                                value = ObjectConvertor.tryConvertAsType(value, parameters[i].getType());
                            }
                        }
                    }
                    if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                        try {
                            // 尝试转换类型为复杂POJO对象
                            Class<?> parameterType = parameters[i].getType();
                            if (!TypeOf.isBaseType(parameterType)) {
                                value = RichConverter.convert(value, parameterType);
                            }
                        } catch (Throwable e) {

                        }
                    }

                    if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                        try {
                            value = ObjectConvertor.tryConvertAsType(value, parameters[i].getType());
                            if (TypeOf.instanceOf(value, parameters[i].getType())) {
                            }
                        } catch (Exception e) {

                        }
                    }

                }
                args[i] = value;
            }

            Object target = rawDefinition.getBindTarget();
            if (Modifier.isStatic(bindMethod.getModifiers())) {
                target = null;
            } else {
                if (target == null) {
                    target = rawDefinition.getBindClass().newInstance();
                }
            }
            Object ret = bindMethod.invoke(target, args);
            return ret;
        } catch (InvocationTargetException e) {
            Throwable ex = e.getTargetException();
            if (ex != null) {
                throw ex;
            }
            throw e;
        }
    }

}
