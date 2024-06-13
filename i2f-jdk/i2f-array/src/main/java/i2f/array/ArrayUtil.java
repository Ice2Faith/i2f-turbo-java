package i2f.array;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
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

    public static <T> T[] newArray(T[] sample, int len) {
        return (T[]) Array.newInstance(sample.getClass().getComponentType(), len);
    }

    public static <T> T[] ofArray(Iterator<T> iterator) {
        return ofArray(iterator, null, null, e -> e);
    }

    public static <T> T[] ofArray(Iterator<T> iterator, Class<T> elemType) {
        return ofArray(iterator, elemType, null, e -> e);
    }

    public static <T> T[] ofArray(Iterator<T> iterator, Predicate<T> filter) {
        return ofArray(iterator, null, filter, e -> e);
    }

    public static <T> T[] ofArray(Iterator<T> iterator, Class<T> elemType, Predicate<T> filter) {
        return ofArray(iterator, elemType, filter, e -> e);
    }

    public static <T, R> R[] ofArray(Iterator<T> iterator, Function<T, R> mapper) {
        return ofArray(iterator, null, null, mapper);
    }

    public static <T, R> R[] ofArray(Iterator<T> iterator, Class<R> elemType, Function<T, R> mapper) {
        return ofArray(iterator, elemType, null, mapper);
    }

    public static <T, R> R[] ofArray(Iterator<T> iterator, Class<R> elemType, Predicate<T> filter, Function<T, R> mapper) {
        LinkedList<R> list = new LinkedList<>();
        int len = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter != null && !filter.test(elem)) {
                continue;
            }
            R val = mapper.apply(elem);
            if (val != null && elemType == null) {
                elemType = (Class<R>) val.getClass();
            }
            list.addLast(val);
            len++;
        }
        R[] ret = (R[]) Array.newInstance(elemType, len);
        for (int i = 0; i < len; i++) {
            R elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> T[] ofArray(T[] arr) {
        return ofArray(arr, (Class<T>) arr.getClass().getComponentType(), null, e -> e);
    }

    public static <T> T[] ofArray(T[] arr, Class<T> elemType) {
        return ofArray(arr, elemType, null, e -> e);
    }

    public static <T> T[] ofArray(T[] arr, Predicate<T> filter) {
        return ofArray(arr, (Class<T>) arr.getClass().getComponentType(), filter, e -> e);
    }

    public static <T> T[] ofArray(T[] arr, Class<T> elemType, Predicate<T> filter) {
        return ofArray(arr, elemType, filter, e -> e);
    }

    public static <T, R> R[] ofArray(T[] arr, Class<R> elemType, Function<T, R> mapper) {
        return ofArray(arr, elemType, null, mapper);
    }

    public static <T, R> R[] ofArray(T[] arr, Class<R> elemType, Predicate<T> filter, Function<T, R> mapper) {
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

    public static <T> int[] ofIntArray(Iterator<T> iterator, Function<T, Integer> mapper) {
        return ofIntArray(iterator, null, mapper);
    }

    public static <T> int[] ofIntArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Integer> mapper) {
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

    public static <T> int[] ofIntArray(T[] arr, Function<T, Integer> mapper) {
        return ofIntArray(arr, null, mapper);
    }

    public static <T> int[] ofIntArray(T[] arr, Predicate<T> filter, Function<T, Integer> mapper) {
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


    public static <T> long[] ofLongArray(Iterator<T> iterator, Function<T, Long> mapper) {
        return ofLongArray(iterator, null, mapper);
    }

    public static <T> long[] ofLongArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Long> mapper) {
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

    public static <T> long[] ofLongArray(T[] arr, Function<T, Long> mapper) {
        return ofLongArray(arr, null, mapper);
    }

    public static <T> long[] ofLongArray(T[] arr, Predicate<T> filter, Function<T, Long> mapper) {
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


    public static <T> short[] ofShortArray(Iterator<T> iterator, Function<T, Short> mapper) {
        return ofShortArray(iterator, null, mapper);
    }

    public static <T> short[] ofShortArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Short> mapper) {
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

    public static <T> short[] ofShortArray(T[] arr, Function<T, Short> mapper) {
        return ofShortArray(arr, null, mapper);
    }

    public static <T> short[] ofShortArray(T[] arr, Predicate<T> filter, Function<T, Short> mapper) {
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


    public static <T> char[] ofCharArray(Iterator<T> iterator, Function<T, Character> mapper) {
        return ofCharArray(iterator, null, mapper);
    }

    public static <T> char[] ofCharArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Character> mapper) {
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

    public static <T> char[] ofCharArray(T[] arr, Function<T, Character> mapper) {
        return ofCharArray(arr, null, mapper);
    }

    public static <T> char[] ofCharArray(T[] arr, Predicate<T> filter, Function<T, Character> mapper) {
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


    public static <T> byte[] ofByteArray(Iterator<T> iterator, Function<T, Byte> mapper) {
        return ofByteArray(iterator, null, mapper);
    }

    public static <T> byte[] ofByteArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Byte> mapper) {
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

    public static <T> byte[] ofByteArray(T[] arr, Function<T, Byte> mapper) {
        return ofByteArray(arr, null, mapper);
    }

    public static <T> byte[] ofByteArray(T[] arr, Predicate<T> filter, Function<T, Byte> mapper) {
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


    public static <T> float[] ofFloatArray(Iterator<T> iterator, Function<T, Float> mapper) {
        return ofFloatArray(iterator, null, mapper);
    }

    public static <T> float[] ofFloatArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Float> mapper) {
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

    public static <T> float[] ofFloatArray(T[] arr, Function<T, Float> mapper) {
        return ofFloatArray(arr, null, mapper);
    }

    public static <T> float[] ofFloatArray(T[] arr, Predicate<T> filter, Function<T, Float> mapper) {
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


    public static <T> double[] ofDoubleArray(Iterator<T> iterator, Function<T, Double> mapper) {
        return ofDoubleArray(iterator, null, mapper);
    }

    public static <T> double[] ofDoubleArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Double> mapper) {
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

    public static <T> double[] ofDoubleArray(T[] arr, Function<T, Double> mapper) {
        return ofDoubleArray(arr, null, mapper);
    }

    public static <T> double[] ofDoubleArray(T[] arr, Predicate<T> filter, Function<T, Double> mapper) {
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


    public static <T> boolean[] ofBooleanArray(Iterator<T> iterator, Function<T, Boolean> mapper) {
        return ofBooleanArray(iterator, null, mapper);
    }

    public static <T> boolean[] ofBooleanArray(Iterator<T> iterator, Predicate<T> filter, Function<T, Boolean> mapper) {
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

    public static <T> boolean[] ofBooleanArray(T[] arr, Function<T, Boolean> mapper) {
        return ofBooleanArray(arr, null, mapper);
    }

    public static <T> boolean[] ofBooleanArray(T[] arr, Predicate<T> filter, Function<T, Boolean> mapper) {
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

    public static <T> T[] merge(T[]... arrs) {
        return merge(null, e -> e, null, arrs);
    }

    public static <T> T[] merge(Class<T> elemType, T[]... arrs) {
        return merge(null, e -> e, elemType, arrs);
    }

    public static <T> T[] merge(Predicate<T> filter, T[]... arrs) {
        return merge(filter, e -> e, null, arrs);
    }

    public static <T> T[] merge(Predicate<T> filter, Class<T> elemType, T[]... arrs) {
        return merge(filter, e -> e, elemType, arrs);
    }

    public static <T, R> R[] merge(Function<T, R> mapper, Class<R> elemType, T[]... arrs) {
        return merge(null, mapper, elemType, arrs);
    }

    public static <T, R> R[] merge(Predicate<T> filter, Function<T, R> mapper, Class<R> elemType, T[]... arrs) {
        LinkedList<R> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                T elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                R val = mapper.apply(elem);
                if (val != null && elemType == null) {
                    elemType = (Class<R>) val.getClass();
                }
                list.addLast(val);
                len++;
            }
        }

        R[] ret = (R[]) Array.newInstance(elemType, len);
        for (int i = 0; i < len; i++) {
            R elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> int[] mergeInt(Function<T, Integer> mapper, T[]... arrs) {
        return mergeInt(null, mapper, arrs);
    }

    public static <T> int[] mergeInt(Predicate<T> filter, Function<T, Integer> mapper, T[]... arrs) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static int[] mergeInt(int[]... arrs) {
        return mergeInt(null, arrs);
    }

    public static int[] mergeInt(Predicate<Integer> filter, int[]... arrs) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (int[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                int elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> long[] mergeLong(Function<T, Long> mapper, T[]... arrs) {
        return mergeLong(null, mapper, arrs);
    }

    public static <T> long[] mergeLong(Predicate<T> filter, Function<T, Long> mapper, T[]... arrs) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static long[] mergeLong(long[]... arrs) {
        return mergeLong(null, arrs);
    }

    public static long[] mergeLong(Predicate<Long> filter, long[]... arrs) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (long[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                long elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> short[] mergeShort(Function<T, Short> mapper, T[]... arrs) {
        return mergeShort(null, mapper, arrs);
    }

    public static <T> short[] mergeShort(Predicate<T> filter, Function<T, Short> mapper, T[]... arrs) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static short[] mergeShort(short[]... arrs) {
        return mergeShort(null, arrs);
    }

    public static short[] mergeShort(Predicate<Short> filter, short[]... arrs) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (short[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                short elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> char[] mergeChar(Function<T, Character> mapper, T[]... arrs) {
        return mergeChar(null, mapper, arrs);
    }

    public static <T> char[] mergeChar(Predicate<T> filter, Function<T, Character> mapper, T[]... arrs) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static char[] mergeChar(char[]... arrs) {
        return mergeChar(null, arrs);
    }

    public static char[] mergeChar(Predicate<Character> filter, char[]... arrs) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (char[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                char elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> byte[] mergeByte(Function<T, Byte> mapper, T[]... arrs) {
        return mergeByte(null, mapper, arrs);
    }

    public static <T> byte[] mergeByte(Predicate<T> filter, Function<T, Byte> mapper, T[]... arrs) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static byte[] mergeByte(byte[]... arrs) {
        return mergeByte(null, arrs);
    }

    public static byte[] mergeByte(Predicate<Byte> filter, byte[]... arrs) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (byte[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                byte elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> boolean[] mergeBoolean(Function<T, Boolean> mapper, T[]... arrs) {
        return mergeBoolean(null, mapper, arrs);
    }

    public static <T> boolean[] mergeBoolean(Predicate<T> filter, Function<T, Boolean> mapper, T[]... arrs) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static boolean[] mergeBoolean(boolean[]... arrs) {
        return mergeBoolean(null, arrs);
    }

    public static boolean[] mergeBoolean(Predicate<Boolean> filter, boolean[]... arrs) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (boolean[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                boolean elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> float[] mergeFloat(Function<T, Float> mapper, T[]... arrs) {
        return mergeFloat(null, mapper, arrs);
    }

    public static <T> float[] mergeFloat(Predicate<T> filter, Function<T, Float> mapper, T[]... arrs) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static float[] mergeFloat(float[]... arrs) {
        return mergeFloat(null, arrs);
    }

    public static float[] mergeFloat(Predicate<Float> filter, float[]... arrs) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (float[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                float elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> double[] mergeDouble(Function<T, Double> mapper, T[]... arrs) {
        return mergeDouble(null, mapper, arrs);
    }

    public static <T> double[] mergeDouble(Predicate<T> filter, Function<T, Double> mapper, T[]... arrs) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arrs) {
            if (arr == null) {
                continue;
            }
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
        }

        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static double[] mergeDouble(double[]... arrs) {
        return mergeDouble(null, arrs);
    }

    public static double[] mergeDouble(Predicate<Double> filter, double[]... arrs) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (double[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            for (int i = 0; i < arr.length; i++) {
                double elem = arr[i];
                if (filter != null && !filter.test(elem)) {
                    continue;
                }
                list.addLast(elem);
                len++;
            }
        }

        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double elem = list.removeFirst();
            ret[i] = elem;
        }
        return ret;
    }

    public static <T> T[] flat(T[][] arr2) {
        return flat(arr2, null, null, e -> e);
    }

    public static <T> T[] flat(T[][] arr2, Class<T> elemType) {
        return flat(arr2, elemType, null, e -> e);
    }

    public static <T> T[] flat(T[][] arr2, Class<T> elemType, Predicate<T> filter) {
        return flat(arr2, elemType, filter, e -> e);
    }

    public static <T, R> R[] flat(T[][] arr2, Function<T, R> mapper) {
        return flat(arr2, null, null, mapper);
    }

    public static <T, R> R[] flat(T[][] arr2, Class<R> elemType, Function<T, R> mapper) {
        return flat(arr2, elemType, null, mapper);
    }

    public static <T, R> R[] flat(T[][] arr2, Class<R> elemType, Predicate<T> filter, Function<T, R> mapper) {
        LinkedList<R> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    R val = mapper.apply(item);
                    if (val != null && elemType == null) {
                        elemType = (Class<R>) val.getClass();
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        R[] ret = (R[]) Array.newInstance(elemType, len);
        for (int i = 0; i < len; i++) {
            R val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> int[] flatInt(T[][] arr2, Function<T, Integer> mapper) {
        return flatInt(arr2, null, mapper);
    }

    public static <T> int[] flatInt(T[][] arr2, Predicate<T> filter, Function<T, Integer> mapper) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Integer val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static int[] flatInt(int[][] arr2) {
        return flatInt(arr2, null);
    }

    public static int[] flatInt(int[][] arr2, Predicate<Integer> filter) {
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (int[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (int item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            Integer val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }


    public static <T> long[] flatLong(T[][] arr2, Function<T, Long> mapper) {
        return flatLong(arr2, null, mapper);
    }

    public static <T> long[] flatLong(T[][] arr2, Predicate<T> filter, Function<T, Long> mapper) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Long val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static long[] flatLong(long[][] arr2) {
        return flatLong(arr2, null);
    }

    public static long[] flatLong(long[][] arr2, Predicate<Long> filter) {
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (long[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (long item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            Long val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> short[] flatShort(T[][] arr2, Function<T, Short> mapper) {
        return flatShort(arr2, null, mapper);
    }

    public static <T> short[] flatShort(T[][] arr2, Predicate<T> filter, Function<T, Short> mapper) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Short val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static short[] flatShort(short[][] arr2) {
        return flatShort(arr2, null);
    }

    public static short[] flatShort(short[][] arr2, Predicate<Short> filter) {
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (short[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (short item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            Short val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> char[] flatChar(T[][] arr2, Function<T, Character> mapper) {
        return flatChar(arr2, null, mapper);
    }

    public static <T> char[] flatChar(T[][] arr2, Predicate<T> filter, Function<T, Character> mapper) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Character val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static char[] flatChar(char[][] arr2) {
        return flatChar(arr2, null);
    }

    public static char[] flatChar(char[][] arr2, Predicate<Character> filter) {
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (char[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (char item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            Character val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> byte[] flatByte(T[][] arr2, Function<T, Byte> mapper) {
        return flatByte(arr2, null, mapper);
    }

    public static <T> byte[] flatByte(T[][] arr2, Predicate<T> filter, Function<T, Byte> mapper) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Byte val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static byte[] flatByte(byte[][] arr2) {
        return flatByte(arr2, null);
    }

    public static byte[] flatByte(byte[][] arr2, Predicate<Byte> filter) {
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (byte[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (byte item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            Byte val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> boolean[] flatBoolean(T[][] arr2, Function<T, Boolean> mapper) {
        return flatBoolean(arr2, null, mapper);
    }

    public static <T> boolean[] flatBoolean(T[][] arr2, Predicate<T> filter, Function<T, Boolean> mapper) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Boolean val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static boolean[] flatBoolean(boolean[][] arr2) {
        return flatBoolean(arr2, null);
    }

    public static boolean[] flatBoolean(boolean[][] arr2, Predicate<Boolean> filter) {
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (boolean[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (boolean item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            Boolean val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }


    public static <T> float[] flatFloat(T[][] arr2, Function<T, Float> mapper) {
        return flatFloat(arr2, null, mapper);
    }

    public static <T> float[] flatFloat(T[][] arr2, Predicate<T> filter, Function<T, Float> mapper) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Float val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static float[] flatFloat(float[][] arr2) {
        return flatFloat(arr2, null);
    }

    public static float[] flatFloat(float[][] arr2, Predicate<Float> filter) {
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (float[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (float item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            Float val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }


    public static <T> double[] flatDouble(T[][] arr2, Function<T, Double> mapper) {
        return flatDouble(arr2, null, mapper);
    }

    public static <T> double[] flatDouble(T[][] arr2, Predicate<T> filter, Function<T, Double> mapper) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (T[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (T item : arr) {
                if (filter == null || filter.test(item)) {
                    Double val = mapper.apply(item);
                    if (val == null) {
                        continue;
                    }
                    list.addLast(val);
                    len++;
                }
            }
        }
        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static double[] flatDouble(double[][] arr2) {
        return flatDouble(arr2, null);
    }

    public static double[] flatDouble(double[][] arr2, Predicate<Double> filter) {
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (double[] arr : arr2) {
            if (arr == null) {
                continue;
            }
            for (double item : arr) {
                if (filter == null || filter.test(item)) {
                    list.addLast(item);
                    len++;
                }
            }
        }
        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            Double val = list.removeFirst();
            ret[i] = val;
        }
        return ret;
    }

    public static <T> void fill(T[] arr, T value) {
        fill(arr, 0, -1, value);
    }

    public static <T> void fill(T[] arr, int index, T value) {
        fill(arr, index, -1, value);
    }

    public static <T> void fill(T[] arr, int index, int length, T value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(int[] arr, int value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(int[] arr, int index, int value) {
        fill(arr, index, -1, value);
    }

    public static void fill(int[] arr, int index, int length, int value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(long[] arr, long value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(long[] arr, int index, long value) {
        fill(arr, index, -1, value);
    }

    public static void fill(long[] arr, int index, int length, long value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(short[] arr, short value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(short[] arr, int index, short value) {
        fill(arr, index, -1, value);
    }

    public static void fill(short[] arr, int index, int length, short value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(char[] arr, char value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(char[] arr, int index, char value) {
        fill(arr, index, -1, value);
    }

    public static void fill(char[] arr, int index, int length, char value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(byte[] arr, byte value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(byte[] arr, int index, byte value) {
        fill(arr, index, -1, value);
    }

    public static void fill(byte[] arr, int index, int length, byte value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(boolean[] arr, boolean value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(boolean[] arr, int index, boolean value) {
        fill(arr, index, -1, value);
    }

    public static void fill(boolean[] arr, int index, int length, boolean value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(float[] arr, float value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(float[] arr, int index, float value) {
        fill(arr, index, -1, value);
    }

    public static void fill(float[] arr, int index, int length, float value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static void fill(double[] arr, double value) {
        fill(arr, 0, -1, value);
    }

    public static void fill(double[] arr, int index, double value) {
        fill(arr, index, -1, value);
    }

    public static void fill(double[] arr, int index, int length, double value) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = value;
        }
    }

    public static <T> void reset(T[] arr) {
        reset(arr, 0, -1);
    }

    public static <T> void reset(T[] arr, int index) {
        reset(arr, index, -1);
    }

    public static <T> void reset(T[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = null;
        }
    }

    public static void reset(int[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(int[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(int[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = 0;
        }
    }

    public static void reset(long[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(long[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(long[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = 0;
        }
    }

    public static void reset(short[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(short[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(short[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = 0;
        }
    }

    public static void reset(char[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(char[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(char[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = (char) 0;
        }
    }

    public static void reset(byte[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(byte[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(byte[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = (byte) 0;
        }
    }

    public static void reset(boolean[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(boolean[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(boolean[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = false;
        }
    }

    public static void reset(float[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(float[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(float[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = 0;
        }
    }

    public static void reset(double[] arr) {
        reset(arr, 0, -1);
    }

    public static void reset(double[] arr, int index) {
        reset(arr, index, -1);
    }

    public static void reset(double[] arr, int index, int length) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = 0;
        }
    }

    public static <T> void fill(T[] arr, Supplier<T> generator) {
        fill(arr, 0, -1, generator);
    }

    public static <T> void fill(T[] arr, int index, Supplier<T> generator) {
        fill(arr, index, -1, generator);
    }

    public static <T> void fill(T[] arr, int index, int length, Supplier<T> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(int[] arr, Supplier<Integer> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(int[] arr, int index, Supplier<Integer> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(int[] arr, int index, int length, Supplier<Integer> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(long[] arr, Supplier<Long> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(long[] arr, int index, Supplier<Long> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(long[] arr, int index, int length, Supplier<Long> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(short[] arr, Supplier<Short> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(short[] arr, int index, Supplier<Short> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(short[] arr, int index, int length, Supplier<Short> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(char[] arr, Supplier<Character> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(char[] arr, int index, Supplier<Character> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(char[] arr, int index, int length, Supplier<Character> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(byte[] arr, Supplier<Byte> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(byte[] arr, int index, Supplier<Byte> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(byte[] arr, int index, int length, Supplier<Byte> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(boolean[] arr, Supplier<Boolean> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(boolean[] arr, int index, Supplier<Boolean> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(boolean[] arr, int index, int length, Supplier<Boolean> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(float[] arr, Supplier<Float> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(float[] arr, int index, Supplier<Float> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(float[] arr, int index, int length, Supplier<Float> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static void fill(double[] arr, Supplier<Double> generator) {
        fill(arr, 0, -1, generator);
    }

    public static void fill(double[] arr, int index, Supplier<Double> generator) {
        fill(arr, index, -1, generator);
    }

    public static void fill(double[] arr, int index, int length, Supplier<Double> generator) {
        for (int i = 0; i + index < arr.length && i != length; i++) {
            arr[index + i] = generator.get();
        }
    }

    public static <T> T[] copy(T[] arr) {
        return copy(arr, 0, -1, (Class<T>) arr.getClass().getComponentType(), e -> e);
    }

    public static <T> T[] copy(T[] arr, Class<T> elemType) {
        return copy(arr, 0, -1, elemType, e -> e);
    }

    public static <T> T[] copy(T[] arr, int index, int length) {
        return copy(arr, index, length, (Class<T>) arr.getClass().getComponentType(), e -> e);
    }

    public static <T> T[] copy(T[] arr, int index, Class<T> elemType) {
        return copy(arr, index, -1, elemType, e -> e);
    }

    public static <T> T[] copy(T[] arr, int index, int length, Class<T> elemType) {
        return copy(arr, index, length, elemType, e -> e);
    }

    public static <T, R> R[] copy(T[] arr, Class<R> elemType, Function<T, R> mapper) {
        return copy(arr, 0, -1, elemType, mapper);
    }

    public static <T, R> R[] copy(T[] arr, int index, Class<R> elemType, Function<T, R> mapper) {
        return copy(arr, index, -1, elemType, mapper);
    }

    public static <T, R> R[] copy(T[] arr, int index, int length, Class<R> elemType, Function<T, R> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        R[] ret = (R[]) Array.newInstance(elemType, size);
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = mapper.apply(arr[index + i]);
        }
        return ret;
    }

    public static <T> int[] copyInt(T[] arr, Function<T, Integer> mapper) {
        return copyInt(arr, 0, -1, mapper);
    }

    public static <T> int[] copyInt(T[] arr, int index, Function<T, Integer> mapper) {
        return copyInt(arr, index, -1, mapper);
    }

    public static <T> int[] copyInt(T[] arr, int index, int length, Function<T, Integer> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Integer> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Integer val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static int[] copy(int[] arr) {
        return copy(arr, 0, -1);
    }

    public static int[] copy(int[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static int[] copy(int[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        int[] ret = new int[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> long[] copyLong(T[] arr, Function<T, Long> mapper) {
        return copyLong(arr, 0, -1, mapper);
    }

    public static <T> long[] copyLong(T[] arr, int index, Function<T, Long> mapper) {
        return copyLong(arr, index, -1, mapper);
    }

    public static <T> long[] copyLong(T[] arr, int index, int length, Function<T, Long> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Long> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Long val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        long[] ret = new long[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static long[] copy(long[] arr) {
        return copy(arr, 0, -1);
    }

    public static long[] copy(long[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static long[] copy(long[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        long[] ret = new long[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> short[] copyShort(T[] arr, Function<T, Short> mapper) {
        return copyShort(arr, 0, -1, mapper);
    }

    public static <T> short[] copyShort(T[] arr, int index, Function<T, Short> mapper) {
        return copyShort(arr, index, -1, mapper);
    }

    public static <T> short[] copyShort(T[] arr, int index, int length, Function<T, Short> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Short> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Short val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        short[] ret = new short[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static short[] copy(short[] arr) {
        return copy(arr, 0, -1);
    }

    public static short[] copy(short[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static short[] copy(short[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        short[] ret = new short[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> char[] copyChar(T[] arr, Function<T, Character> mapper) {
        return copyChar(arr, 0, -1, mapper);
    }

    public static <T> char[] copyChar(T[] arr, int index, Function<T, Character> mapper) {
        return copyChar(arr, index, -1, mapper);
    }

    public static <T> char[] copyChar(T[] arr, int index, int length, Function<T, Character> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Character> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Character val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static char[] copy(char[] arr) {
        return copy(arr, 0, -1);
    }

    public static char[] copy(char[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static char[] copy(char[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        char[] ret = new char[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> byte[] copyByte(T[] arr, Function<T, Byte> mapper) {
        return copyByte(arr, 0, -1, mapper);
    }

    public static <T> byte[] copyByte(T[] arr, int index, Function<T, Byte> mapper) {
        return copyByte(arr, index, -1, mapper);
    }

    public static <T> byte[] copyByte(T[] arr, int index, int length, Function<T, Byte> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Byte> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Byte val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static byte[] copy(byte[] arr) {
        return copy(arr, 0, -1);
    }

    public static byte[] copy(byte[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static byte[] copy(byte[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        byte[] ret = new byte[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> boolean[] copyBoolean(T[] arr, Function<T, Boolean> mapper) {
        return copyBoolean(arr, 0, -1, mapper);
    }

    public static <T> boolean[] copyBoolean(T[] arr, int index, Function<T, Boolean> mapper) {
        return copyBoolean(arr, index, -1, mapper);
    }

    public static <T> boolean[] copyBoolean(T[] arr, int index, int length, Function<T, Boolean> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Boolean> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Boolean val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static boolean[] copy(boolean[] arr) {
        return copy(arr, 0, -1);
    }

    public static boolean[] copy(boolean[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static boolean[] copy(boolean[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        boolean[] ret = new boolean[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> float[] copyFloat(T[] arr, Function<T, Float> mapper) {
        return copyFloat(arr, 0, -1, mapper);
    }

    public static <T> float[] copyFloat(T[] arr, int index, Function<T, Float> mapper) {
        return copyFloat(arr, index, -1, mapper);
    }

    public static <T> float[] copyFloat(T[] arr, int index, int length, Function<T, Float> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Float> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Float val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        float[] ret = new float[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static float[] copy(float[] arr) {
        return copy(arr, 0, -1);
    }

    public static float[] copy(float[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static float[] copy(float[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        float[] ret = new float[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> double[] copyDouble(T[] arr, Function<T, Double> mapper) {
        return copyDouble(arr, 0, -1, mapper);
    }

    public static <T> double[] copyDouble(T[] arr, int index, Function<T, Double> mapper) {
        return copyDouble(arr, index, -1, mapper);
    }

    public static <T> double[] copyDouble(T[] arr, int index, int length, Function<T, Double> mapper) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        LinkedList<Double> list = new LinkedList<>();
        int len = 0;
        for (int i = 0; index + i < arr.length && i != length; i++) {
            Double val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            list.add(val);
            len++;
        }
        double[] ret = new double[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.removeFirst();
        }
        return ret;
    }

    public static double[] copy(double[] arr) {
        return copy(arr, 0, -1);
    }

    public static double[] copy(double[] arr, int index) {
        return copy(arr, index, -1);
    }

    public static double[] copy(double[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        double[] ret = new double[size];
        for (int i = 0; index + i < arr.length && i != length; i++) {
            ret[i] = arr[index + i];
        }
        return ret;
    }

    public static <T> T[] copy2(T[] arr, T[] dst) {
        return copy2(arr, 0, -1, dst, 0, e -> e);
    }

    public static <T> T[] copy2(T[] arr, int index, T[] dst) {
        return copy2(arr, index, -1, dst, 0, e -> e);
    }

    public static <T> T[] copy2(T[] arr, int index, int length, T[] dst) {
        return copy2(arr, index, length, dst, 0, e -> e);
    }

    public static <T> T[] copy2(T[] arr, T[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset, e -> e);
    }

    public static <T> T[] copy2(T[] arr, int index, T[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset, e -> e);
    }

    public static <T> T[] copy2(T[] arr, int index, int length, T[] dst, int offset) {
        return copy2(arr, index, length, dst, offset, e -> e);
    }

    public static <T, R> R[] copy2(T[] arr, R[] dst, Function<T, R> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T, R> R[] copy2(T[] arr, int index, R[] dst, Function<T, R> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T, R> R[] copy2(T[] arr, int index, int length, R[] dst, Function<T, R> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T, R> R[] copy2(T[] arr, R[] dst, int offset, Function<T, R> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T, R> R[] copy2(T[] arr, int index, R[] dst, int offset, Function<T, R> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T, R> R[] copy2(T[] arr, int index, int length, R[] dst, int offset, Function<T, R> mapper) {
        for (int i = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[i + offset] = mapper.apply(arr[index + i]);
        }
        return dst;
    }


    public static double[] copy2(double[] arr, double[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static double[] copy2(double[] arr, int index, double[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static double[] copy2(double[] arr, int index, int length, double[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static double[] copy2(double[] arr, double[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static double[] copy2(double[] arr, int index, double[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static double[] copy2(double[] arr, int index, int length, double[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> double[] copy2(T[] arr, double[] dst, Function<T, Double> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> double[] copy2(T[] arr, int index, double[] dst, Function<T, Double> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> double[] copy2(T[] arr, int index, int length, double[] dst, Function<T, Double> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> double[] copy2(T[] arr, double[] dst, int offset, Function<T, Double> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> double[] copy2(T[] arr, int index, double[] dst, int offset, Function<T, Double> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> double[] copy2(T[] arr, int index, int length, double[] dst, int offset, Function<T, Double> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Double val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }


    public static float[] copy2(float[] arr, float[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static float[] copy2(float[] arr, int index, float[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static float[] copy2(float[] arr, int index, int length, float[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static float[] copy2(float[] arr, float[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static float[] copy2(float[] arr, int index, float[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static float[] copy2(float[] arr, int index, int length, float[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> float[] copy2(T[] arr, float[] dst, Function<T, Float> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> float[] copy2(T[] arr, int index, float[] dst, Function<T, Float> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> float[] copy2(T[] arr, int index, int length, float[] dst, Function<T, Float> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> float[] copy2(T[] arr, float[] dst, int offset, Function<T, Float> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> float[] copy2(T[] arr, int index, float[] dst, int offset, Function<T, Float> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> float[] copy2(T[] arr, int index, int length, float[] dst, int offset, Function<T, Float> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Float val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static int[] copy2(int[] arr, int[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static int[] copy2(int[] arr, int index, int[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static int[] copy2(int[] arr, int index, int length, int[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static int[] copy2(int[] arr, int[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static int[] copy2(int[] arr, int index, int[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static int[] copy2(int[] arr, int index, int length, int[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> int[] copy2(T[] arr, int[] dst, Function<T, Integer> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> int[] copy2(T[] arr, int index, int[] dst, Function<T, Integer> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> int[] copy2(T[] arr, int index, int length, int[] dst, Function<T, Integer> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> int[] copy2(T[] arr, int[] dst, int offset, Function<T, Integer> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> int[] copy2(T[] arr, int index, int[] dst, int offset, Function<T, Integer> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> int[] copy2(T[] arr, int index, int length, int[] dst, int offset, Function<T, Integer> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Integer val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static long[] copy2(long[] arr, long[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static long[] copy2(long[] arr, int index, long[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static long[] copy2(long[] arr, int index, int length, long[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static long[] copy2(long[] arr, long[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static long[] copy2(long[] arr, int index, long[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static long[] copy2(long[] arr, int index, int length, long[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> long[] copy2(T[] arr, long[] dst, Function<T, Long> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> long[] copy2(T[] arr, int index, long[] dst, Function<T, Long> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> long[] copy2(T[] arr, int index, int length, long[] dst, Function<T, Long> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> long[] copy2(T[] arr, long[] dst, int offset, Function<T, Long> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> long[] copy2(T[] arr, int index, long[] dst, int offset, Function<T, Long> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> long[] copy2(T[] arr, int index, int length, long[] dst, int offset, Function<T, Long> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Long val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static short[] copy2(short[] arr, short[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static short[] copy2(short[] arr, int index, short[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static short[] copy2(short[] arr, int index, int length, short[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static short[] copy2(short[] arr, short[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static short[] copy2(short[] arr, int index, short[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static short[] copy2(short[] arr, int index, int length, short[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> short[] copy2(T[] arr, short[] dst, Function<T, Short> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> short[] copy2(T[] arr, int index, short[] dst, Function<T, Short> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> short[] copy2(T[] arr, int index, int length, short[] dst, Function<T, Short> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> short[] copy2(T[] arr, short[] dst, int offset, Function<T, Short> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> short[] copy2(T[] arr, int index, short[] dst, int offset, Function<T, Short> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> short[] copy2(T[] arr, int index, int length, short[] dst, int offset, Function<T, Short> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Short val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static char[] copy2(char[] arr, char[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static char[] copy2(char[] arr, int index, char[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static char[] copy2(char[] arr, int index, int length, char[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static char[] copy2(char[] arr, char[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static char[] copy2(char[] arr, int index, char[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static char[] copy2(char[] arr, int index, int length, char[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> char[] copy2(T[] arr, char[] dst, Function<T, Character> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> char[] copy2(T[] arr, int index, char[] dst, Function<T, Character> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> char[] copy2(T[] arr, int index, int length, char[] dst, Function<T, Character> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> char[] copy2(T[] arr, char[] dst, int offset, Function<T, Character> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> char[] copy2(T[] arr, int index, char[] dst, int offset, Function<T, Character> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> char[] copy2(T[] arr, int index, int length, char[] dst, int offset, Function<T, Character> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Character val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static byte[] copy2(byte[] arr, byte[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static byte[] copy2(byte[] arr, int index, byte[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static byte[] copy2(byte[] arr, int index, int length, byte[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static byte[] copy2(byte[] arr, byte[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static byte[] copy2(byte[] arr, int index, byte[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static byte[] copy2(byte[] arr, int index, int length, byte[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> byte[] copy2(T[] arr, byte[] dst, Function<T, Byte> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> byte[] copy2(T[] arr, int index, byte[] dst, Function<T, Byte> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> byte[] copy2(T[] arr, int index, int length, byte[] dst, Function<T, Byte> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> byte[] copy2(T[] arr, byte[] dst, int offset, Function<T, Byte> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> byte[] copy2(T[] arr, int index, byte[] dst, int offset, Function<T, Byte> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> byte[] copy2(T[] arr, int index, int length, byte[] dst, int offset, Function<T, Byte> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Byte val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static boolean[] copy2(boolean[] arr, boolean[] dst) {
        return copy2(arr, 0, -1, dst, 0);
    }

    public static boolean[] copy2(boolean[] arr, int index, boolean[] dst) {
        return copy2(arr, index, -1, dst, 0);
    }

    public static boolean[] copy2(boolean[] arr, int index, int length, boolean[] dst) {
        return copy2(arr, index, length, dst, 0);
    }

    public static boolean[] copy2(boolean[] arr, boolean[] dst, int offset) {
        return copy2(arr, 0, -1, dst, offset);
    }

    public static boolean[] copy2(boolean[] arr, int index, boolean[] dst, int offset) {
        return copy2(arr, index, -1, dst, offset);
    }

    public static boolean[] copy2(boolean[] arr, int index, int length, boolean[] dst, int offset) {
        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            dst[offset + j] = arr[index + i];
            j++;
        }
        return dst;
    }

    public static <T> boolean[] copy2(T[] arr, boolean[] dst, Function<T, Boolean> mapper) {
        return copy2(arr, 0, -1, dst, 0, mapper);
    }

    public static <T> boolean[] copy2(T[] arr, int index, boolean[] dst, Function<T, Boolean> mapper) {
        return copy2(arr, index, -1, dst, 0, mapper);
    }

    public static <T> boolean[] copy2(T[] arr, int index, int length, boolean[] dst, Function<T, Boolean> mapper) {
        return copy2(arr, index, length, dst, 0, mapper);
    }

    public static <T> boolean[] copy2(T[] arr, boolean[] dst, int offset, Function<T, Boolean> mapper) {
        return copy2(arr, 0, -1, dst, offset, mapper);
    }

    public static <T> boolean[] copy2(T[] arr, int index, boolean[] dst, int offset, Function<T, Boolean> mapper) {
        return copy2(arr, index, -1, dst, offset, mapper);
    }

    public static <T> boolean[] copy2(T[] arr, int index, int length, boolean[] dst, int offset, Function<T, Boolean> mapper) {

        for (int i = 0, j = 0; index + i < arr.length && i != length && offset + i < dst.length; i++) {
            Boolean val = mapper.apply(arr[index + i]);
            if (val == null) {
                continue;
            }
            dst[offset + j] = val;
            j++;
        }
        return dst;
    }

    public static <T> T[] reverse(T[] arr) {
        return reverse(arr, 0, -1);
    }

    public static <T> T[] reverse(T[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static <T> T[] reverse(T[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            T tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static int[] reverse(int[] arr) {
        return reverse(arr, 0, -1);
    }

    public static int[] reverse(int[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static int[] reverse(int[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            int tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static long[] reverse(long[] arr) {
        return reverse(arr, 0, -1);
    }

    public static long[] reverse(long[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static long[] reverse(long[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            long tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static short[] reverse(short[] arr) {
        return reverse(arr, 0, -1);
    }

    public static short[] reverse(short[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static short[] reverse(short[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            short tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static char[] reverse(char[] arr) {
        return reverse(arr, 0, -1);
    }

    public static char[] reverse(char[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static char[] reverse(char[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            char tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static byte[] reverse(byte[] arr) {
        return reverse(arr, 0, -1);
    }

    public static byte[] reverse(byte[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static byte[] reverse(byte[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            byte tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static boolean[] reverse(boolean[] arr) {
        return reverse(arr, 0, -1);
    }

    public static boolean[] reverse(boolean[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static boolean[] reverse(boolean[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            boolean tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static float[] reverse(float[] arr) {
        return reverse(arr, 0, -1);
    }

    public static float[] reverse(float[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static float[] reverse(float[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            float tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }

    public static double[] reverse(double[] arr) {
        return reverse(arr, 0, -1);
    }

    public static double[] reverse(double[] arr, int index) {
        return reverse(arr, index, -1);
    }

    public static double[] reverse(double[] arr, int index, int length) {
        int size = arr.length - index;
        if (length > 0) {
            size = Math.min(size, length);
        }
        for (int i = 0; i < size / 2; i++) {
            double tmp = arr[index + i];
            arr[index + i] = arr[index + size - 1 - i];
            arr[index + size - 1 - i] = tmp;
        }
        return arr;
    }


    public static <T> int compare(T[] arr1, T[] arr2, Comparator<T> comparator) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            T v1 = arr1[i];
            T v2 = arr2[i];
            int rs = comparator.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(byte[] arr1, byte[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            byte v1 = arr1[i];
            byte v2 = arr2[i];
            int rs = Byte.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(short[] arr1, short[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            short v1 = arr1[i];
            short v2 = arr2[i];
            int rs = Short.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(char[] arr1, char[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            char v1 = arr1[i];
            char v2 = arr2[i];
            int rs = Character.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(int[] arr1, int[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            int v1 = arr1[i];
            int v2 = arr2[i];
            int rs = Integer.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(long[] arr1, long[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            long v1 = arr1[i];
            long v2 = arr2[i];
            int rs = Long.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(float[] arr1, float[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            float v1 = arr1[i];
            float v2 = arr2[i];
            int rs = Float.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(double[] arr1, double[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            double v1 = arr1[i];
            double v2 = arr2[i];
            int rs = Double.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(boolean[] arr1, boolean[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            boolean v1 = arr1[i];
            boolean v2 = arr2[i];
            int rs = Boolean.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static <T> boolean equal(T[] arr1, T[] arr2) {
        return equal(arr1, arr2, Objects::equals);
    }

    public static <T> boolean equal(T[] arr1, T[] arr2, BiPredicate<T, T> equalizer) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            T v1 = arr1[i];
            T v2 = arr2[i];
            boolean rs = equalizer.test(v1, v2);
            if (!rs) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(int[] arr1, int[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            int v1 = arr1[i];
            int v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(long[] arr1, long[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            long v1 = arr1[i];
            long v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(short[] arr1, short[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            short v1 = arr1[i];
            short v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(char[] arr1, char[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            char v1 = arr1[i];
            char v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(byte[] arr1, byte[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            byte v1 = arr1[i];
            byte v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }

    public static boolean equal(boolean[] arr1, boolean[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null) {
            return false;
        }
        if (arr2 == null) {
            return false;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            boolean v1 = arr1[i];
            boolean v2 = arr2[i];
            if (v1 != v2) {
                return false;
            }
            i++;
        }

        if (i < arr1.length) {
            return false;
        }
        if (i < arr2.length) {
            return false;
        }
        return true;
    }
}
