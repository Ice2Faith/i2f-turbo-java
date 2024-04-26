package i2f.container.iterable;

import i2f.container.iterable.impl.IteratorIterable;

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
    public static <E> Iterable<E> of(Iterator<E> iterator) {
        return new IteratorIterable<>(iterator);
    }
}
