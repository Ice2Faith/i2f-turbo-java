package i2f.cache.impl.container;

import i2f.cache.std.container.IContainerCache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/6/26 10:46
 * @desc
 */
public class MapCache<K, V> implements IContainerCache<K, V> {
    protected volatile Map<K, V> map = new ConcurrentHashMap<>();

    public MapCache() {
    }

    public MapCache(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public Collection<K> keys() {
        return map.keySet();
    }

    @Override
    public void clean() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void forEach(Consumer<K> consumer) {
        map.forEach((k, v) -> {
            consumer.accept(k);
        });
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void set(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean exists(K key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }
}
