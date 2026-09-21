package i2f.extension.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import i2f.cache.std.ext.IExpireContainerCache;
import lombok.Data;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/9/21 11:10
 * @desc
 */
@Data
public class CaffeineExpireCache<K, V> implements IExpireContainerCache<K, V> {
    protected static final Object NULL_PLACEHOLDER = new Object();

    @Data
    public static class CacheEntry<V> {
        private final V value;
        private final long ttlNanos;
        private final long createdAtNanos; // 新增

        public CacheEntry(V value, long duration, TimeUnit unit) {
            this.value = value;
            this.ttlNanos = unit.toNanos(duration);
            this.createdAtNanos = System.nanoTime();
        }
    }

    public static class TtlExpiry<K, V> implements Expiry<K, CacheEntry<V>> {

        @Override
        public long expireAfterCreate(@NonNull K key, @NonNull CacheEntry<V> entry, long currentTime) {
            return entry.getTtlNanos(); // 直接使用包装类中携带的 TTL
        }

        @Override
        public long expireAfterUpdate(@NonNull K key, @NonNull CacheEntry<V> entry, long currentTime, @NonNegative long currentDuration) {
            return entry.getTtlNanos();
        }

        @Override
        public long expireAfterRead(@NonNull K key, @NonNull CacheEntry<V> entry, long currentTime, @NonNegative long currentDuration) {
            return currentDuration; // 读后不刷新
        }
    }

    protected final Cache<Object, CacheEntry<V>> cache;

    public CaffeineExpireCache(int capital) {
        cache = Caffeine.newBuilder()
                .initialCapacity(Math.min(32, capital))
                .maximumSize(capital)
                .expireAfter(new TtlExpiry<Object, V>())
                .build();
    }

    public CaffeineExpireCache(Cache<Object, CacheEntry<V>> cache) {
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

    @SuppressWarnings("unchecked")
    @Override
    public Collection<K> keys() {
        return cache.asMap().keySet().stream()
                .map(e -> (K) unwrap(e))
                .collect(Collectors.toList());
    }

    @Override
    public void clean() {
        cache.invalidateAll();
    }

    @Override
    public void set(K key, V value, long time, TimeUnit timeUnit) {
        cache.put(wrap(key), new CacheEntry<>(value, time, timeUnit));
    }

    @Override
    public void expire(K key, long time, TimeUnit timeUnit) {
        cache.asMap().compute(wrap(key), (k, entry) -> {
            if (entry == null) {
                return null;
            }
            return new CacheEntry<>(entry.getValue(), time, timeUnit);
        });
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        CacheEntry<V> entry = cache.getIfPresent(wrap(key));
        if (entry == null) {
            return null;
        }
        long remaining = entry.getTtlNanos() - (System.nanoTime() - entry.getCreatedAtNanos());
        if (remaining <= 0) {
            return 0L; // 已过期但尚未被清理
        }
        return timeUnit.convert(remaining, TimeUnit.NANOSECONDS);
    }

    @Override
    public V get(K key) {
        CacheEntry<V> entry = cache.getIfPresent(wrap(key));
        return entry != null ? entry.getValue() : null;
    }

    @Override
    public void set(K key, V value) {
        cache.put(wrap(key), new CacheEntry<>(value, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
    }

    @Override
    public boolean exists(K key) {
        CacheEntry<V> entry = cache.getIfPresent(wrap(key));
        return entry != null;
    }

    @Override
    public void remove(K key) {
        cache.invalidate(wrap(key));
    }
}
