package i2f.turbo.idea.plugin.utils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/8/20 14:48
 * @desc
 */
public class CompletionUtils {
    public static final Map<String, Class<?>> FUNCTIONS = Collections.unmodifiableMap(getFunctions());

    public static void getFunctionsNext(Class<?> clazz, Map<String, Class<?>> completions) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String next = method.getName() + "()";
            if (!completions.containsKey(next)) {
                completions.put(next, clazz);
            }
        }
    }

    public static Map<String, Class<?>> getFunctions() {
        Map<String, Class<?>> completions = new LinkedHashMap<>();

        // String 类方法
        getFunctionsNext(String.class, completions);

        // Collection 类方法
        getFunctionsNext(Collection.class, completions);

        // Map 类方法
        getFunctionsNext(Map.class, completions);

        // List 类方法
        getFunctionsNext(List.class, completions);

        // HashMap 类方法
        getFunctionsNext(HashMap.class, completions);
        return completions;
    }
}
