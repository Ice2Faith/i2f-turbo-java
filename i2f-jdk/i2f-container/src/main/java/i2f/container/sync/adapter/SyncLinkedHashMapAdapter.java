package i2f.container.sync.adapter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:56
 * @desc
 */
public class SyncLinkedHashMapAdapter<K, V> extends LinkedHashMap<K, V> {
    private LinkedHashMap<K, V> map;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncLinkedHashMapAdapter(LinkedHashMap<K, V> map) {
        this.map = map;
    }

    public SyncLinkedHashMapAdapter(LinkedHashMap<K, V> map, ReadWriteLock lock) {
        this.map = map;
        this.lock = lock;
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock().lock();
        try {
            return map.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        lock.readLock().lock();
        try {
            return map.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            return map.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Collection<V> values() {
        lock.readLock().lock();
        try {
            return map.values();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        lock.readLock().lock();
        try {
            return map.entrySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.readLock().lock();
        try {
            map.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.writeLock().lock();
        try {
            map.replaceAll(function);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return map.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock().lock();
        try {
            map.putAll(m);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.putIfAbsent(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.writeLock().lock();
        try {
            return map.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.writeLock().lock();
        try {
            return map.replace(key, oldValue, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.replace(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        lock.writeLock().lock();
        try {
            return map.computeIfAbsent(key, mappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.computeIfPresent(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.compute(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.merge(key, value, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object clone() {
        lock.readLock().lock();
        try {
            return map.clone();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return map.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return map.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return map.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

}
