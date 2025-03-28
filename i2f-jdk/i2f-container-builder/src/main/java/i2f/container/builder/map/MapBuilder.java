package i2f.container.builder.map;


import i2f.typeof.token.TypeToken;

import java.util.*;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/4/24 10:41
 * @desc
 */
public class MapBuilder<K, V, M extends Map<K, V>> implements Supplier<M> {
    protected M map;

    public MapBuilder(M map) {
        this.map = map;
    }

    public MapBuilder(M map, Class<K> keyType, Class<V> valueType) {
        this.map = map;
    }

    public MapBuilder(M map, TypeToken<K> keyType, TypeToken<V> valueType) {
        this.map = map;
    }

    public static <K,V,M extends Map<K,V>> MapBuilder<K, V, M> of(M map){
        return new MapBuilder<>(map);
    }

    public static <K,V,M extends Map<K,V>> MapBuilder<K, V, M> of(M map, Class<K> keyType, Class<V> valueType){
        return new MapBuilder<>(map,keyType,valueType);
    }

    public static <K,V,M extends Map<K,V>> MapBuilder<K, V, M> of(M map, TypeToken<K> keyType, TypeToken<V> valueType){
        return new MapBuilder<>(map,keyType,valueType);
    }

    public static<K,V> MapBuilder<K,V,Map<K,V>> ofHashMap(Class<K> keyType,Class<V> valueType){
        return new MapBuilder<>(new HashMap<>(),keyType,valueType);
    }

    public static<K,V> MapBuilder<K,V,Map<K,V>> ofLinkedHashMap(Class<K> keyType,Class<V> valueType){
        return new MapBuilder<>(new LinkedHashMap<>(),keyType,valueType);
    }

    public static<K,V> MapBuilder<K,V,Map<K,V>> ofTreeMap(Class<K> keyType,Class<V> valueType){
        return new MapBuilder<>(new TreeMap<>(),keyType,valueType);
    }

    public static MapBuilder<String,Object,Map<String,Object>> ofObjectMap(Map<String,Object> map){
        return new MapBuilder<>(map);
    }

    public static MapBuilder<String,Object,Map<String,Object>> ofObjectMap(){
        return new MapBuilder<>(new LinkedHashMap<>(),String.class,Object.class);
    }

    @Override
    public M get() {
        return map;
    }

    public <R> R getAs(Function<M, R> converter) {
        return converter.apply(map);
    }

    public MapBuilder<K, V, M> then(Consumer<M> consumer) {
        consumer.accept(map);
        return this;
    }


    public <U> MapBuilder<K, V, M> set(BiConsumer<M, U> consumer, U val) {
        consumer.accept(map, val);
        return this;
    }

    public <U, R> MapBuilder<K, V, M> apply(BiFunction<M, U, R> function, U val) {
        function.apply(map, val);
        return this;
    }

    public <R> MapBuilder<K, V, M> call(Function<M, R> function) {
        function.apply(map);
        return this;
    }

