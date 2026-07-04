package i2f.lru;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2026/7/4 15:13
 * @desc
 */
public class ConcurrentLinkedSet<T> {
    private final AtomicReference<RefValue<Object>> headNode = new AtomicReference<>();
    private final AtomicReference<RefValue<Object>> tailNode = new AtomicReference<>();
    private final ConcurrentHashMap<RefValue<Object>, RefValue<Object>> nextMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<RefValue<Object>, RefValue<Object>> prevMap = new ConcurrentHashMap<>();
    private final AtomicInteger size = new AtomicInteger(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected static class NullValue {
        public static final NullValue INSTANCE = new NullValue();

        private NullValue() {

        }
    }

    protected static class RefValue<T> {
        public static final RefValue<?> EMPTY = new RefValue<>(null);
        protected T value;

        private RefValue(T value) {
            this.value = value;
        }

        public static <T> RefValue<T> of(T value) {
            if (value == null) {
                return empty();
            }
            return new RefValue<>(value);
        }

        @SuppressWarnings("unchecked")
        public static <T> RefValue<T> empty() {
            return (RefValue<T>) EMPTY;
        }

        public T get() {
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RefValue)) {
                return false;
            }
            RefValue<?> refValue = (RefValue<?>) o;
            return Objects.equals(value, refValue.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "RefValue{" +
                    "value=" + value +
                    '}';
        }
    }

    protected static Object wrapValue(Object val) {
        return val == null ? NullValue.INSTANCE : val;
    }

    protected static Object unwrapValue(Object val) {
        return val == NullValue.INSTANCE ? null : val;
    }

    public void addTail(T value) {
        Object innerValue = wrapValue(value);
        RefValue<Object> optionValue = RefValue.of(innerValue);

        lock.writeLock().lock();
        try {
            remove(value);
            RefValue<Object> tail = tailNode.get();
            if (tail == null) {
                headNode.set(optionValue);
                tailNode.set(optionValue);
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);
            } else {
                tailNode.set(optionValue);
                nextMap.put(tail, optionValue);
                nextMap.remove(optionValue);
                prevMap.put(optionValue, tail);
            }
            size.incrementAndGet();
        } finally {
            afterModify();
            lock.writeLock().unlock();
        }
    }

    public void addHead(T value) {
        Object innerValue = wrapValue(value);
        RefValue<Object> optionValue = RefValue.of(innerValue);

        lock.writeLock().lock();
        try {
            remove(value);
            RefValue<Object> head = headNode.get();
            if (head == null) {
                headNode.set(optionValue);
                tailNode.set(optionValue);
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);
            } else {
                headNode.set(optionValue);
                nextMap.put(optionValue, head);
                prevMap.put(head, optionValue);
                prevMap.remove(optionValue);
            }
            size.incrementAndGet();
        } finally {
            afterModify();
            lock.writeLock().unlock();
        }
    }

    public T getHead() {
        lock.readLock().lock();
        try {
            RefValue<Object> head = headNode.get();
            if (head == null) {
                return null;
            }
            return (T) unwrapValue(head.get());
        } finally {
            lock.readLock().unlock();
        }
    }

    public T getTail() {
        lock.readLock().lock();
        try {
            RefValue<Object> tail = tailNode.get();
            if (tail == null) {
                return null;
            }
            return (T) unwrapValue(tail.get());
        } finally {
            lock.readLock().unlock();
        }
    }

    public T getPrevious(T value) {
        Object innerValue = wrapValue(value);
        RefValue<Object> optionValue = RefValue.of(innerValue);

        lock.readLock().lock();
        try {
            RefValue<Object> prev = prevMap.get(optionValue);
            if (prev == null) {
                return null;
            }
            return (T) unwrapValue(prev.get());
        } finally {
            lock.readLock().unlock();
        }
    }

    public T getNext(T value) {
        Object innerValue = wrapValue(value);
        RefValue<Object> optionValue = RefValue.of(innerValue);

        lock.readLock().lock();
        try {
            RefValue<Object> next = nextMap.get(optionValue);
            if (next == null) {
                return null;
            }
            return (T) unwrapValue(next.get());
        } finally {
            lock.readLock().unlock();
        }
    }

    public T removeHead() {
        lock.writeLock().lock();
        try {
            T ret = getHead();
            remove(ret);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public T removeTail() {
        lock.writeLock().lock();
        try {
            T ret = getTail();
            remove(ret);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean contains(T value) {
        lock.readLock().lock();
        try {
            if (headNode.get() == null) {
                return false;
            }
            Object innerValue = wrapValue(value);
            RefValue<Object> optionValue = RefValue.of(innerValue);
            if (Objects.equals(headNode.get(), optionValue)) {
                return true;
            }
            if (nextMap.contains(optionValue) || prevMap.contains(optionValue)) {
                return true;
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean remove(T value) {
        lock.writeLock().lock();
        try {
            boolean ret = contains(value);
            if (!ret) {
                return false;
            }
            // a, a 1, a 1 2, 8 a, 9 8 a, 1 a 2
            Object innerValue = wrapValue(value);
            RefValue<Object> optionValue = RefValue.of(innerValue);

            RefValue<Object> next = nextMap.get(optionValue);
            RefValue<Object> prev = prevMap.get(optionValue);
            if (next == null && prev == null) {
                headNode.set(null);
                tailNode.set(null);
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);
            } else if (next == null) {
                tailNode.set(prev);
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);
                nextMap.remove(prev);
            } else if (prev == null) {
                headNode.set(next);
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);
                prevMap.remove(next);
            } else {
                prevMap.remove(optionValue);
                nextMap.remove(optionValue);

                nextMap.put(prev, next);
                prevMap.put(next, prev);
            }
            size.decrementAndGet();
            return true;
        } finally {
            afterModify();
            lock.writeLock().unlock();
        }
    }

    public boolean moveToHead(T value) {
        lock.writeLock().lock();
        try {
            boolean ret = remove(value);
            addHead(value);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean moveToTail(T value) {
        lock.writeLock().lock();
        try {
            boolean ret = remove(value);
            addTail(value);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void afterModify() {

    }

    protected ReadWriteLock getLock() {
        return lock;
    }

    public void compute(Consumer<ConcurrentLinkedSet<T>> consumer) {
        lock.writeLock().lock();
        try {
            consumer.accept(this);
        } finally {
            afterModify();
            lock.writeLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return headNode.get() == null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return size.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            headNode.set(null);
            tailNode.set(null);
            prevMap.clear();
            nextMap.clear();
            size.set(0);
        } finally {
            afterModify();
            lock.writeLock().unlock();
        }
    }

    public List<T> toList() {
        lock.readLock().lock();
        try {
            List<T> ret = new ArrayList<>();
            RefValue<Object> currValue = headNode.get();
            while (currValue != null) {
                Object item = unwrapValue(currValue.get());
                ret.add((T) item);
                currValue = nextMap.get(currValue);
            }
            return ret;
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<T> toReverseList() {
        lock.readLock().lock();
        try {
            List<T> ret = new ArrayList<>();
            RefValue<Object> currValue = tailNode.get();
            while (currValue != null) {
                Object item = unwrapValue(currValue.get());
                ret.add((T) item);
                currValue = prevMap.get(currValue);
            }
            return ret;
        } finally {
            lock.readLock().unlock();
        }
    }
}
