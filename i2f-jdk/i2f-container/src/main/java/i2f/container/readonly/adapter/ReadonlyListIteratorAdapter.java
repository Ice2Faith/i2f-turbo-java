package i2f.container.readonly.adapter;

import i2f.container.readonly.exception.ReadonlyException;

import java.util.ListIterator;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:27
 * @desc
 */
public class ReadonlyListIteratorAdapter<E> implements ListIterator<E> {
    private ListIterator<E> iterator;

    public ReadonlyListIteratorAdapter(ListIterator<E> iterator) {
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
    public boolean hasPrevious() {

        return iterator.hasPrevious();

    }

    @Override
    public E previous() {

        return iterator.previous();

    }

    @Override
    public int nextIndex() {

        return iterator.nextIndex();

    }

    @Override
    public int previousIndex() {

        return iterator.previousIndex();

    }

    @Override
    public void remove() {
        throw new ReadonlyException("list iterator are readonly.");
    }

    @Override
    public void set(E e) {
        throw new ReadonlyException("list iterator are readonly.");
    }

    @Override
    public void add(E e) {
        throw new ReadonlyException("list iterator are readonly.");
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {

        iterator.forEachRemaining(action);

    }
}
