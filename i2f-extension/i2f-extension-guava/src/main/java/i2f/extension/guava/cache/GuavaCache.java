package i2f.extension.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import i2f.cache.std.container.IContainerCache;
import lombok.Data;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/9/21 14:00
 * @desc
 */
@Data
public class GuavaCache<K, V> implements IContainerCache<K, V> {
    protected static final Object NULL_PLACEHOLDER = new Object();

    protected final Cache<Object, Object> cache;

    public GuavaCache(int capital) {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(Math.min(32, capital))       // 初始容量
                .maximumSize(capital)         // 最大缓存条目数
                .recordStats()             // 开启统计
                .build();
    }

    public GuavaCache(Cache<Object, Object> cache) {
        this.cache = cache;
    }


    public boolean isNullPlaceholder(Object obj) {
        return obj == NULL_PLACEHOLDER;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Object obj) {
        return isNullPlaceholder(obj) ? null : (T) obj;
    }

    public Object wrap(Object obj) {
        return obj == null ? NULL_PLACEHOLDER : obj;
    }

    @Override
    public V get(K key) {
        Object obj = cache.getIfPresent(wrap(key));
        return unwrap(obj);
    }

    @Override
    public void set(K key, V value) {
        cache.put(wrap(key), wrap(value));
    }

    @Override
    public boolean exists(K key) {
        Object obj = cache.getIfPresent(wrap(key));
        return obj != null;
    }

    @Override
    public void remove(K key) {
        cache.invalidate(wrap(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<K> keys() {
        cache.cleanUp();
        return cache.asMap().keySet().stream()
                .map(e -> (K) unwrap(e))
                .collect(Collectors.toList());
    }

    @Override
    public void clean() {
        cache.invalidateAll();
    }
}
