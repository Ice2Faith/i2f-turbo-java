package i2f.mixin.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.mixin.MixinProxyFactory;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:06
 * @desc
 */
public interface ObjectMixins {

    default BigDecimal to_number(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        return (BigDecimal) ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
    }

    default Integer to_int(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (int) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return (Integer) ObjectConvertor.tryConvertAsType(obj, Integer.class);
    }

    default Long to_long(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (long) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return (Long) ObjectConvertor.tryConvertAsType(obj, Long.class);
    }

    default boolean to_boolean(Object obj) {
        return ObjectConvertor.toBoolean(obj);
    }

    default boolean isnull(Object obj) {
        return obj == null;
    }

    default boolean not_null(Object obj) {
        return !isnull(obj);
    }

    default boolean is_empty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String
                || obj instanceof CharSequence
                || obj instanceof Appendable) {
            return String.valueOf(obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Iterator) {
            return !((Iterator) obj).hasNext();
        }
        if (obj instanceof Enumeration) {
            return !((Enumeration) obj).hasMoreElements();
        }
        return false;
    }

    default boolean is_blank(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String
                || obj instanceof CharSequence
                || obj instanceof Appendable) {
            String str = String.valueOf(obj);
            char[] arr = str.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char ch = arr[i];
                if (!Character.isWhitespace(ch)) {
                    return false;
                }
            }
            return true;
        }
        return is_empty(obj);
    }

    default int length(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof CharSequence
                || obj instanceof String
                || obj instanceof Appendable) {
            return String.valueOf(obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        throw new IllegalArgumentException("length(obj) function cannot support type:" + obj.getClass());
    }

    default int lengthb(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof CharSequence
                || obj instanceof String
                || obj instanceof Appendable) {
            String str = String.valueOf(obj);
            return str.getBytes(StandardCharsets.UTF_8).length;
        }
        return length(obj);
    }

    default Object if_empty(Object v1, Object v2) {
        return is_empty(v1) ? v2 : v1;
    }

    default Object evl(Object v1, Object v2) {
        return if_empty(v1, v2);
    }

    default Object if_blank(Object v1, Object v2) {
        return is_blank(v1) ? v2 : v1;
    }

    default Object ifnull(Object v1, Object v2) {
        return v1 != null ? v1 : v2;
    }

    default Object nullif(Object v1, Object v2) {
        if (v1 == v2) {
            return null;
        }
        if (v1 == null || v2 == null) {
            return v1;
        }
        if (v1.equals(v2) || v2.equals(v1)) {
            return null;
        }
        if ((v1 instanceof CharSequence)
                && (v2 instanceof CharSequence)) {
            return String.valueOf(v1).equals(String.valueOf(v2));
        }
        if (ObjectConvertor.isNumericType(v1.getClass())
                && ObjectConvertor.isNumericType(v2.getClass())) {
            BigDecimal n1 = (BigDecimal) ObjectConvertor.tryConvertAsType(v1, BigDecimal.class);
            BigDecimal n2 = (BigDecimal) ObjectConvertor.tryConvertAsType(v2, BigDecimal.class);
            if (n1.compareTo(n2) == 0) {
                return null;
            }
        }
        if ((v1 instanceof Comparable)
                && (v2 instanceof Comparable)) {
            Comparable c1 = (Comparable) v1;
            Comparable c2 = (Comparable) v2;
            if (c1.compareTo(c2) == 0) {
                return null;
            }
        }
        return v1;
    }

    default Object nvl(Object v1, Object v2) {
        return ifnull(v1, v2);
    }

    default Object bvl(Object v1, Object v2) {
        return if_blank(v1, v2);
    }

    default Object if2(Object cond, Object trueVal, Object falseVal) {
        if (cond instanceof Boolean) {
            Boolean ok = (Boolean) cond;
            if (ok) {
                return trueVal;
            } else {
                return falseVal;
            }
        }
        if (to_boolean(cond)) {
            return trueVal;
        } else {
            return falseVal;
        }
    }

    default Object nvl2(Object cond, Object trueVal, Object falseVal) {
        return if2(cond, trueVal, falseVal);
    }

    default Object nvl_args(Object value, Object... values) {
        return coalesce(value, values);
    }

    default Object coalesce(Object value, Object... values) {
        if (value != null) {
            return value;
        }
        if (values == null || values.length == 0) {
            return value;
        }
        Object last = value;
        for (Object item : values) {
            last = item;
            if (item != null) {
                return item;
            }
        }
        return last;
    }

    default Object evl_args(Object value, Object... values) {
        return coalesce_empty(value, values);
    }

    default Object coalesce_empty(Object value, Object... values) {
        if (!is_empty(value)) {
            return value;
        }
        if (values == null || values.length == 0) {
            return value;
        }
        Object last = value;
        for (Object item : values) {
            last = item;
            if (!is_empty(item)) {
                return item;
            }
        }
        return last;
    }

    default Object decode(Object target, Object... args) {
        int i = 0;
        while (i + 1 < args.length) {
            if (Objects.equals(target, args[i])) {
                return args[i + 1];
            }
            i += 2;
        }
        if (args.length % 2 != 0) {
            return args[args.length - 1];
        }
        return target;
    }

    default Object cast(Object val, Object type) {
        if (type == null) {
            throw new ClassCastException("cannot cast value to type null");
        }
        if (val == null) {
            return null;
        }
        Class<?> clazz = type.getClass();
        if (type instanceof Class) {
            clazz = (Class<?>) type;
        } else {
            try {
                Class<?> clz = ReflectResolver.loadClass(String.valueOf(type));
                if (clz != null) {
                    clazz = clz;
                }
            } catch (Exception e) {

            }
        }
        return ObjectConvertor.tryConvertAsType(val, clazz);
    }

    default Object convert(Object val, Object type) {
        return cast(val, type);
    }

    default Object visit_get(Object obj, String expression) {
        return Visitor.visit(expression, obj).get();
    }

    default void visit_set(Object obj, String expression, Object value) {
        Visitor.visit(expression, obj).set(value);
    }

    default void visit_del(Object obj, String expression) {
        Visitor.visit(expression, obj).delete();
    }

    default long hashcode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    default boolean equal(Object obj1, Object obj2) {
        if (isnull(obj1) && isnull(obj2)) {
            return true;
        }
        if (isnull(obj1)) {
            return obj2.equals(obj1);
        }
        return obj1.equals(obj2);
    }

    default int compare(Object left, Object right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        if (left.equals(right)) {
            return 0;
        }
        if (left.getClass().equals(right.getClass())) {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
        }
        if (TypeOf.typeOf(right.getClass(), left.getClass())) {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
        }
        if (TypeOf.typeOf(left.getClass(), right.getClass())) {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
        }
        if (ObjectConvertor.isNumericType(left.getClass())
                && ObjectConvertor.isNumericType(right.getClass())) {
            BigDecimal bl = new BigDecimal(String.valueOf(left));
            BigDecimal br = new BigDecimal(String.valueOf(right));
            return bl.compareTo(br);
        }
        if (ObjectConvertor.isDateType(left.getClass())
                && ObjectConvertor.isDateType(right.getClass())) {
            try {
                Date bl = (Date) ObjectConvertor.tryConvertAsType(left, Date.class);
                Date br = (Date) ObjectConvertor.tryConvertAsType(right, Date.class);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (ObjectConvertor.isBooleanType(left.getClass())
                || ObjectConvertor.isBooleanType(right.getClass())) {
            try {
                Boolean bl = ObjectConvertor.toBoolean(left);
                Boolean br = ObjectConvertor.toBoolean(right);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (TypeOf.typeOfAny(left.getClass(), String.class, CharSequence.class, Appendable.class)
                || TypeOf.typeOfAny(right.getClass(), String.class, CharSequence.class, Appendable.class)) {
            String sl = String.valueOf(left);
            String sr = String.valueOf(right);
            return sl.compareTo(sr);
        }
        try {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
            if (right instanceof Comparable<?>) {
                int ret = ((Comparable) right).compareTo(left);
                if (ret == 0) {
                    return 0;
                }
                if (ret > 0) {
                    return -1;
                }
                return 1;
            }
        } catch (Exception e) {

        }
        return Integer.compare(left.hashCode(), right.hashCode());
    }

    default boolean cmp_eq(Object v1, Object v2) {
        return compare(v1, v2) == 0;
    }

    default boolean cmp_neq(Object v1, Object v2) {
        return compare(v1, v2) != 0;
    }

    default boolean cmp_gt(Object v1, Object v2) {
        return compare(v1, v2) > 0;
    }

    default boolean cmp_lt(Object v1, Object v2) {
        return compare(v1, v2) < 0;
    }

    default boolean cmp_gte(Object v1, Object v2) {
        return compare(v1, v2) >= 0;
    }

    default boolean cmp_lte(Object v1, Object v2) {
        return compare(v1, v2) <= 0;
    }

    default Object trunc(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        if (ObjectConvertor.isNumericType(clazz)) {
            return MixinProxyFactory.getMixinInstance(MathMixins.class).trunc(obj, 0);
        } else if (ObjectConvertor.isDateType(clazz)) {
            return MixinProxyFactory.getMixinInstance(DateMixins.class).trunc(obj, "");
        }
        throw new IllegalArgumentException("un-support trunc apply to type, expect date/number, but found :" + clazz);
    }

}
