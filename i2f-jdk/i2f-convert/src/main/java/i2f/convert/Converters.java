package i2f.convert;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/30 11:23
 * @desc
 */
public class Converters {

    public static <T> Integer parseInt(T obj) {
        return parseAs(obj, String::valueOf, Integer::valueOf, null);
    }

    public static <T> Long parseLong(T obj) {
        return parseAs(obj, String::valueOf, Long::valueOf, null);
    }

    public static <T> Short parseShort(T obj) {
        return parseAs(obj, String::valueOf, Short::valueOf, null);
    }

    public static <T> Boolean parseBoolean(T obj) {
        return parseAs(obj, String::valueOf, Boolean::valueOf, null);
    }

    public static <T> Double parseDouble(T obj) {
        return parseAs(obj, String::valueOf, Double::valueOf, null);
    }

    public static <T> Float parseFloat(T obj) {
        return parseAs(obj, String::valueOf, Float::valueOf, null);
    }

    public static <T> int parseInt(T obj, int defVal) {
        return parseAs(obj, String::valueOf, Integer::valueOf, defVal);
    }

    public static <T> long parseLong(T obj, long defVal) {
        return parseAs(obj, String::valueOf, Long::valueOf, defVal);
    }

    public static <T> short parseShort(T obj, short defVal) {
        return parseAs(obj, String::valueOf, Short::valueOf, defVal);
    }

    public static <T> boolean parseBoolean(T obj, boolean defVal) {
        return parseAs(obj, String::valueOf, Boolean::valueOf, defVal);
    }

    public static <T> double parseDouble(T obj, double defVal) {
        return parseAs(obj, String::valueOf, Double::valueOf, defVal);
    }

    public static <T> float parseFloat(T obj, float defVal) {
        return parseAs(obj, String::valueOf, Float::valueOf, defVal);
    }

    public static <T, R> R parseAs(T obj, Function<T, R> parser, R defVal) {
        return parseAs(obj, v -> v, parser, defVal);
    }

    public static <T, V, R> R parseAs(T obj, Function<T, V> mapper, Function<V, R> parser, R defVal) {
        if (obj == null) {
            return defVal;
        }
        V val = mapper.apply(obj);
        if (val == null) {
            return defVal;
        }
        try {
            return parser.apply(val);
        } catch (Throwable e) {

        }
        return defVal;
    }
}
