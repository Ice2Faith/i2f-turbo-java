package i2f.lru;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * LRU 性质的List包装器
 * 提供touch方法，此方法执行后将会把对应的元素放到开头
 * 以便于后续访问时，减少遍历的次数，以提高性能
 * 因此，此列表的元素位置是不固定的
 * 一般用于读多写少的场景
 * 是一种用于系统内部性能自调节的组件
 *
 * @author Ice2Faith
 * @date 2025/5/13 11:24
 */
public class LruList<E> implements List<E> {
    protected final Lock lock = new ReentrantLock();
    protected final LinkedList<E> delegate = new LinkedList<>();

    public LruList() {

    }

    public LruList(E... elems) {
        this.delegate.addAll(Arrays.asList(elems));
    }


    public LruList(Iterable<E> iterable) {
        for (E item : iterable) {
            delegate.add(item);
        }
    }

    public LruList(Iterator<E> iterator) {
        while (iterator.hasNext()) {
            delegate.add(iterator.next());
        }
    }

    public LruList(Enumeration<E> enumeration) {
        while (enumeration.hasMoreElements()) {
            delegate.add(enumeration.nextElement());
        }
    }

    public static <E> LruList<E> of(E... elems) {
        return new LruList<>(elems);
    }

    public static <E> LruList<E> of(Iterable<E> iterable) {
        return new LruList<>(iterable);
    }

    public static <E> LruList<E> of(Iterator<E> iterator) {
        return new LruList<>(iterator);
    }

    public static <E> LruList<E> of(Enumeration<E> enumeration) {
        return new LruList<>(enumeration);
    }

    public E touch(E val) {
        lock.lock();
        try {
            if (!delegate.isEmpty()) {
                E head = delegate.get(0);
                if (head == val) {
                    return head;
                }
            }
            boolean find = false;
            Iterator<E> iterator = delegate.iterator();
            while (iterator.hasNext()) {
                E elem = iterator.next();
                if (elem == val) {
                    find = true;
                    iterator.remove();
                    break;
                }
            }
            if (find) {
                delegate.addFirst(val);
            }
            return val;
        } finally {
            lock.unlock();
        }
    }

    public E touch(int index) {
        lock.lock();
        try {
            if (index == 0) {
                if (!delegate.isEmpty()) {
                    E head = delegate.get(0);
                    return head;
                }
            }
            E ret = delegate.remove(index);
            delegate.addFirst(ret);
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public E touchFirst(Predicate<E> predicate) {
        lock.lock();
        try {
            AtomicReference<E> ref = null;
            Iterator<E> iterator = delegate.iterator();
            while (iterator.hasNext()) {
                E val = iterator.next();
                if (predicate.test(val)) {
                    ref = new AtomicReference<>(val);
                    iterator.remove();
                    break;
                }
            }
            if (ref != null) {
                delegate.add(0, ref.get());
            }
            if (ref == null) {
                return null;
            }
            return ref.get();
        } finally {
            lock.unlock();
        }
    }

    public void touchIf(Predicate<E> predicate) {
        lock.lock();
        try {
            List<E> list = new ArrayList<>();
            Iterator<E> iterator = delegate.iterator();
            while (iterator.hasNext()) {
                E val = iterator.next();
                if (predicate.test(val)) {
                    list.add(val);
                    iterator.remove();
                }
            }
            for (E item : list) {
                delegate.add(0, item);
            }
        } finally {
            lock.unlock();
        }
    }

    public E touchDelegate(Function<List<E>, E> extractor) {
        lock.lock();
        try {
            E elem = extractor.apply(delegate);
            if (elem != null) {
                touch(elem);
            }
            return elem;
        } finally {
            lock.unlock();
        }
    }

    public void sync(Consumer<List<E>> consumer) {
        lock.lock();
        try {
            consumer.accept(delegate);
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return delegate.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return delegate.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.lock();
        try {
            boolean ok = delegate.contains(o);
            return ok;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.lock();
        try {
            return new SyncWrappedIterator<>(delegate.iterator(), lock);
        } finally {
            lock.unlock();
        }
    }

    public static class SyncWrappedIterator<E> implements Iterator<E> {
        protected Iterator<E> delegate;
        protected Lock lock = new ReentrantLock();

        public SyncWrappedIterator(Iterator<E> delegate) {
            this.delegate = delegate;
        }

        public SyncWrappedIterator(Iterator<E> delegate, Lock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        @Override
        public boolean hasNext() {
            lock.lock();
            try {
                return delegate.hasNext();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public E next() {
            lock.lock();
            try {
                return delegate.next();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void remove() {
            lock.lock();
            try {
                delegate.remove();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            lock.lock();
            try {
                delegate.forEachRemaining(action);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Object[] toArray() {
        lock.lock();
        try {
            return delegate.toArray();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.lock();
        try {
            return delegate.toArray(a);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.lock();
        try {
            return delegate.add(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        try {
            return delegate.remove(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.lock();
        try {
            return delegate.containsAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.lock();
        try {
            return delegate.addAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        lock.lock();
        try {
            return delegate.addAll(index, c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.lock();
        try {
            return delegate.removeAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.lock();
        try {
            return delegate.retainAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            delegate.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.lock();
        try {
            return delegate.equals(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.lock();
        try {
            return delegate.hashCode();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.lock();
        try {
            return delegate.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        lock.lock();
        try {
            return delegate.set(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.lock();
        try {
            delegate.add(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.lock();
        try {
            return delegate.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.lock();
        try {
            int ret = delegate.indexOf(o);
            return ret;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.lock();
        try {
            return delegate.lastIndexOf(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.lock();
        try {
            return new SyncWrappedListIterator<>(delegate.listIterator(), lock);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.lock();
        try {
            return new SyncWrappedListIterator<>(delegate.listIterator(index), lock);
        } finally {
            lock.unlock();
        }
    }

    public static class SyncWrappedListIterator<E> implements ListIterator<E> {
        protected ListIterator<E> delegate;
        protected Lock lock = new ReentrantLock();

        public SyncWrappedListIterator(ListIterator<E> delegate) {
            this.delegate = delegate;
        }

        public SyncWrappedListIterator(ListIterator<E> delegate, Lock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        @Override
        public boolean hasNext() {
            lock.lock();
            try {
                return delegate.hasNext();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public E next() {
            lock.lock();
            try {
                return delegate.next();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean hasPrevious() {
            lock.lock();
            try {
                return delegate.hasPrevious();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public E previous() {
            lock.lock();
            try {
                return delegate.previous();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int nextIndex() {
            lock.lock();
            try {
                return delegate.nextIndex();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int previousIndex() {
            lock.lock();
            try {
                return delegate.previousIndex();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void remove() {
            lock.lock();
            try {
                delegate.remove();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void set(E e) {
            lock.lock();
            try {
                delegate.set(e);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void add(E e) {
            lock.lock();
            try {
                delegate.add(e);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            lock.lock();
            try {
                delegate.forEachRemaining(action);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.lock();
        try {
            return delegate.subList(fromIndex, toIndex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        lock.lock();
        try {
            delegate.replaceAll(operator);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        lock.lock();
        try {
            delegate.sort(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.lock();
        try {
            return delegate.spliterator();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.lock();
        try {
            return delegate.removeIf(filter);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.lock();
        try {
            return delegate.stream();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.lock();
        try {
            return delegate.parallelStream();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.lock();
        try {
            delegate.forEach(action);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public String toString() {
        lock.lock();
        try {
            return delegate.toString();
        } finally {
            lock.unlock();
        }
    }

}
