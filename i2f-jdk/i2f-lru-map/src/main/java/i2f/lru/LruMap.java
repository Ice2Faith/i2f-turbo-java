package i2f.lru;

import i2f.clock.SystemClock;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

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

    public static String windowKey(Object key,Duration window){
        return key+":wk_"+windowKey(window);
    }

    public static String windowKey(Duration window){
        long range = window.toMillis();
        if(range<=0){
            range=1;
        }
        long windowCount = SystemClock.currentTimeMillis() / range;
        return String.format("%x",windowCount);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize.set(maxSize);
        if(size()>maxSize){
            shrink();
        }
    }

    protected void shrink(){
        lock.writeLock().lock();
        try {
            Map<K,V> map=new LinkedHashMap<>(this);
            clear();
            int max=maxSize.get();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if(max>=0){
                    put(entry.getKey(),entry.getValue());
                }else{
                    break;
                }
                max--;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


    public <R> R atomic(Function<LruMap<K, V>, R> wrapper) {
        lock.writeLock().lock();
        try {
            return wrapper.apply(this);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        lock.readLock().lock();
        try {
            return size() > maxSize.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        lock.writeLock().lock();
        try {
            super.finalize();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock().lock();
        try {
            return super.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.readLock().lock();
        try {
            return super.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        lock.readLock().lock();
        try {
            return super.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            super.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            return super.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Collection<V> values() {
        lock.readLock().lock();
        try {
            return super.values();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        lock.readLock().lock();
        try {
            return super.entrySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.readLock().lock();
        try {
            super.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.writeLock().lock();
        try {
            super.replaceAll(function);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return super.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return super.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock().lock();
        try {
            return super.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            return super.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock().lock();
        try {
            super.putAll(m);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.writeLock().lock();
        try {
            return super.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        lock.writeLock().lock();
        try {
            return super.putIfAbsent(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.writeLock().lock();
        try {
            return super.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.writeLock().lock();
        try {
            return super.replace(key, oldValue, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.writeLock().lock();
        try {
            return super.replace(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        lock.writeLock().lock();
        try {
            return super.computeIfAbsent(key, mappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return super.computeIfPresent(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return super.compute(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return super.merge(key, value, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object clone() {
        lock.readLock().lock();
        try {
            return super.clone();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return super.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return super.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return super.toString();
        } finally {
            lock.readLock().unlock();
        }
    }


}
