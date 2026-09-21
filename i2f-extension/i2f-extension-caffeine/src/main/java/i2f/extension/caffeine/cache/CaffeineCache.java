package i2f.extension.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import i2f.cache.std.base.ICache;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2026/9/21 10:54
 * @desc
 */
@Data
public class CaffeineCache<K, V> implements ICache<K, V> {
    protected static final Object NULL_PLACEHOLDER = new Object();

    protected final Cache<Object, Object> cache;

    public CaffeineCache(int capital) {
        cache = Caffeine.newBuilder()
                .initialCapacity(Math.min(32, capital))       // 初始容量
                .maximumSize(capital)         // 最大缓存条目数
                .recordStats()             // 开启统计
                .build();
    }

    public CaffeineCache(Cache<Object, Object> cache) {
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
}
