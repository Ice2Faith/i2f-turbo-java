package i2f.lru;

import i2f.clock.SystemClock;

import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2024/7/1 16:46
 * @desc
 */
public class ExpireConcurrentMap<K, V> {
    public static class ExpireData<T> {
        public T data;
        public long expireTs = -1;

        public ExpireData(T data) {
            this.data = data;
        }

        public ExpireData(T data, long expireTs) {
            this.data = data;
            this.expireTs = expireTs;
        }
    }

    private ConcurrentHashMap<K, ExpireData<V>> map = new ConcurrentHashMap<>();
    private static AtomicInteger threadCount = new AtomicInteger();

    private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "cache-expire-" + threadCount.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }
    });

    {
        pool.scheduleWithFixedDelay(() -> {
            refreshExpires();
        }, 30, 30, TimeUnit.SECONDS);
    }

    public void refreshExpires() {
        CopyOnWriteArrayList<K> removeKeys = new CopyOnWriteArrayList<>();
        map.forEach((k, v) -> {
            if (v.expireTs >= 0) {
                if (v.expireTs < SystemClock.currentTimeMillis()) {
                    removeKeys.add(k);
                }
            }
        });
        for (K k : removeKeys) {
            map.remove(k);
        }
    }

    public ConcurrentHashMap<K, ExpireData<V>> getMap() {
        return map;
    }

    public void set(K key, V value) {
        map.put(key, new ExpireData<>(value));
    }

    public void set(K key, V value, long time, TimeUnit timeUnit) {
        map.put(key, new ExpireData<>(value, SystemClock.currentTimeMillis() + timeUnit.toMillis(time)));
    }

    public V get(K key) {
        ExpireData<V> data = map.get(key);
        if (data == null) {
            return null;
        }
        if (data.expireTs > SystemClock.currentTimeMillis()) {
            return null;
        }
        return data.data;
    }

    public void remove(K key) {
        map.remove(key);
    }

    public boolean exists(K key) {
        ExpireData<V> data = map.get(key);
        if (data == null) {
            return false;
        }
        if (data.expireTs > SystemClock.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    public void clean() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public int validSize() {
        refreshExpires();
        return map.size();
    }

    public Set<K> keys() {
        return map.keySet();
    }

    public void expire(K key, long time, TimeUnit timeUnit) {
        map.computeIfPresent(key, (k, v) -> {
            v.expireTs = SystemClock.currentTimeMillis() + timeUnit.toMillis(time);
            return v;
        });
    }

    public Long getExpire(K key, TimeUnit timeUnit) {
        ExpireData<V> data = map.get(key);
        if (data == null) {
            return null;
        }
        if (data.expireTs > SystemClock.currentTimeMillis()) {
            return null;
        }
        return data.expireTs;
    }
}
