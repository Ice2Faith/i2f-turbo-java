package i2f.extension.antlr4.script.tiny.impl.context;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2025/2/25 20:52
 * @desc
 */
public class TinyScriptFunctions {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static Date now() {
        return new Date();
    }

    public static int rand() {
        return RANDOM.nextInt();
    }

    public static int rand(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static void println(Object... obj) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : obj) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(item);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }

    public static void exec(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object arrNew(Class<?> elemType, int len) {
        return Array.newInstance(elemType, len);
    }

    public static boolean isArray(Object arr) {
        if (arr == null) {
            return false;
        }
        return arr.getClass().isArray();
    }

    public static int arrLen(Object arr) {
        if (!isArray(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        return Array.getLength(arr);
    }

    public static <T> T arrGet(Object arr, int index) {
        if (!isArray(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        int len = arrLen(arr);
        if (index < 0) {
            index = len + index;
        }
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException("array length is:" + len + " , but require index :" + index);
        }
        return (T) Array.get(arr, index);
    }

    public static void arrSet(Object arr, int index, Object value) {
        if (!isArray(arr)) {
            throw new IllegalArgumentException("expect array type,but found:" + (arr == null ? arr : arr.getClass()));
        }
        int len = arrLen(arr);
        if (index < 0) {
            index = len + index;
        }
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException("array length is:" + len + " , but require index :" + index);
        }
        Array.set(arr, index, value);
    }
}
