package i2f.functional.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/9/27 17:12
 */
public class Func {
    public static <T, V> void setIf(T obj, BiConsumer<T, V> setter, V value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) {
                return;
            }
        }
        setter.accept(obj, value);
    }

    public static <V> void setIf(Consumer<V> setter, V value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) {
                return;
            }
        }
        setter.accept(value);
    }
}
