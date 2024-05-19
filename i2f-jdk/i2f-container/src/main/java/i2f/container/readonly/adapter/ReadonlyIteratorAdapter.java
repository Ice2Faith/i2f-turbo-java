package i2f.container.readonly.adapter;

import i2f.container.readonly.exception.ReadonlyException;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:16
 * @desc
 */
public class ReadonlyIteratorAdapter<E> implements Iterator<E> {
    private Iterator<E> iterator;

    public ReadonlyIteratorAdapter(Iterator<E> iterator) {
        this.iterator = iterator;
    }


    @Override
    public boolean hasNext() {

        return iterator.hasNext();

    }

    @Override
    public E next() {

        return iterator.next();

    }

    @Override
    public void remove() {
        throw new ReadonlyException("iterator are readonly.");
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {

        iterator.forEachRemaining(action);

    }
}
