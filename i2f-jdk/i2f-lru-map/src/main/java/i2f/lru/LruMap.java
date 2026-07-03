package i2f.lru;

import i2f.clock.SystemClock;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:36
 * @desc
 */
public class LruMap<K, V> extends LinkedHashMap<K, V> {
    protected final AtomicInteger maxSize = new AtomicInteger(4096);
    protected final ReentrantLock lock = new ReentrantLock();

    public LruMap() {
    }

    public LruMap(int initialCapacity, float loadFactor, int maxSize) {
        super(initialCapacity, loadFactor);
        this.maxSize.set(maxSize);
    }

    public LruMap(int initialCapacity, int maxSize) {
        super(initialCapacity);
        this.maxSize.set(maxSize);
    }

    public LruMap(int maxSize) {
        this.maxSize.set(maxSize);
    }

    public LruMap(Map<? extends K, ? extends V> m, int maxSize) {
        super(m);
        this.maxSize.set(maxSize);
    }

    public LruMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize.set(maxSize);
    }

    public static String windowKey(Object key, Duration window) {
        return key + ":wk_" + windowKey(window);
    }

    public static String windowKey(Duration window) {
        long range = window.toMillis();
        if (range <= 0) {
            range = 1;
        }
        long windowCount = SystemClock.currentTimeMillis() / range;
        return String.format("%x", windowCount);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize.set(maxSize);
        if (size() > maxSize) {
            shrink();
        }
    }

    protected void shrink() {
        lock.lock();
        try {
            Map<K, V> map = new LinkedHashMap<>(this);
            clear();
            int max = maxSize.get();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (max >= 0) {
                    put(entry.getKey(), entry.getValue());
                } else {
                    break;
                }
                max--;
            }
        } finally {
            lock.unlock();
        }
    }


    public <R> R atomic(Function<LruMap<K, V>, R> wrapper) {
        lock.lock();
        try {
            return wrapper.apply(this);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        lock.lock();
        try {
            return size() > maxSize.get();
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        lock.lock();
        try {
            super.finalize();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.lock();
        try {
            return super.containsValue(value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        lock.lock();
        try {
            return super.getOrDefault(key, defaultValue);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Set<K> keySet() {
        lock.lock();
        try {
            return super.keySet();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<V> values() {
        lock.lock();
        try {
            return super.values();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        lock.lock();
        try {
            return super.entrySet();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.lock();
        try {
            super.forEach(action);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.lock();
        try {
            super.replaceAll(function);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return super.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.lock();
        try {
            super.putAll(m);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        lock.lock();
        try {
            return super.putIfAbsent(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.lock();
        try {
            return super.remove(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.lock();
        try {
            return super.replace(key, oldValue, newValue);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.lock();
        try {
            return super.replace(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        lock.lock();
        try {
            return super.computeIfAbsent(key, mappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.lock();
        try {
            return super.computeIfPresent(key, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.lock();
        try {
            return super.compute(key, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        lock.lock();
        try {
            return super.merge(key, value, remappingFunction);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object clone() {
        lock.lock();
        try {
            return super.clone();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.lock();
        try {
            return super.equals(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.lock();
        try {
            return super.hashCode();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return super.toString();
        } finally {
            lock.unlock();
        }
    }


}
