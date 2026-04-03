package i2f.ai.std.tool;

import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;
import i2f.convert.obj.ObjectConvertor;
import i2f.invokable.Invocation;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;
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

    public static <R> List<R> convertTools(Map<String, ToolRawDefinition> list, Function<ToolRawDefinition, R> convertor) {
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

    public static <R> List<R> convertTools(Collection<ToolRawDefinition> list, Function<ToolRawDefinition, R> convertor) {
        List<R> ret = new ArrayList<>();
        for (ToolRawDefinition definition : list) {
            R item = convertor.apply(definition);
            if (item != null) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(JsonSchemaAnnotationResolver resolver, IContext context) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        List<Object> list = context.getAllBeans();
        for (Object item : list) {
            if (item instanceof ToolRawDefinitionsProvider) {
                ToolRawDefinitionsProvider provider = (ToolRawDefinitionsProvider) item;
                List<ToolRawDefinition> tools = provider.getTools();
                for (ToolRawDefinition definition : tools) {
                    ret.put(definition.getName(), definition);
                }
            } else if (resolver.isToolsObject(item)) {
                Map<String, ToolRawDefinition> map = parseTools(resolver, item);
                ret.putAll(map);
            }
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(JsonSchemaAnnotationResolver resolver, Collection<Object> beans) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolRawDefinition> next = parseTools(resolver, bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(JsonSchemaAnnotationResolver resolver, Object... beans) {
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        for (Object bean : beans) {
            Map<String, ToolRawDefinition> next = parseTools(resolver, bean);
            ret.putAll(next);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(JsonSchemaAnnotationResolver resolver, Object bean) {
        Class<?> clazz = bean.getClass();
        Map<String, ToolRawDefinition> ret = parseTools(resolver, clazz);
        for (Map.Entry<String, ToolRawDefinition> entry : ret.entrySet()) {
            ToolRawDefinition definition = entry.getValue();
            definition.setBindTarget(bean);
        }
        return ret;
    }

    public static Map<String, ToolRawDefinition> parseTools(JsonSchemaAnnotationResolver resolver, Class<?> clazz) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        Map<String, ToolRawDefinition> ret = new LinkedHashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!resolver.isToolMethod(method)) {
                continue;
            }
            ToolRawDefinition definition = getToolDefinition(resolver, method);
            if (definition == null) {
                continue;
            }
            definition.setBindClass(clazz);
            ret.put(definition.getName(), definition);
        }
        return ret;
    }

    public static ToolRawDefinition getToolDefinition(JsonSchemaAnnotationResolver resolver, Method method) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return null;
        }
        List<String> parameterNames = new ArrayList<>();
        Map<String, Object> functionSchema = JsonSchema.getFunctionJsonSchema(resolver, method, parameterNames);

        String name = (String) functionSchema.get(JsonSchema.SchemaField.NAME);
        String description = (String) functionSchema.get(JsonSchema.SchemaField.DESCRIPTION);
        Map<String, Object> parametersSchema = (Map<String, Object>) functionSchema.get(JsonSchema.SchemaField.PARAMETERS);

        ToolRawDefinition definition = new ToolRawDefinition();
        definition.setJsonSchema(functionSchema);
        definition.setName(name);
        definition.setDescription(description);
        definition.setParametersJsonSchema(parametersSchema);
        definition.setParameterNames(parameterNames);
        definition.setBindMethod(method);
        definition.setBindClass(method.getDeclaringClass());
        definition.setBindTarget(null);
        definition.setTags(new HashSet<>(resolver.getToolMethodTags(method)));

        return definition;
    }

    public static Object invokeTool(ToolRawDefinition rawDefinition, Map<String, Object> argumentsMap) throws Throwable {
        return invokeTool(rawDefinition, argumentsMap, null);
    }

    public static Object invokeTool(ToolRawDefinition rawDefinition, Map<String, Object> argumentsMap, IProxyInvocationHandler handler) throws Throwable {
        try {
            Method bindMethod = rawDefinition.getBindMethod();
            Object[] args = new Object[rawDefinition.getParameterNames().size()];
            for (int i = 0; i < args.length; i++) {
                Parameter[] parameters = bindMethod.getParameters();
                Object value = argumentsMap.get(rawDefinition.getParameterNames().get(i));
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
            if (handler != null) {
                Invocation invocation = new Invocation();
                invocation.setTarget(target);
                invocation.setInvokable(new JdkMethod(bindMethod));
                invocation.setArgs(args);
                Object ret = handler.invoke(invocation);
                return ret;
            } else {
                Object ret = bindMethod.invoke(target, args);
                return ret;
            }
        } catch (InvocationTargetException e) {
            Throwable ex = e.getTargetException();
            if (ex != null) {
                throw ex;
            }
            throw e;
        }
    }

}
