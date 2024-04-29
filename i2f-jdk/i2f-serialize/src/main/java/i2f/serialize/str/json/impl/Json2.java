package i2f.serialize.str.json.impl;

import i2f.check.Predicates;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class Json2 {
    public static boolean nullExclude = false;
    public static String sep = ",";
    public static String quote = "\"";
    public static String null2 = "null";
    public static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static String toJson(Object obj) {
        if (Predicates.isNull(obj)) {
            return whenNull(obj);
        }
        if (obj instanceof Boolean) {
            return whenBoolean((Boolean) obj);
        }
        if (obj instanceof String) {
            return whenString((String) obj);
        }
        if (obj instanceof Number) {
            return whenNumber((Number) obj);
        }
        if (obj instanceof Date) {
            return whenDate((Date) obj);
        }
        if (obj.getClass().isArray()) {
            return whenArray(obj);
        }
        if (obj instanceof Map) {
            return whenMap((Map) obj);
        }
        if (obj instanceof Collection) {
            return whenCollection((Collection) obj);
        }

        return whenBean(obj);
    }

    private static String whenNull(Object obj) {
        return null2;
    }

    private static String whenBoolean(Boolean obj) {
        return String.valueOf(obj);
    }

    private static String whenString(String str) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(quote);
        char[] bufs = str.toCharArray();
        for (char ch : bufs) {
            if (ch == '\"') {
                buffer.append("\\\"");
            } else if (ch == '\\') {
                buffer.append("\\\\");
            } else if (ch == '\b') {
                buffer.append("\\b");
            } else if (ch == '\f') {
                buffer.append("\\f");
            } else if (ch == '\n') {
                buffer.append("\\n");
            } else if (ch == '\r') {
                buffer.append("\\r");
            } else if (ch == '\t') {
                buffer.append("\\t");
            } else if (Predicates.isCh(ch)) {
                buffer.append(ch);
            } else if (Predicates.isIn(ch, '&', '=', '<', '>', '\'')) {
                buffer.append("\\u");
                String hex = Integer.toHexString((int) ch & 0xffff);
                for (int i = hex.length(); i < 4; i++) {
                    buffer.append('0');
                }
                buffer.append(hex);
            } else {
                buffer.append(ch);
            }
        }
        buffer.append(quote);
        return buffer.toString();
    }

    private static String whenNumber(Number num) {
        return num.toString();
    }

    private static String whenDate(Date date) {
        return quote + fmt.format(date) + quote;
    }

    private static String whenCollection(Collection col) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        if (Predicates.nonNull(col)) {
            boolean isFirst = true;
            for (Object item : col) {
                if (nullExclude && Predicates.isNull(item)) {
                    continue;
                }
                if (!isFirst) {
                    buffer.append(sep);
                }
                buffer.append(toJson(item));
                isFirst = false;
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    private static String whenArray(Object arr) {
        if (Predicates.nonNull(arr) && !Predicates.isArrayType(arr)) {
            return toJson(arr);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        if (Predicates.nonNull(arr)) {
            int len = Array.getLength(arr);
            boolean isFirst = true;
            for (int i = 0; i < len; i++) {
                Object val = Array.get(arr, i);
                if (nullExclude && Predicates.isNull(val)) {
                    continue;
                }
                if (!isFirst) {
                    buffer.append(sep);
                }
                buffer.append(toJson(val));
                isFirst = false;
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    private static String whenMap(Map map) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (Predicates.nonNull(map)) {
            boolean isFirst = true;
            for (Object item : map.keySet()) {
                Object val = map.get(item);
                if (nullExclude && Predicates.isNull(val)) {
                    continue;
                }
                if (!isFirst) {
                    buffer.append(sep);
                }
                buffer.append(quote).append(item).append(quote).append(":").append(toJson(val));
                isFirst = false;
            }
        }
        buffer.append("}");
        return buffer.toString();
    }

    private static String whenBean(Object obj) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (Predicates.nonNull(obj)) {
            Class clazz = obj.getClass();
            Set<Field> fields = ReflectResolver.getFields(clazz).keySet();
            boolean isFirst = true;
            for (Field item : fields) {
                String name = item.getName();
                try {
                    Object val = ReflectResolver.valueGet(obj, item);
                    if (nullExclude && Predicates.isNull(val)) {
                        continue;
                    }
                    if (!isFirst) {
                        buffer.append(sep);
                    }
                    buffer.append(quote).append(name).append(quote).append(":").append(toJson(val));
                    isFirst = false;
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        buffer.append("}");
        return buffer.toString();
    }
}
