package i2f.lru;

import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2025/8/2 13:44
 */
public class WindowLruMap<T> extends LruMap<String, T> {
    public WindowLruMap() {
    }

    public WindowLruMap(int initialCapacity, float loadFactor, int maxSize) {
        super(initialCapacity, loadFactor, maxSize);
    }

    public WindowLruMap(int initialCapacity, int maxSize) {
        super(initialCapacity, maxSize);
    }

    public WindowLruMap(int maxSize) {
        super(maxSize);
    }

    public WindowLruMap(java.util.Map<? extends String, ? extends T> m, int maxSize) {
        super(m, maxSize);
    }

    public WindowLruMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
        super(initialCapacity, loadFactor, accessOrder, maxSize);
    }

    public T get(String key, Duration duration) {
        lock.lock();
        try {
            return super.get(windowKey(key, duration));
        } finally {
            lock.unlock();
        }
    }

    public T put(String key, Duration duration, T value) {
        lock.lock();
        try {
            return super.put(windowKey(key, duration), value);
        } finally {
            lock.unlock();
        }
    }

    public boolean containsKey(String key, Duration duration) {
        lock.lock();
        try {
            return super.containsKey(windowKey(key, duration));
        } finally {
            lock.unlock();
        }
    }

    public T getOrDefault(String key, Duration duration, T defaultValue) {
        lock.lock();
        try {
            return super.getOrDefault(windowKey(key, duration), defaultValue);
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(String key, Duration duration, Object value) {
        lock.lock();
        try {
            return super.remove(windowKey(key, duration), value);
        } finally {
            lock.unlock();
        }
    }
}
