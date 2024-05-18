package i2f.array;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/23 14:46
 * @desc
 */
public class ArrayUtil {
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

    public static <T> T[] arrayGetRegion(Object arr, int index, int length, Class<T> elemType) {
        int size = arrayLength(arr);
        int len = size - index;
        if (length > 0) {
            len = Math.min(len, length);
        }
        T[] ret = (T[]) Array.newInstance(elemType, len);
        int i = 0;
        while (i < size && i != length) {
            ret[i] = (T) Array.get(arr, i + index);
            i++;
        }
        return ret;
    }

    public static void arraySet(Object arr, int index, Object value) {
        Array.set(arr, index, value);
    }

    public static <T> void arraySetRegion(Object arr, int index, T[] values, int offset, int length) {
        int size = arrayLength(arr);
        int i = 0;
        while (i + index < size && i + offset < values.length && i != length) {
            Array.set(arr, index + i, values[offset + i]);
            i++;
        }
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

    public static <T> T[] newArray(Class<T> elemType, int len) {
        return (T[]) Array.newInstance(elemType, len);
    }

    public static <T> T[] ofArray(Iterator<T> iterator,
                                  Class<T> elemType) {
        return ofArray(iterator, elemType, null, e -> e);
    }

    public static <T> T[] ofArray(Iterator<T> iterator,
                                  Class<T> elemType,
                                  Predicate<T> filter) {
        return ofArray(iterator, elemType, filter, e -> e);
    }

    public static <T, R> R[] ofArray(Iterator<T> iterator,
                                     Class<R> elemType,
                                     Function<T, R> mapper) {
        return ofArray(iterator, elemType, null, mapper);
    }

    public static <T, R> R[] ofArray(Iterator<T> iterator,
                                     Class<R> elemType,
                                     Predicate<T> filter,
                                     Function<T, R> mapper) {
        LinkedList<R> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            list.addLast(mapper.apply(elem));
            len++;
        }
        R[] ret = (R[]) Array.newInstance(elemType, len);
        for (int i = 0; i < len; i++) {
            R elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> T[] ofArray(T[] arr,
                                  Class<T> elemType) {
        return ofArray(arr, elemType, null, e -> e);
    }

    public static <T> T[] ofArray(T[] arr,
                                  Class<T> elemType,
                                  Predicate<T> filter) {
        return ofArray(arr, elemType, filter, e -> e);
    }

    public static <T, R> R[] ofArray(T[] arr,
                                     Class<R> elemType,
                                     Function<T, R> mapper) {
        return ofArray(arr, elemType, null, mapper);
    }

    public static <T, R> R[] ofArray(T[] arr,
                                     Class<R> elemType,
                                     Predicate<T> filter,
                                     Function<T, R> mapper) {
        LinkedList<R> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            list.addLast(mapper.apply(elem));
            len++;
        }
        R[] ret = (R[]) Array.newInstance(elemType, len);
        for (int i = 0; i < len; i++) {
            R elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> int[] ofIntArray(Iterator<T> iterator,
                                       Function<T, Integer> mapper) {
        return ofIntArray(iterator, null, mapper);
    }

    public static <T> int[] ofIntArray(Iterator<T> iterator,
                                       Predicate<T> filter,
                                       Function<T, Integer> mapper) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Integer val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> int[] ofIntArray(T[] arr,
                                       Function<T, Integer> mapper) {
        return ofIntArray(arr, null, mapper);
    }

    public static <T> int[] ofIntArray(T[] arr,
                                       Predicate<T> filter,
                                       Function<T, Integer> mapper) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Integer val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> long[] ofLongArray(Iterator<T> iterator,
                                         Function<T, Long> mapper) {
        return ofLongArray(iterator, null, mapper);
    }

    public static <T> long[] ofLongArray(Iterator<T> iterator,
                                         Predicate<T> filter,
                                         Function<T, Long> mapper) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Long val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> long[] ofLongArray(T[] arr,
                                         Function<T, Long> mapper) {
        return ofLongArray(arr, null, mapper);
    }

    public static <T> long[] ofLongArray(T[] arr,
                                         Predicate<T> filter,
                                         Function<T, Long> mapper) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Long val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> short[] ofShortArray(Iterator<T> iterator,
                                           Function<T, Short> mapper) {
        return ofShortArray(iterator, null, mapper);
    }

    public static <T> short[] ofShortArray(Iterator<T> iterator,
                                           Predicate<T> filter,
                                           Function<T, Short> mapper) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Short val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> short[] ofShortArray(T[] arr,
                                           Function<T, Short> mapper) {
        return ofShortArray(arr, null, mapper);
    }

    public static <T> short[] ofShortArray(T[] arr,
                                           Predicate<T> filter,
                                           Function<T, Short> mapper) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Short val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> char[] ofCharArray(Iterator<T> iterator,
                                         Function<T, Character> mapper) {
        return ofCharArray(iterator, null, mapper);
    }

    public static <T> char[] ofCharArray(Iterator<T> iterator,
                                         Predicate<T> filter,
                                         Function<T, Character> mapper) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Character val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> char[] ofCharArray(T[] arr,
                                         Function<T, Character> mapper) {
        return ofCharArray(arr, null, mapper);
    }

    public static <T> char[] ofCharArray(T[] arr,
                                         Predicate<T> filter,
                                         Function<T, Character> mapper) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Character val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> byte[] ofByteArray(Iterator<T> iterator,
                                         Function<T, Byte> mapper) {
        return ofByteArray(iterator, null, mapper);
    }

    public static <T> byte[] ofByteArray(Iterator<T> iterator,
                                         Predicate<T> filter,
                                         Function<T, Byte> mapper) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Byte val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> byte[] ofByteArray(T[] arr,
                                         Function<T, Byte> mapper) {
        return ofByteArray(arr, null, mapper);
    }

    public static <T> byte[] ofByteArray(T[] arr,
                                         Predicate<T> filter,
                                         Function<T, Byte> mapper) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Byte val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> float[] ofFloatArray(Iterator<T> iterator,
                                           Function<T, Float> mapper) {
        return ofFloatArray(iterator, null, mapper);
    }

    public static <T> float[] ofFloatArray(Iterator<T> iterator,
                                           Predicate<T> filter,
                                           Function<T, Float> mapper) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Float val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> float[] ofFloatArray(T[] arr,
                                           Function<T, Float> mapper) {
        return ofFloatArray(arr, null, mapper);
    }

    public static <T> float[] ofFloatArray(T[] arr,
                                           Predicate<T> filter,
                                           Function<T, Float> mapper) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Float val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> double[] ofDoubleArray(Iterator<T> iterator,
                                             Function<T, Double> mapper) {
        return ofDoubleArray(iterator, null, mapper);
    }

    public static <T> double[] ofDoubleArray(Iterator<T> iterator,
                                             Predicate<T> filter,
                                             Function<T, Double> mapper) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Double val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> double[] ofDoubleArray(T[] arr,
                                             Function<T, Double> mapper) {
        return ofDoubleArray(arr, null, mapper);
    }

    public static <T> double[] ofDoubleArray(T[] arr,
                                             Predicate<T> filter,
                                             Function<T, Double> mapper) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Double val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }


    public static <T> boolean[] ofBooleanArray(Iterator<T> iterator,
                                               Function<T, Boolean> mapper) {
        return ofBooleanArray(iterator, null, mapper);
    }

    public static <T> boolean[] ofBooleanArray(Iterator<T> iterator,
                                               Predicate<T> filter,
                                               Function<T, Boolean> mapper) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Boolean val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> boolean[] ofBooleanArray(T[] arr,
                                               Function<T, Boolean> mapper) {
        return ofBooleanArray(arr, null, mapper);
    }

    public static <T> boolean[] ofBooleanArray(T[] arr,
                                               Predicate<T> filter,
                                               Function<T, Boolean> mapper) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            T elem = arr[i];
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            Boolean val = mapper.apply(elem);
            if (val == null) {
                continue;
            }
            list.addLast(val);
            len++;
        }
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> void fill(T[] arr, T value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(long[] arr, long value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(short[] arr, short value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(char[] arr, char value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(byte[] arr, byte value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(boolean[] arr, boolean value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(float[] arr, float value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }

    public static void fill(double[] arr, double value) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
    }


    public static <T> void reset(T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
    }

    public static <T> void fill(T[] arr, Supplier<T> generator) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = generator.get();
        }
    }
}
