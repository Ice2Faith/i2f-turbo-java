package i2f.container.readonly.adapter;

import com.sun.xml.internal.stream.util.ReadOnlyIterator;
import i2f.container.readonly.exception.ReadonlyException;
import i2f.container.sync.adapter.SyncIteratorAdapter;
import i2f.container.sync.adapter.SyncSpliteratorAdapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:40
 * @desc
 */
public class ReadonlyCollectionAdapter<E> implements Collection<E> {
    private Collection<E> collection;

    public ReadonlyCollectionAdapter(Collection<E> collection) {
        this.collection = collection;
    }


    @Override
    public int size() {

        return collection.size();

    }

    @Override
    public boolean isEmpty() {

        return collection.isEmpty();

    }

    @Override
    public boolean contains(Object o) {

        return collection.contains(o);

    }

    @Override
    public Iterator<E> iterator() {

        return new ReadOnlyIterator(collection.iterator());

    }

    @Override
    public Object[] toArray() {

        return collection.toArray();

    }

    @Override
    public <T> T[] toArray(T[] a) {

        return collection.toArray(a);

    }

    @Override
    public boolean add(E e) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public boolean remove(Object o) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return collection.containsAll(c);

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public void clear() {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new ReadonlyException("collection are readonly.");
    }

    @Override
    public Spliterator<E> spliterator() {

        return collection.spliterator();

    }

    @Override
    public Stream<E> stream() {

        return collection.stream();

    }

    @Override
    public Stream<E> parallelStream() {

        return collection.parallelStream();

    }

    @Override
    public void forEach(Consumer<? super E> action) {

        collection.forEach(action);

    }
}
