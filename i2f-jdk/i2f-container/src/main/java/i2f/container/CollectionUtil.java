package i2f.container;


import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/5/19 21:12
 * @desc
 */
public class CollectionUtil {

    public static <T, C extends Collection<T>> C of(C ret,
                                                    Iterator<T> iterator) {
        return of(ret, iterator, null, e -> e);
    }

    public static <T, C extends Collection<T>> C of(C ret,
                                                    Iterator<T> iterator,
                                                    Predicate<T> filter) {
        return of(ret, iterator, filter, e -> e);
    }

    public static <T, R, C extends Collection<R>> C of(C ret,
                                                       Iterator<T> iterator,
                                                       Function<T, R> mapper) {
        return of(ret, iterator, null, mapper);
    }

    public static <T, R, C extends Collection<R>> C of(C ret,
                                                       Iterator<T> iterator,
                                                       Predicate<T> filter,
                                                       Function<T, R> mapper) {
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <T, C extends Collection<T>> C of(C ret,
                                                    Iterator<T> iterator,
                                                    int offset) {
        return of(ret, iterator, offset, -1, e -> e);
    }

    public static <T, C extends Collection<T>> C of(C ret,
                                                    Iterator<T> iterator,
                                                    int offset,
                                                    int length) {
        return of(ret, iterator, offset, length, e -> e);
    }

    public static <T, R, C extends Collection<R>> C of(C ret,
                                                       Iterator<T> iterator,
                                                       int offset,
                                                       Function<T, R> mapper) {
        return of(ret, iterator, offset, -1, mapper);
    }

    public static <T, R, C extends Collection<R>> C of(C ret,
                                                       Iterator<T> iterator,
                                                       int offset,
                                                       int length,
                                                       Function<T, R> mapper) {
        int endIndex = Integer.MAX_VALUE;
        if (length >= 0) {
            endIndex = offset + length;
        }
        int i = 0;
        while (iterator.hasNext()) {
            T elem = iterator.next();
            if (i >= offset && i < endIndex) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
            i++;
        }
        return ret;
    }


    public static <T, C extends Collection<T>> C as(Supplier<C> supplier,
                                                    Iterator<T> iterator) {
        return as(supplier, iterator, null, e -> e);
    }

    public static <T, C extends Collection<T>> C as(Supplier<C> supplier,
                                                    Iterator<T> iterator,
                                                    Predicate<T> filter) {
        return as(supplier, iterator, filter, e -> e);
    }

    public static <T, R, C extends Collection<R>> C as(Supplier<C> supplier,
                                                       Iterator<T> iterator,
                                                       Function<T, R> mapper) {
        return as(supplier, iterator, null, mapper);
    }

    public static <T, R, C extends Collection<R>> C as(Supplier<C> supplier,
                                                       Iterator<T> iterator,
                                                       Predicate<T> filter,
                                                       Function<T, R> mapper) {
        return of(supplier.get(), iterator, filter, mapper);
    }

    public static <T, C extends Collection<T>> C as(Supplier<C> supplier,
                                                    Iterator<T> iterator,
                                                    int offset) {
        return of(supplier.get(), iterator, offset, -1, e -> e);
    }

    public static <T, C extends Collection<T>> C as(Supplier<C> supplier,
                                                    Iterator<T> iterator,
                                                    int offset,
                                                    int length) {
        return of(supplier.get(), iterator, offset, length, e -> e);
    }

    public static <T, R, C extends Collection<R>> C as(Supplier<C> supplier,
                                                       Iterator<T> iterator,
                                                       int offset,
                                                       Function<T, R> mapper) {
        return of(supplier.get(), iterator, offset, -1, mapper);
    }

    public static <T, R, C extends Collection<R>> C as(Supplier<C> supplier,
                                                       Iterator<T> iterator,
                                                       int offset,
                                                       int length,
                                                       Function<T, R> mapper) {
        return of(supplier.get(), iterator, offset, length, mapper);
    }

    public static <T, C extends Collection<T>> C ofArgs(C ret, T... arr) {
        return ofArgs(ret, null, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C ofArgs(C ret, Predicate<T> filter, T... arr) {
        return ofArgs(ret, filter, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArgs(C ret, Function<T, R> mapper, T... arr) {
        return ofArgs(ret, null, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArgs(C ret, Predicate<T> filter, Function<T, R> mapper, T... arr) {
        for (T elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }


    public static <T, C extends Collection<T>> C argsAs(Supplier<C> supplier, T... arr) {
        return ofArgs(supplier.get(), null, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C argsAs(Supplier<C> supplier, Predicate<T> filter, T... arr) {
        return ofArgs(supplier.get(), filter, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C argsAs(Supplier<C> supplier, Function<T, R> mapper, T... arr) {
        return ofArgs(supplier.get(), null, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C argsAs(Supplier<C> supplier, Predicate<T> filter, Function<T, R> mapper, T... arr) {
        return ofArgs(supplier.get(), filter, mapper, arr);
    }


    public static <T, C extends Collection<T>> C ofArray(C ret, T[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C ofArray(C ret, Predicate<T> filter, T[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArray(C ret, Function<T, R> mapper, T[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArray(C ret, Predicate<T> filter, Function<T, R> mapper, T[] arr) {
        for (T elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <C extends Collection<Integer>> C ofArray(C ret, int[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Integer>> C ofArray(C ret, Predicate<Integer> filter, int[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Integer, R> mapper, int[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Integer> filter, Function<Integer, R> mapper, int[] arr) {
        for (int elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <C extends Collection<Long>> C ofArray(C ret, long[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Long>> C ofArray(C ret, Predicate<Long> filter, long[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Long, R> mapper, long[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Long> filter, Function<Long, R> mapper, long[] arr) {
        for (long elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }


    public static <C extends Collection<Short>> C ofArray(C ret, short[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Short>> C ofArray(C ret, Predicate<Short> filter, short[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Short, R> mapper, short[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Short> filter, Function<Short, R> mapper, short[] arr) {
        for (short elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <C extends Collection<Character>> C ofArray(C ret, char[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Character>> C ofArray(C ret, Predicate<Character> filter, char[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Character, R> mapper, char[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Character> filter, Function<Character, R> mapper, char[] arr) {
        for (char elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }


    public static <C extends Collection<Byte>> C ofArray(C ret, byte[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Byte>> C ofArray(C ret, Predicate<Byte> filter, byte[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Byte> filter, Function<Byte, R> mapper, byte[] arr) {
        for (byte elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }


    public static <C extends Collection<Boolean>> C ofArray(C ret, boolean[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Boolean>> C ofArray(C ret, Predicate<Boolean> filter, boolean[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Boolean> filter, Function<Boolean, R> mapper, boolean[] arr) {
        for (boolean elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }


    public static <C extends Collection<Float>> C ofArray(C ret, float[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Float>> C ofArray(C ret, Predicate<Float> filter, float[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Float, R> mapper, float[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Float> filter, Function<Float, R> mapper, float[] arr) {
        for (float elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <C extends Collection<Double>> C ofArray(C ret, double[] arr) {
        return ofArray(ret, null, e -> e, arr);
    }

    public static <C extends Collection<Double>> C ofArray(C ret, Predicate<Double> filter, double[] arr) {
        return ofArray(ret, filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Function<Double, R> mapper, double[] arr) {
        return ofArray(ret, null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, Predicate<Double> filter, Function<Double, R> mapper, double[] arr) {
        for (double elem : arr) {
            if (filter == null || filter.test(elem)) {
                R val = mapper.apply(elem);
                ret.add(val);
            }
        }
        return ret;
    }

    public static <T, C extends Collection<T>> C ofArray(C ret, int index, T[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C ofArray(C ret, int index, int length, T[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArray(C ret, int index, Function<T, R> mapper, T[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<T, R> mapper, T[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Integer>> C ofArray(C ret, int index, int[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Integer>> C ofArray(C ret, int index, int length, int[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Integer, R> mapper, int[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Integer, R> mapper, int[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }

    public static <C extends Collection<Long>> C ofArray(C ret, int index, long[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Long>> C ofArray(C ret, int index, int length, long[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Long, R> mapper, long[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Long, R> mapper, long[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Short>> C ofArray(C ret, int index, short[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Short>> C ofArray(C ret, int index, int length, short[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Short, R> mapper, short[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Short, R> mapper, short[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Character>> C ofArray(C ret, int index, char[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Character>> C ofArray(C ret, int index, int length, char[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Character, R> mapper, char[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Character, R> mapper, char[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Byte>> C ofArray(C ret, int index, byte[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Byte>> C ofArray(C ret, int index, int length, byte[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Byte, R> mapper, byte[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Boolean>> C ofArray(C ret, int index, boolean[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Boolean>> C ofArray(C ret, int index, int length, boolean[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Boolean, R> mapper, boolean[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <C extends Collection<Float>> C ofArray(C ret, int index, float[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Float>> C ofArray(C ret, int index, int length, float[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Float, R> mapper, float[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Float, R> mapper, float[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }

    public static <C extends Collection<Double>> C ofArray(C ret, int index, double[] arr) {
        return ofArray(ret, index, -1, e -> e, arr);
    }

    public static <C extends Collection<Double>> C ofArray(C ret, int index, int length, double[] arr) {
        return ofArray(ret, index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, Function<Double, R> mapper, double[] arr) {
        return ofArray(ret, index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C ofArray(C ret, int index, int length, Function<Double, R> mapper, double[] arr) {
        for (int i = 0; index + i < arr.length && i != length; i++) {
            R val = mapper.apply(arr[i]);
            ret.add(val);
        }
        return ret;
    }


    public static <T, C extends Collection<T>> C arrayAs(Supplier<C> supplier, T[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C arrayAs(Supplier<C> supplier, Predicate<T> filter, T[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<T, R> mapper, T[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<T> filter, Function<T, R> mapper, T[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }

    public static <C extends Collection<Integer>> C arrayAs(Supplier<C> supplier, int[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Integer>> C arrayAs(Supplier<C> supplier, Predicate<Integer> filter, int[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Integer, R> mapper, int[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Integer> filter, Function<Integer, R> mapper, int[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }

    public static <C extends Collection<Long>> C arrayAs(Supplier<C> supplier, long[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Long>> C arrayAs(Supplier<C> supplier, Predicate<Long> filter, long[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Long, R> mapper, long[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Long> filter, Function<Long, R> mapper, long[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }


    public static <C extends Collection<Short>> C arrayAs(Supplier<C> supplier, short[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Short>> C arrayAs(Supplier<C> supplier, Predicate<Short> filter, short[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Short, R> mapper, short[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Short> filter, Function<Short, R> mapper, short[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }

    public static <C extends Collection<Character>> C arrayAs(Supplier<C> supplier, char[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Character>> C arrayAs(Supplier<C> supplier, Predicate<Character> filter, char[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Character, R> mapper, char[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Character> filter, Function<Character, R> mapper, char[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }


    public static <C extends Collection<Byte>> C arrayAs(Supplier<C> supplier, byte[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Byte>> C arrayAs(Supplier<C> supplier, Predicate<Byte> filter, byte[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Byte> filter, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }


    public static <C extends Collection<Boolean>> C arrayAs(Supplier<C> supplier, boolean[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Boolean>> C arrayAs(Supplier<C> supplier, Predicate<Boolean> filter, boolean[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Boolean> filter, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }


    public static <C extends Collection<Float>> C arrayAs(Supplier<C> supplier, float[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Float>> C arrayAs(Supplier<C> supplier, Predicate<Float> filter, float[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Float, R> mapper, float[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Float> filter, Function<Float, R> mapper, float[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }

    public static <C extends Collection<Double>> C arrayAs(Supplier<C> supplier, double[] arr) {
        return ofArray(supplier.get(), null, e -> e, arr);
    }

    public static <C extends Collection<Double>> C arrayAs(Supplier<C> supplier, Predicate<Double> filter, double[] arr) {
        return ofArray(supplier.get(), filter, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Function<Double, R> mapper, double[] arr) {
        return ofArray(supplier.get(), null, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, Predicate<Double> filter, Function<Double, R> mapper, double[] arr) {
        return ofArray(supplier.get(), filter, mapper, arr);
    }

    public static <T, C extends Collection<T>> C arrayAs(Supplier<C> supplier, int index, T[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <T, C extends Collection<T>> C arrayAs(Supplier<C> supplier, int index, int length, T[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <T, R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<T, R> mapper, T[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <T, R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<T, R> mapper, T[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Integer>> C arrayAs(Supplier<C> supplier, int index, int[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Integer>> C arrayAs(Supplier<C> supplier, int index, int length, int[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Integer, R> mapper, int[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Integer, R> mapper, int[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }

    public static <C extends Collection<Long>> C arrayAs(Supplier<C> supplier, int index, long[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Long>> C arrayAs(Supplier<C> supplier, int index, int length, long[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Long, R> mapper, long[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Long, R> mapper, long[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Short>> C arrayAs(Supplier<C> supplier, int index, short[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Short>> C arrayAs(Supplier<C> supplier, int index, int length, short[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Short, R> mapper, short[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Short, R> mapper, short[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Character>> C arrayAs(Supplier<C> supplier, int index, char[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Character>> C arrayAs(Supplier<C> supplier, int index, int length, char[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Character, R> mapper, char[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Character, R> mapper, char[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Byte>> C arrayAs(Supplier<C> supplier, int index, byte[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Byte>> C arrayAs(Supplier<C> supplier, int index, int length, byte[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Byte, R> mapper, byte[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Boolean>> C arrayAs(Supplier<C> supplier, int index, boolean[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Boolean>> C arrayAs(Supplier<C> supplier, int index, int length, boolean[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Boolean, R> mapper, boolean[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }


    public static <C extends Collection<Float>> C arrayAs(Supplier<C> supplier, int index, float[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Float>> C arrayAs(Supplier<C> supplier, int index, int length, float[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Float, R> mapper, float[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Float, R> mapper, float[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }

    public static <C extends Collection<Double>> C arrayAs(Supplier<C> supplier, int index, double[] arr) {
        return ofArray(supplier.get(), index, -1, e -> e, arr);
    }

    public static <C extends Collection<Double>> C arrayAs(Supplier<C> supplier, int index, int length, double[] arr) {
        return ofArray(supplier.get(), index, length, e -> e, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, Function<Double, R> mapper, double[] arr) {
        return ofArray(supplier.get(), index, -1, mapper, arr);
    }

    public static <R, C extends Collection<R>> C arrayAs(Supplier<C> supplier, int index, int length, Function<Double, R> mapper, double[] arr) {
        return ofArray(supplier.get(), index, length, mapper, arr);
    }

}
