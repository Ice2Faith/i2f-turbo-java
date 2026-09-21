package i2f.extension.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import i2f.cache.std.ext.IExpireContainerCache;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/9/21 11:10
 * @desc
 *
 * 建议使用 caffeine 代替此实现
 */
@Data
public class GuavaExpireCache<K, V> implements IExpireContainerCache<K, V> {
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

    protected final Cache<Object, CacheEntry<V>> cache;

    protected static final AtomicInteger POOL_GROUP_COUNTER=new AtomicInteger(0);
    protected final ScheduledExecutorService cleanPool= Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread ret = new Thread(r);
            ret.setName("guava-cache-cleanup-"+POOL_GROUP_COUNTER.incrementAndGet());
            ret.setDaemon(true);
            return ret;
        }
    });

    {
        cleanPool.scheduleAtFixedRate(this::cleanerTask,30,30,TimeUnit.SECONDS);
    }

    public GuavaExpireCache(int capital) {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(Math.min(32, capital))
                .maximumSize(capital)
                .build();
    }

    public GuavaExpireCache(Cache<Object, CacheEntry<V>> cache) {
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
        cache.cleanUp();
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

    public CacheEntry<V> getIfPresent(K key) {
        return innerGetIfPresent(wrap(key));
    }

    protected CacheEntry<V> innerGetIfPresent(Object wrappedKey){
        return cache.asMap().compute(wrappedKey, (k, entry) -> {
            if (entry == null) {
                return null;
            }
            long remaining = entry.getTtlNanos() - (System.nanoTime() - entry.getCreatedAtNanos());
            if (remaining <= 0) {
                cache.invalidate(k);
                return null;
            }
            return entry;
        });
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        CacheEntry<V> entry = getIfPresent(key);
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
        CacheEntry<V> entry = getIfPresent(key);
        return entry != null ? entry.getValue() : null;
    }

    @Override
    public void set(K key, V value) {
        cache.put(wrap(key), new CacheEntry<>(value, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
    }

    @Override
    public boolean exists(K key) {
        CacheEntry<V> entry = getIfPresent(key);
        return entry != null;
    }

    @Override
    public void remove(K key) {
        cache.invalidate(wrap(key));
    }

    protected void cleanerTask(){
        try{
            cleanUp();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void cleanUp(){
        cache.cleanUp();
        Set<Object> keys = new HashSet<>(cache.asMap().keySet());
        for (Object key : keys) {
            innerGetIfPresent(key);
        }
    }
}
