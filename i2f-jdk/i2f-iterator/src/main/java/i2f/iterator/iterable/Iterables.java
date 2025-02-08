package i2f.iterator.iterable;

import i2f.iterator.iterable.impl.IteratorIterable;
import i2f.iterator.iterator.impl.ArrayIterator;
import i2f.iterator.iterator.impl.EnumerationIterator;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/23 14:06
 * @desc 提供一种直接转换以使用增强for循环的方式
 * for(Object obj : Iterables.of(Iterators.of())){
 * xxx
 * }
 */
public class Iterables {
    public static <E> Iterable<E> of(Enumeration<E> enumeration) {
        return new IteratorIterable<>(new EnumerationIterator<>(enumeration));
    }

    public static <E> Iterable<E> of(Iterator<E> iterator) {
        return new IteratorIterable<>(iterator);
    }

    public static <E> Iterable<E> of(E[] arr) {
        return new IteratorIterable<>(new ArrayIterator<>(arr));
    }
}
