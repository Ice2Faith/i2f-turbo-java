package i2f.ai.std.tool.schema;

import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
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
    public static interface SchemaType {
        String STRING = "string";
        String NUMBER = "number";
        String BOOLEAN = "boolean";
        String INTEGER = "integer";
        String OBJECT = "object";
        String ARRAY = "array";
    }

    public static interface SchemaField {
        String NAME = "name";
        String DESCRIPTION = "description";
        String STRICT = "strict";
        String PARAMETERS = "parameters";
        String TYPE = "type";
        String FORMAT = "format";
        String ENUM = "enum";
        String ITEMS = "items";
        String PROPERTIES = "properties";
    }

    public static Map<String, Object> getFunctionJsonSchema(JsonSchemaAnnotationResolver resolver, Method method) {
        return getFunctionJsonSchema(resolver, method, null);
    }

    public static Map<String, Object> getFunctionJsonSchema(JsonSchemaAnnotationResolver resolver, Method method, List<String> parameterNames) {
        Map<String, Object> functionSchema = new LinkedHashMap<>();

        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }

        String name = method.getName();
        String description = method.getName();

        String annName = resolver.getToolMethodName(method);
        if (annName != null && !annName.isEmpty()) {
            name = annName;
        }
        String annDescription = resolver.getToolMethodDescription(method);
        if (annDescription != null && !annDescription.isEmpty()) {
            description = annDescription;
        }

        Map<String, Object> jsonSchema = new LinkedHashMap<>();
        Parameter[] parameters = method.getParameters();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramName = addParameterJsonSchema(resolver, jsonSchema, parameter);
                if (parameterNames != null) {
                    parameterNames.add(paramName);
                }
            }
        }

        functionSchema.put(SchemaField.NAME, name);
        functionSchema.put(SchemaField.DESCRIPTION, description);
        functionSchema.put(SchemaField.STRICT, true);
        functionSchema.put(SchemaField.PARAMETERS, jsonSchema);

        return functionSchema;
    }

    public static String addParameterJsonSchema(JsonSchemaAnnotationResolver resolver, Map<String, Object> jsonSchema, Parameter parameter) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        String paramName = parameter.getName();
        String paramDesc = paramName;

        String annName = resolver.getToolParameterName(parameter);
        if (annName != null && !annName.isEmpty()) {
            paramName = annName;
        }
        String annDescription = resolver.getToolParameterDescription(parameter);
        if (annDescription != null && !annDescription.isEmpty()) {
            paramDesc = annDescription;
        }

        Map<String, Object> pair = getTypeSchemaInfo(resolver, parameter);
        Object additionalDesc = pair.get(SchemaField.DESCRIPTION);
        pair.put(SchemaField.DESCRIPTION, paramDesc + (additionalDesc == null ? "" : ". " + additionalDesc));
        jsonSchema.put(paramName, pair);
        return paramName;
    }

    public static String addFieldJsonSchema(JsonSchemaAnnotationResolver resolver, Map<String, Object> jsonSchema, Field field) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        String paramName = field.getName();
        String paramDesc = paramName;
        String annDescription = resolver.getToolFieldDescription(field);
        if (annDescription != null && !annDescription.isEmpty()) {
            paramDesc = annDescription;
        }
        Map<String, Object> pair = getTypeSchemaInfo(resolver, field);

        Object additionalDesc = pair.get(SchemaField.DESCRIPTION);
        pair.put(SchemaField.DESCRIPTION, paramDesc + (additionalDesc == null ? "" : ". " + additionalDesc));

        jsonSchema.put(paramName, pair);

        return paramName;
    }

    public static Map<String, Object> getTypeJsonSchema(JsonSchemaAnnotationResolver resolver, Class<?> type) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        Map<String, Object> jsonSchema = new LinkedHashMap<>();
        if (TypeOf.isBaseType(type)) {
            jsonSchema.put(SchemaField.TYPE, convertAsJsonSchemaType(type));
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
            jsonSchema.put(SchemaField.TYPE, convertAsJsonSchemaType(type));
            jsonSchema.put(SchemaField.FORMAT, "date-time");
            jsonSchema.put(SchemaField.DESCRIPTION, "must use format: yyyy-MM-dd HH:mm:ss, such as 2020-01-01 14:01:01");
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, LocalDate.class)) {
            jsonSchema.put(SchemaField.TYPE, convertAsJsonSchemaType(type));
            jsonSchema.put(SchemaField.FORMAT, "date");
            jsonSchema.put(SchemaField.DESCRIPTION, "must use format: yyyy-MM-dd, such as 2020-01-01");
            return jsonSchema;
        } else if (TypeOf.typeOfAny(type, LocalTime.class)) {
            jsonSchema.put(SchemaField.TYPE, convertAsJsonSchemaType(type));
            jsonSchema.put(SchemaField.FORMAT, "time");
            jsonSchema.put(SchemaField.DESCRIPTION, "must use format: HH:mm:ss, such as 14:01:01");
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

            String paramName = addFieldJsonSchema(resolver, jsonSchema, field);
        }
        return jsonSchema;
    }

    public static Map<String, Object> getTypeSchemaInfo(JsonSchemaAnnotationResolver resolver, Parameter parameter) {
        return getTypeSchemaInfo(resolver, parameter.getType(), () -> TypeToken.getGenericsParameterType(parameter));
    }

    public static Map<String, Object> getTypeSchemaInfo(JsonSchemaAnnotationResolver resolver, Field field) {
        return getTypeSchemaInfo(resolver, field.getType(), () -> TypeToken.getGenericsFieldType(field));
    }

    public static Map<String, Object> getTypeSchemaInfo(JsonSchemaAnnotationResolver resolver, Class<?> type, Supplier<Class<?>> collectionElementTypeSupplier) {
        if (resolver == null) {
            resolver = JsonSchemaAnnotationResolver.INSTANCE;
        }
        Map<String, Object> pair = new LinkedHashMap<>();
        String schemaType = convertAsJsonSchemaType(type);
        pair.put(SchemaField.TYPE, schemaType);
        if (SchemaType.STRING.equals(schemaType)) {
            if (type.isEnum()) {
                List<String> vars = new ArrayList<>();
                Object[] arr = type.getEnumConstants();
                for (Object item : arr) {
                    vars.add(String.valueOf(item));
                }
                pair.put(SchemaField.ENUM, vars);
            } else if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
                pair.put(SchemaField.FORMAT, "date-time");
                pair.put(SchemaField.DESCRIPTION, "must use format: yyyy-MM-dd HH:mm:ss, such as 2020-01-01 14:01:01");
            } else if (TypeOf.typeOfAny(type, LocalDate.class)) {
                pair.put(SchemaField.FORMAT, "date");
                pair.put(SchemaField.DESCRIPTION, "must use format: yyyy-MM-dd, such as 2020-01-01");
            } else if (TypeOf.typeOfAny(type, LocalTime.class)) {
                pair.put(SchemaField.FORMAT, "time");
                pair.put(SchemaField.DESCRIPTION, "must use format: HH:mm:ss, such as 14:01:01");
            }
        } else if (SchemaType.ARRAY.equals(schemaType)) {
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                Map<String, Object> next = getTypeJsonSchema(resolver, componentType);
                pair.put(SchemaField.ITEMS, next);
            } else if (TypeOf.typeOf(type, Collection.class)) {
                Class<?> elementType = collectionElementTypeSupplier.get();
                Map<String, Object> next = getTypeJsonSchema(resolver, elementType);
                pair.put(SchemaField.ITEMS, next);
            }
        } else if (SchemaType.OBJECT.equals(schemaType)) {
            Map<String, Object> next = getTypeJsonSchema(resolver, type);
            pair.put(SchemaField.PROPERTIES, next);
        }
        return pair;
    }

    public static String convertAsJsonSchemaType(Class<?> type) {
        if (TypeOf.isBaseType(type)) {
            if (TypeOf.typeOfAny(type, Long.class, long.class,
                    Integer.class, int.class,
                    Short.class, short.class,
                    BigInteger.class)) {
                return SchemaType.INTEGER;
            } else if (TypeOf.typeOfAny(type, Boolean.class, boolean.class)) {
                return SchemaType.BOOLEAN;
            } else if (TypeOf.typeOfAny(type, Number.class)) {
                return SchemaType.NUMBER;
            }
            return SchemaType.STRING;
        }
        if (TypeOf.typeOf(type, CharSequence.class)) {
            return SchemaType.STRING;
        }
        if (type.isArray() || TypeOf.typeOf(type, Collection.class)) {
            return SchemaType.ARRAY;
        }
        if (type.isEnum()) {
            return SchemaType.STRING;
        }
        if (TypeOf.typeOfAny(type, Date.class, LocalDateTime.class)) {
            return SchemaType.STRING;
        }
        if (TypeOf.typeOfAny(type, LocalDate.class)) {
            return SchemaType.STRING;
        }
        if (TypeOf.typeOfAny(type, LocalTime.class)) {
            return SchemaType.STRING;
        }
        return SchemaType.OBJECT;
    }
}
