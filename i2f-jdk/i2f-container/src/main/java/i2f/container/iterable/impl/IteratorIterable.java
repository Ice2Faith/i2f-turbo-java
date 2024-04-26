package i2f.container.iterable.impl;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/23 14:07
 * @desc
 */
public class IteratorIterable<E> implements Iterable<E> {
    protected Iterator<E> iterator;

    public IteratorIterable(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }
}
