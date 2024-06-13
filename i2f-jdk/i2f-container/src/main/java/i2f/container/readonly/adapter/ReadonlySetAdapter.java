package i2f.container.readonly.adapter;

import i2f.container.readonly.exception.ReadonlyException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:32
 * @desc
 */
public class ReadonlySetAdapter<E> implements Set<E> {
    private Set<E> set;

    public ReadonlySetAdapter(Set<E> set) {
        this.set = set;
    }


    @Override
    public int size() {

        return set.size();

    }

    @Override
    public boolean isEmpty() {

        return set.isEmpty();

    }

    @Override
    public boolean contains(Object o) {

        return set.contains(o);

    }

    @Override
    public Iterator<E> iterator() {

        return new ReadonlyIteratorAdapter<>(set.iterator());

    }

    @Override
    public Object[] toArray() {

        return set.toArray();

    }

    @Override
    public <T> T[] toArray(T[] a) {

        return set.toArray(a);

    }

    @Override
    public boolean add(E e) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public boolean remove(Object o) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return set.containsAll(c);

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public void clear() {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public Spliterator<E> spliterator() {

        return set.spliterator();

    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new ReadonlyException("set are readonly.");
    }

    @Override
    public Stream<E> stream() {

        return set.stream();

    }

    @Override
    public Stream<E> parallelStream() {

        return set.parallelStream();

    }

    @Override
    public void forEach(Consumer<? super E> action) {

        set.forEach(action);

    }
}
