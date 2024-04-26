package i2f.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/3/29 17:07
 * @desc
 */

public class ReflectSignature {
    private static final Map<String, Class<?>> simpleSignaturesNameTypeMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, String> simpleSignaturesTypeNameMap = new ConcurrentHashMap<>();


    static {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Z", boolean.class);
        map.put("B", byte.class);
        map.put("C", char.class);
        map.put("S", short.class);
        map.put("I", int.class);
        map.put("J", long.class);
        map.put("F", float.class);
        map.put("D", double.class);
        map.put("V", void.class);

        simpleSignaturesNameTypeMap.putAll(map);

        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            simpleSignaturesTypeNameMap.put(entry.getValue(), entry.getKey());
        }

    }

    public static <T> String toSign(Class<T> clazz) {
        String sign = simpleSignaturesTypeNameMap.get(clazz);
        if (sign == null) {
            if (clazz.isArray()) {
                sign = clazz.getName();
            } else {
                String name = clazz.getName();
                String path = ReflectResolver.className2UrlPath(name);
                sign = "L" + path + ";";
            }
        }
        return sign;
    }

    public static Class<?> ofSign(String sign) {
        Class<?> clazz = simpleSignaturesNameTypeMap.get(sign);
        if (clazz == null) {
            if (sign.startsWith("[")) {
                clazz = ReflectResolver.loadClass(sign);
            } else {
                if (sign.startsWith("L")) {
                    sign = sign.substring(1);
                }
                if (sign.endsWith(";")) {
                    sign = sign.substring(0, sign.length() - 1);
                }
                String name = ReflectResolver.path2ClassName(sign);
                clazz = ReflectResolver.loadClass(name);
            }
        }
        if (clazz == null) {
            clazz = ReflectResolver.loadClass(sign);
        }
        return clazz;
    }

    public static String sign(Method method) {
        Parameter[] params = method.getParameters();
        Class<?> retType = method.getReturnType();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Parameter param : params) {
            builder.append(toSign(param.getType()));
        }
        builder.append(")");
        builder.append(toSign(retType));
        return builder.toString();
    }

    public static String sign(Field field) {
        return toSign(field.getType());
    }

}