    public MapBuilder<K, V, M> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V, M> putKeys(V value, Iterable<? extends K> keys) {
        for (K key : keys) {
            map.put(key, value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putKeys(V value, Iterator<? extends K> keys) {
        while (keys.hasNext()) {
            map.put(keys.next(), value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putKeys(V value, K... keys) {
        for (K key : keys) {
            map.put(key, value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterable<? extends K> keys) {
        for (K key : keys) {
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterator<? extends K> keys) {
        while (keys.hasNext()) {
            K key = keys.next();
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, K... keys) {
        for (K key : keys) {
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }


    public <H> MapBuilder<K, V, M> putKeys(V value, Iterable<H> keys, Function<H, K> keyFunction) {
        for (H key : keys) {
            map.put(keyFunction.apply(key), value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putKeys(V value, Iterator<H> keys, Function<H, K> keyFunction) {
        while (keys.hasNext()) {
            map.put(keyFunction.apply(keys.next()), value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putKeys(V value, Function<H, K> keyFunction, H... keys) {
        for (H key : keys) {
            map.put(keyFunction.apply(key), value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterable<H> keys, Function<H, K> keyFunction) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterator<H> keys, Function<H, K> keyFunction) {
        while (keys.hasNext()) {
            K key = keyFunction.apply(keys.next());
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Function<H, K> keyFunction, H... keys) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(V value, Iterable<? extends K> keys) {
        return putKeys(value, keys, Objects::nonNull);
    }

    public MapBuilder<K, V, M> putKeys(V value, Iterable<? extends K> keys, Predicate<K> keyFilter) {
        for (K key : keys) {
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(V value, Iterator<? extends K> keys) {
        return putKeys(value, keys, Objects::nonNull);
    }

    public MapBuilder<K, V, M> putKeys(V value, Iterator<? extends K> keys, Predicate<K> keyFilter) {
        while (keys.hasNext()) {
            K key = keys.next();
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(V value, K... keys) {
        return putKeys(value, Objects::nonNull, keys);
    }

    public MapBuilder<K, V, M> putKeys(V value, Predicate<K> keyFilter, K... keys) {
        for (K key : keys) {
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, Iterable<? extends K> keys) {
        return putKeys(valueFunction, keys, Objects::nonNull);
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterable<? extends K> keys, Predicate<K> keyFilter) {
        for (K key : keys) {
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, Iterator<? extends K> keys) {
        return putKeys(valueFunction, keys, Objects::nonNull);
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterator<? extends K> keys, Predicate<K> keyFilter) {
        while (keys.hasNext()) {
            K key = keys.next();
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, K... keys) {
        return putKeys(valueFunction, Objects::nonNull, keys);
    }

    public MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Predicate<K> keyFilter, K... keys) {
        for (K key : keys) {
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }


    public <H> MapBuilder<K, V, M> putNonNullKeys(V value, Iterable<H> keys, Function<H, K> keyFunction) {
        return putKeys(value, keys, keyFunction, Objects::nonNull);
    }

    public <H> MapBuilder<K, V, M> putKeys(V value, Iterable<H> keys, Function<H, K> keyFunction, Predicate<K> keyFilter) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putNonNullKeys(V value, Iterator<H> keys, Function<H, K> keyFunction) {
        return putKeys(value, keys, keyFunction, Objects::nonNull);
    }

    public <H> MapBuilder<K, V, M> putKeys(V value, Iterator<H> keys, Function<H, K> keyFunction, Predicate<K> keyFilter) {
        while (keys.hasNext()) {
            K key = keyFunction.apply(keys.next());
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putNonNullKeys(V value, Function<H, K> keyFunction, H... keys) {
        return putKeys(value, keyFunction, Objects::nonNull, keys);
    }

    public <H> MapBuilder<K, V, M> putKeys(V value, Function<H, K> keyFunction, Predicate<K> keyFilter, H... keys) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, value);
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, Iterable<H> keys, Function<H, K> keyFunction) {
        return putKeys(valueFunction, keys, keyFunction, Objects::nonNull);
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterable<H> keys, Function<H, K> keyFunction, Predicate<K> keyFilter) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, Iterator<H> keys, Function<H, K> keyFunction) {
        return putKeys(valueFunction, keys, keyFunction, Objects::nonNull);
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Iterator<H> keys, Function<H, K> keyFunction, Predicate<K> keyFilter) {
        while (keys.hasNext()) {
            K key = keyFunction.apply(keys.next());
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public <H> MapBuilder<K, V, M> putNonNullKeys(Function<K, V> valueFunction, Function<H, K> keyFunction, H... keys) {
        return putKeys(valueFunction, keyFunction, Objects::nonNull, keys);
    }

    public <H> MapBuilder<K, V, M> putKeys(Function<K, V> valueFunction, Function<H, K> keyFunction, Predicate<K> keyFilter, H... keys) {
        for (H item : keys) {
            K key = keyFunction.apply(item);
            if (keyFilter != null && !keyFilter.test(key)) {
                continue;
            }
            map.put(key, valueFunction.apply(key));
        }
        return this;
    }

    public MapBuilder<K, V, M> remove(Object key) {
        map.remove(key);
        return this;
    }

    public MapBuilder<K, V, M> putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
        return this;
    }

    public MapBuilder<K, V, M> clear() {
        map.clear();
        return this;
    }

    public MapBuilder<K, V, M> replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
        return this;
    }

    public MapBuilder<K, V, M> putIfAbsent(K key, V value) {
        map.putIfAbsent(key, value);
        return this;
    }


    public MapBuilder<K, V, M> remove(Object key, Object value) {
        map.remove(key, value);
        return this;
    }

    public MapBuilder<K, V, M> replace(K key, V oldValue, V newValue) {
        map.replace(key, oldValue, newValue);
        return this;
    }

    public MapBuilder<K, V, M> replace(K key, V value) {
        map.replace(key, value);
        return this;
    }

    public MapBuilder<K, V, M> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        map.computeIfAbsent(key, mappingFunction);
        return this;
    }

    public MapBuilder<K, V, M> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        map.computeIfPresent(key, remappingFunction);
        return this;
    }

    public MapBuilder<K, V, M> compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        map.compute(key, remappingFunction);
        return this;
    }

    public MapBuilder<K, V, M> merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        map.merge(key, value, remappingFunction);
        return this;
    }
}
