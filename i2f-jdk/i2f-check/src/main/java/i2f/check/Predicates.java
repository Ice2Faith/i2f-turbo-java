package i2f.check;


import java.util.Collection;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/19 11:30
 * @desc 提供一些断言方法，用于在方法引用中使用
 * Predicates::isNull
 */
public class Predicates {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return !isNull(obj);
    }

    public static <T> boolean isEquals(T o1, T o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 != null) {
            return o1.equals(o2);
        }
        return false;
    }

    public static <T> boolean nonEquals(T o1, T o2) {
        return !isEquals(o1, o2);
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean nonEmptyString(String str) {
        return !isEmptyString(str);
    }

    public static boolean isBlankString(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean nonBlankString(String str) {
        return !isBlankString(str);
    }

    public static <T> boolean isEmptyCollection(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean nonEmptyCollection(Collection<T> collection) {
        return !isEmptyCollection(collection);
    }

    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> boolean nonEmptyMap(Map<K, V> map) {
        return !isEmptyMap(map);
    }

    public static boolean isArrayType(Object arr) {
        return arr != null && arr.getClass().isArray();
    }

    public static boolean nonArrayType(Object arr) {
        return !isArrayType(arr);
    }

    public static <T> boolean isEmptyArray(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <T> boolean nonEmptyArray(T[] arr) {
        return !isEmptyArray(arr);
    }

    public static boolean isEmptyBooleanArray(boolean[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyBooleanArray(boolean[] arr) {
        return !isEmptyBooleanArray(arr);
    }

    public static boolean isEmptyByteArray(byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyByteArray(byte[] arr) {
        return !isEmptyByteArray(arr);
    }

    public static boolean isEmptyCharArray(char[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyCharArray(char[] arr) {
        return !isEmptyCharArray(arr);
    }

    public static boolean isEmptyDoubleArray(double[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyDoubleArray(double[] arr) {
        return !isEmptyDoubleArray(arr);
    }

    public static boolean isEmptyFloatArray(float[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyFloatArray(float[] arr) {
        return !isEmptyFloatArray(arr);
    }

    public static boolean isEmptyIntArray(int[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyIntArray(int[] arr) {
        return !isEmptyIntArray(arr);
    }

    public static boolean isEmptyLongArray(long[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyLongArray(long[] arr) {
        return !isEmptyLongArray(arr);
    }

    public static boolean isEmptyShortArray(short[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean nonEmptyShortArray(short[] arr) {
        return !isEmptyShortArray(arr);
    }

    public static boolean isType(Object obj, Class<?> type) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return clazz.equals(type) || type.isAssignableFrom(clazz);
    }

    public static boolean isStringType(Object obj) {
        return obj instanceof String;
    }

    public static boolean nonStringType(Object obj) {
        return !isStringType(obj);
    }

    public static boolean isNumberType(Object obj) {
        return obj instanceof Number;
    }

    public static boolean nonNumberType(Object obj) {
        return !isNumberType(obj);
    }

    public static boolean isBooleanType(Object obj) {
        return obj instanceof Boolean;
    }

    public static boolean nonBooleanType(Object obj) {
        return !isBooleanType(obj);
    }

    public static boolean isByteType(Object obj) {
        return obj instanceof Byte;
    }

    public static boolean nonByteType(Object obj) {
        return !isByteType(obj);
    }

    public static boolean isCharType(Object obj) {
        return obj instanceof Character;
    }

    public static boolean nonCharType(Object obj) {
        return !isCharType(obj);
    }

    public static boolean isDoubleType(Object obj) {
        return obj instanceof Double;
    }

    public static boolean nonDoubleType(Object obj) {
        return !isDoubleType(obj);
    }

    public static boolean isFloatType(Object obj) {
        return obj instanceof Float;
    }

    public static boolean nonFloatType(Object obj) {
        return !isFloatType(obj);
    }

    public static boolean isIntegerType(Object obj) {
        return obj instanceof Integer;
    }

    public static boolean nonIntegerType(Object obj) {
        return !isIntegerType(obj);
    }

    public static boolean isLongType(Object obj) {
        return obj instanceof Long;
    }

    public static boolean nonLongType(Object obj) {
        return !isLongType(obj);
    }

    public static boolean isShortType(Object obj) {
        return obj instanceof Short;
    }

    public static boolean nonShortType(Object obj) {
        return !isShortType(obj);
    }

    public static boolean isIntegerString(String str) {
        return str != null && str.matches("[+|-]?\\d+");
    }

    public static boolean nonIntegerString(String str) {
        return !isIntegerString(str);
    }

    public static boolean isDoubleString(String str) {
        return str != null && str.matches("[+|-]?\\d+(\\.\\d+)");
    }

    public static boolean nonDoubleString(String str) {
        return !isDoubleString(str);
    }

    public static boolean isMatchString(String str, String patten) {
        return str != null && str.matches(patten);
    }

    public static boolean nonMatchString(String str, String patten) {
        return !isMatchString(str, patten);
    }


    public static boolean isChLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isChUpper(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isCh(char ch) {
        return isChLower(ch) || isChUpper(ch);
    }

    public static boolean isChNum(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isChNum(char ch, int base) {
        if (base < 2 || base > (10 + 26)) {
            return false;
        }
        if (base <= 10) {
            if (ch >= '0' && ch <= ('0' + base - 1)) {
                return true;
            }
        } else {
            if (isChNum(ch)) {
                return true;
            }
            int off = base - 10;
            if (ch >= 'A' && ch <= ('A' + off - 1)) {
                return true;
            }
            if (ch >= 'a' && ch <= ('a' + off - 1)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(Object tar, Object... vals) {
        if (isEmptyArray(vals)) {
            return false;
        }
        for (Object item : vals) {
            if (isNull(tar)) {
                if (isNull(item)) {
                    return true;
                }
            } else {
                if (tar.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean notIn(Object tar, Object... vals) {
        return !isIn(tar, vals);
    }

}
