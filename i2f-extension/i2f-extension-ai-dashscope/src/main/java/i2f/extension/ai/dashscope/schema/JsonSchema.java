package i2f.extension.ai.dashscope.schema;

import i2f.extension.ai.dashscope.tool.Tool;
import i2f.extension.ai.dashscope.tool.ToolParam;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/3/19 17:55
 * @desc
 */
public class JsonSchema {
    public static Map<String, Object> getFunctionJsonSchema(Method method) {
        return getFunctionJsonSchema(method, null);
    }

    public static Map<String, Object> getFunctionJsonSchema(Method method, List<String> parameterNames) {
        Map<String, Object> functionSchema = new LinkedHashMap<>();

        String name = method.getName();
        String description = method.getName();

        Tool ann = method.getAnnotation(Tool.class);
        if (ann != null) {
            if (ann.value() != null && !ann.value().isEmpty()) {
                name = ann.value();
            }
            if (ann.description() != null && !ann.description().isEmpty()) {
                description = ann.description();
            }
        }

        Map<String, Object> jsonSchema = new LinkedHashMap<>();
        Parameter[] parameters = method.getParameters();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramName = JsonSchema.addParameterJsonSchema(jsonSchema, parameter);
                if (parameterNames != null) {
                    parameterNames.add(paramName);
                }
            }
        }

        functionSchema.put("name", name);
        functionSchema.put("description", description);
        functionSchema.put("strict", true);
        functionSchema.put("parameters", jsonSchema);

        return functionSchema;
    }

    public static String addParameterJsonSchema(Map<String, Object> jsonSchema, Parameter parameter) {
        String paramName = parameter.getName();
        String paramDesc = paramName;
        ToolParam paramAnn = parameter.getAnnotation(ToolParam.class);
        if (paramAnn != null) {
            if (paramAnn.value() != null && !paramAnn.value().isEmpty()) {
                paramName = paramAnn.value();
            }
            if (paramAnn.description() != null && !paramAnn.description().isEmpty()) {
                paramDesc = paramAnn.description();
            }
        }
        Map<String, Object> pair = getTypeSchemaInfo(parameter);
        Object additionalDesc = pair.get("description");
        pair.put("description", paramDesc + (additionalDesc == null ? "" : ". " + additionalDesc));
        jsonSchema.put(paramName, pair);
        return paramName;
    }

    public static String addFieldJsonSchema(Map<String, Object> jsonSchema, Field field) {
        String paramName = field.getName();
        String paramDesc = paramName;
        ToolParam paramAnn = field.getAnnotation(ToolParam.class);
        if (paramAnn != null) {
            if (paramAnn.value() != null && !paramAnn.value().isEmpty()) {
                paramName = paramAnn.value();
            }
            if (paramAnn.description() != null && !paramAnn.description().isEmpty()) {
                paramDesc = paramAnn.description();
            }
        }
        Map<String, Object> pair = getTypeSchemaInfo(field);

        Object additionalDesc = pair.get("description");
        pair.put("description", paramDesc + (additionalDesc == null ? "" : ". " + additionalDesc));

        jsonSchema.put(paramName, pair);

        return paramName;
    }

    public static Map<String, Object> getTypeJsonSchema(Class<?> type) {
        Map<String, Object> jsonSchema = new LinkedHashMap<>();
        if (TypeOf.isBaseType(type)) {
            jsonSchema.put("type", convertAsJsonSchemaType(type));
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
            jsonSchema.put("type", convertAsJsonSchemaType(type));
            jsonSchema.put("format", "date-time");
            jsonSchema.put("description", "must use format: yyyy-MM-dd HH:mm:ss, such as 2020-01-01 14:01:01");
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, LocalDate.class)) {
            jsonSchema.put("type", convertAsJsonSchemaType(type));
            jsonSchema.put("format", "date");
            jsonSchema.put("description", "must use format: yyyy-MM-dd, such as 2020-01-01");
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, LocalTime.class)) {
            jsonSchema.put("type", convertAsJsonSchemaType(type));
            jsonSchema.put("format", "time");
            jsonSchema.put("description", "must use format: HH:mm:ss, such as 14:01:01");
            return jsonSchema;
        }
        Set<Field> set = new LinkedHashSet<>();
        Field[] arr1 = type.getFields();
        if (arr1 != null) {
            set.addAll(Arrays.asList(arr1));
        }
        Field[] arr2 = type.getDeclaredFields();
        if (arr2 != null) {
            set.addAll(Arrays.asList(arr2));
        }
        for (Field field : set) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            if (Modifier.isFinal(modifiers)) {
                continue;
            }

            String paramName = addFieldJsonSchema(jsonSchema, field);
        }
        return jsonSchema;
    }

    public static Map<String, Object> getTypeSchemaInfo(Parameter parameter) {
        return getTypeSchemaInfo(parameter.getType(), () -> TypeToken.getGenericsParameterType(parameter));
    }

    public static Map<String, Object> getTypeSchemaInfo(Field field) {
        return getTypeSchemaInfo(field.getType(), () -> TypeToken.getGenericsFieldType(field));
    }

    public static Map<String, Object> getTypeSchemaInfo(Class<?> type, Supplier<Class<?>> collectionElementTypeSupplier) {
        Map<String, Object> pair = new LinkedHashMap<>();
        String schemaType = convertAsJsonSchemaType(type);
        pair.put("type", schemaType);
        if (String.class.getSimpleName().toLowerCase().equals(schemaType)) {
            if (type.isEnum()) {
                List<String> vars = new ArrayList<>();
                Object[] arr = type.getEnumConstants();
                for (Object item : arr) {
                    vars.add(String.valueOf(item));
                }
                pair.put("enum", vars);
            } else if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
                pair.put("format", "date-time");
                pair.put("description", "must use format: yyyy-MM-dd HH:mm:ss, such as 2020-01-01 14:01:01");
            } else if (TypeOf.typeOfAny(type, LocalDate.class)) {
                pair.put("format", "date");
                pair.put("description", "must use format: yyyy-MM-dd, such as 2020-01-01");
            } else if (TypeOf.typeOfAny(type, LocalTime.class)) {
                pair.put("format", "time");
                pair.put("description", "must use format: HH:mm:ss, such as 14:01:01");
            }
        } else if ("array".equals(schemaType)) {
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                Map<String, Object> next = getTypeJsonSchema(componentType);
                pair.put("items", next);
            } else if (TypeOf.typeOf(type, Collection.class)) {
                Class<?> elementType = collectionElementTypeSupplier.get();
                Map<String, Object> next = getTypeJsonSchema(elementType);
                pair.put("items", next);
            }
        } else if (Object.class.getSimpleName().toLowerCase().equals(schemaType)) {
            Map<String, Object> next = getTypeJsonSchema(type);
            pair.put("properties", next);
        }
        return pair;
    }

    public static String convertAsJsonSchemaType(Class<?> type) {
        if (TypeOf.isBaseType(type)) {
            return type.getSimpleName().toLowerCase();
        }
        if (TypeOf.typeOf(type, CharSequence.class)) {
            return String.class.getSimpleName().toLowerCase();
        }
        if (type.isArray() || TypeOf.typeOf(type, Collection.class)) {
            return "array";
        }
        if (type.isEnum()) {
            return String.class.getSimpleName().toLowerCase();
        }
        if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
            return String.class.getSimpleName().toLowerCase();
        }
        if (TypeOf.typeOfAny(type, LocalDate.class)) {
            return String.class.getSimpleName().toLowerCase();
        }
        if (TypeOf.typeOfAny(type, LocalTime.class)) {
            return String.class.getSimpleName().toLowerCase();
        }
        return Object.class.getSimpleName().toLowerCase();
    }
}
