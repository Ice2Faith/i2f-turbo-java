package i2f.extension.antlr4.script.funic.lang.functions;

import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.funic.lang.annotations.FunicFunction;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/4/23 17:54
 * @desc
 */
public class FunicBuiltinFunctions {
    protected DefaultFunicVisitor visitor;

    public FunicBuiltinFunctions(DefaultFunicVisitor visitor) {
        this.visitor = visitor;
    }

    public Object eval(String formula) {
        return Funic.script(formula, visitor);
    }

    public String render(String text) {
        if (text == null) {
            return null;
        }
        return visitor.getResolver().renderString(text, visitor);
    }

    public Object assign(Object obj, Object... args) {
        if (obj instanceof Collection) {
            Collection list = (Collection) obj;
            for (Object item : args) {
                if (item == null) {
                    list.add(item);
                } else if (item instanceof Iterable) {
                    Iterable<?> iterable = (Iterable<?>) item;
                    for (Object v : iterable) {
                        list.add(v);
                    }
                } else if (item instanceof Iterator) {
                    Iterator<?> iterator = (Iterator<?>) item;
                    while (iterator.hasNext()) {
                        Object v = iterator.next();
                        list.add(v);
                    }
                } else if (item instanceof Enumeration) {
                    Enumeration<?> enumeration = (Enumeration<?>) item;
                    while (enumeration.hasMoreElements()) {
                        Object v = enumeration.nextElement();
                        list.add(v);
                    }
                } else if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        list.add(entry);
                    }
                } else if (item.getClass().isArray()) {
                    int len = Array.getLength(item);
                    for (int i = 0; i < len; i++) {
                        list.add(Array.get(item, i));
                    }
                } else {
                    list.add(item);
                }
            }
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            for (Object item : args) {
                Map<String, Object> unpackMap = visitor.getResolver().unpackMap(item, visitor);
                Set<String> keys = new HashSet<>(unpackMap.keySet());
                for (String key : keys) {
                    Object v = unpackMap.get(key);
                    if (v == null) {
                        unpackMap.remove(key);
                    }
                }
                map.putAll(unpackMap);
            }
        } else {
            for (Object item : args) {
                Map<String, Object> unpackMap = visitor.getResolver().unpackMap(item, visitor);
                Set<String> keys = new HashSet<>(unpackMap.keySet());
                for (String key : keys) {
                    Object v = unpackMap.get(key);
                    if (v == null) {
                        unpackMap.remove(key);
                    }
                    if (key == null) {
                        unpackMap.remove(key);
                    }
                }
                ReflectResolver.map2bean(unpackMap, obj);
            }
        }
        return obj;
    }

    public int compare(Object v1, Object v2) {
        return visitor.getResolver().compare(v1, v2, false);
    }

    public int compare(Object v1, Object v2, boolean forceType) {
        return visitor.getResolver().compare(v1, v2, forceType);
    }

    public Object cast(Object value, Class<?> clazz) {
        return visitor.getResolver().convertType(value, clazz, visitor);
    }

    @FunicFunction(value = "int")
    public Integer to_int(Object value) {
        return (Integer) cast(value, Integer.class);
    }

    @FunicFunction(value = "string")
    public String to_string(Object value) {
        return (String) cast(value, String.class);
    }

    @FunicFunction(value = "double")
    public Double to_double(Object value) {
        return (Double) cast(value, Double.class);
    }

    @FunicFunction(value = "boolean")
    public boolean to_boolean(Object value) {
        return visitor.getResolver().toBoolean(value, visitor);
    }

    @FunicFunction(value = "decimal")
    public BigDecimal to_decimal(Object value) {
        return (BigDecimal) cast(value, BigDecimal.class);
    }
}
