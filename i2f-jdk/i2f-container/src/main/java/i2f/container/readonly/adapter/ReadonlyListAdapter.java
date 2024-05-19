package i2f.container.readonly.adapter;

import i2f.container.readonly.exception.ReadonlyException;
import i2f.container.sync.adapter.SyncIteratorAdapter;
import i2f.container.sync.adapter.SyncListIteratorAdapter;
import i2f.container.sync.adapter.SyncSpliteratorAdapter;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:14
 * @desc
 */
public class ReadonlyListAdapter<E> implements List<E> {
    private List<E> list;

    public ReadonlyListAdapter(List<E> list) {
        this.list = list;
    }


    @Override
    public int size() {

        return list.size();

    }

    @Override
    public boolean isEmpty() {

        return list.isEmpty();

    }

    @Override
    public boolean contains(Object o) {

        return list.contains(o);

    }

    @Override
    public Iterator<E> iterator() {

        return new ReadonlyIteratorAdapter<>(list.iterator());

    }

    @Override
    public Object[] toArray() {

        return list.toArray();

    }

    @Override
    public <T> T[] toArray(T[] a) {

        return list.toArray(a);

    }

    @Override
    public boolean add(E e) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public boolean remove(Object o) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return list.containsAll(c);

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public void clear() {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public E get(int index) {

        return list.get(index);

    }

    @Override
    public E set(int index, E element) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public void add(int index, E element) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public E remove(int index) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public int indexOf(Object o) {

        return list.indexOf(o);

    }

    @Override
    public int lastIndexOf(Object o) {

        return list.lastIndexOf(o);

    }

    @Override
    public ListIterator<E> listIterator() {

        return new ReadonlyListIteratorAdapter<>(list.listIterator());

    }

    @Override
    public ListIterator<E> listIterator(int index) {

        return new ReadonlyListIteratorAdapter<>(list.listIterator(index));

    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {

        return list.subList(fromIndex, toIndex);

    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public void sort(Comparator<? super E> c) {

        list.sort(c);

    }

    @Override
    public Spliterator<E> spliterator() {

        return list.spliterator();

    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new ReadonlyException("list are readonly.");
    }

    @Override
    public Stream<E> stream() {

        return list.stream();

    }

    @Override
    public Stream<E> parallelStream() {

        return list.parallelStream();

    }

    @Override
    public void forEach(Consumer<? super E> action) {

        list.forEach(action);

    }
}
