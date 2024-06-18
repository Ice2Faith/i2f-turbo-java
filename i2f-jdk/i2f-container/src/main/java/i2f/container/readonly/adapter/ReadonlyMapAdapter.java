package i2f.container.readonly.adapter;

import i2f.container.readonly.exception.ReadonlyException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/18 13:56
 * @desc
 */
public class ReadonlyMapAdapter<K, V> implements Map<K, V> {

    public static <K, V> Map<K, V> of(Map<K, V> map) {
        if (map instanceof ReadonlyMapAdapter) {
            return map;
        }
        return new ReadonlyMapAdapter<K, V>(map);
    }

    public static <K, V> Map<K, V> hashMap() {
        return of(new HashMap<>());
    }

    public static <K, V> Map<K, V> treeMap() {
        return of(new TreeMap<>());
    }

    public static <K, V> Map<K, V> linkedHashMap() {
        return of(new LinkedHashMap<>());
    }

    public static <K, V> Map<K, V> lruMap(int maxSize) {
        return of(new LinkedHashMap<K, V>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxSize;
            }
        });
    }

    private Map<K, V> map;

    public ReadonlyMapAdapter(Map<K, V> map) {
        this.map = map;
    }


    @Override
    public int size() {

        return map.size();

    }

    @Override
    public boolean isEmpty() {

        return map.isEmpty();

    }

    @Override
    public boolean containsKey(Object key) {

        return map.containsKey(key);

    }

    @Override
    public boolean containsValue(Object value) {

        return map.containsValue(value);

    }

    @Override
    public V get(Object key) {

        return map.get(key);

    }

    @Override
    public V put(K key, V value) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V remove(Object key) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public void clear() {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public Set<K> keySet() {

        return map.keySet();

    }

    @Override
    public Collection<V> values() {

        return map.values();

    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        return map.entrySet();

    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {

        return map.getOrDefault(key, defaultValue);

    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

        map.forEach(action);

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V replace(K key, V value) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new ReadonlyException("map are readonly.");
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new ReadonlyException("map are readonly.");
    }
}
