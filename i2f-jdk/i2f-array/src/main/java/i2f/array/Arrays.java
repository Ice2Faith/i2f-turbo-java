package i2f.array;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/23 14:46
 * @desc
 */
public class Arrays {
    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().isArray();
    }

    public static int arrayLength(Object arr) {
        return Array.getLength(arr);
    }

    public static Object arrayGet(Object arr, int index) {
        return Array.get(arr, index);
    }

    public static void arraySet(Object arr, int index, Object value) {
        Array.set(arr, index, value);
    }


    public static <T> T[] of(T... arr) {
        return arr;
    }

    public static int[] ofInt(int... arr) {
        return arr;
    }

    public static long[] ofLong(long... arr) {
        return arr;
    }

    public static short[] ofShort(short... arr) {
        return arr;
    }

    public static byte[] ofByte(byte... arr) {
        return arr;
    }

    public static char[] ofChar(char... arr) {
        return arr;
    }

    public static double[] ofDouble(double... arr) {
        return arr;
    }

    public static float[] ofFloat(float... arr) {
        return arr;
    }

    public static boolean[] ofBoolean(boolean... arr) {
        return arr;
    }

    public static <T> List<T> oneList(T obj) {
        List<T> ret = new ArrayList<>();
        if (obj == null) {
            return ret;
        }
        ret.add(obj);
        return ret;
    }


    public static <T> List<T> ofList(T... arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <T, C extends Collection<T>> C ofAsCollection(Supplier<C> supplier, T... arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <T, C extends Collection<T>> C ofCollection(C col, T... arr) {
        if (arr == null) {
            return col;
        }
        for (T item : arr) {
            col.add(item);
        }
        return col;
    }

    public static <T> List<T> toList(T[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <T, C extends Collection<T>> C asCollection(Supplier<C> supplier, T[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <T, C extends Collection<T>> C toCollection(C col, T[] arr) {
        if (arr == null) {
            return col;
        }
        for (T item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Integer> toList(int[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Integer>> C asCollection(Supplier<C> supplier, int[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Integer>> C toCollection(C col, int[] arr) {
        if (arr == null) {
            return col;
        }
        for (int item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Long> toList(long[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Long>> C asCollection(Supplier<C> supplier, long[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Long>> C toCollection(C col, long[] arr) {
        if (arr == null) {
            return col;
        }
        for (long item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Short> toList(short[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Short>> C asCollection(Supplier<C> supplier, short[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Short>> C toCollection(C col, short[] arr) {
        if (arr == null) {
            return col;
        }
        for (short item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Byte> toList(byte[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Byte>> C asCollection(Supplier<C> supplier, byte[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Byte>> C toCollection(C col, byte[] arr) {
        if (arr == null) {
            return col;
        }
        for (byte item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Character> toList(char[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Character>> C asCollection(Supplier<C> supplier, char[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Character>> C toCollection(C col, char[] arr) {
        if (arr == null) {
            return col;
        }
        for (char item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Double> toList(double[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Double>> C asCollection(Supplier<C> supplier, double[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Double>> C toCollection(C col, double[] arr) {
        if (arr == null) {
            return col;
        }
        for (double item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Float> toList(float[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Float>> C asCollection(Supplier<C> supplier, float[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Float>> C toCollection(C col, float[] arr) {
        if (arr == null) {
            return col;
        }
        for (float item : arr) {
            col.add(item);
        }
        return col;
    }

    public static List<Boolean> toList(boolean[] arr) {
        return asCollection(ArrayList::new, arr);
    }

    public static <C extends Collection<Boolean>> C asCollection(Supplier<C> supplier, boolean[] arr) {
        return toCollection(supplier.get(), arr);
    }

    public static <C extends Collection<Boolean>> C toCollection(C col, boolean[] arr) {
        if (arr == null) {
            return col;
        }
        for (boolean item : arr) {
            col.add(item);
        }
        return col;
    }
}
