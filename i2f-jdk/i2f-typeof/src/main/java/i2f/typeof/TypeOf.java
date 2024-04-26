package i2f.typeof;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/26 10:39
 * @desc
 */
public class TypeOf {
    public static final Map<Class<?>, Class<?>> basicTypeMap;

    static {
        if (true) {
            Map<Class<?>, Class<?>> map = new LinkedHashMap<>();
            map.put(int.class, Integer.class);
            map.put(short.class, Short.class);
            map.put(long.class, Long.class);
            map.put(byte.class, Byte.class);
            map.put(float.class, Float.class);
            map.put(double.class, Double.class);
            map.put(char.class, Character.class);
            map.put(boolean.class, Boolean.class);
            map.put(void.class, Void.class);


            Map<Class<?>, Class<?>> reverseBasicTypeMap = new LinkedHashMap<>();
            for (Map.Entry<Class<?>, Class<?>> entry : map.entrySet()) {
                reverseBasicTypeMap.put(entry.getValue(), entry.getKey());
            }
            map.putAll(reverseBasicTypeMap);

            basicTypeMap = Collections.unmodifiableMap(map);
        }
    }

    public static boolean typeOfAny(Class<?> clazz, Class<?>... types) {
        for (Class<?> item : types) {
            if (typeOf(clazz, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean typeOfOnly(Class<?> clazz, Class<?> type) {
        if (clazz == null || type == null) {
            return false;
        }

        return type.equals(clazz) || type.isAssignableFrom(clazz);
    }

    public static boolean typeOf(Class<?> clazz, Class<?> type) {
        if (clazz == null || type == null) {
            return false;
        }

        if (typeOfOnly(clazz, type)) {
            return true;
        }

        if (typeOfOnly(type, basicTypeMap.get(clazz))) {
            return true;
        }

        if (typeOfOnly(clazz, basicTypeMap.get(type))) {
            return true;
        }

        return false;
    }

    public static boolean instanceOf(Object obj, Class<?> type) {
        if (obj == null || type == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return typeOf(clazz, type);
    }
}
