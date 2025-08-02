package i2f.cache.impl.expire;

import i2f.cache.std.base.ICache;
import i2f.cache.std.expire.IExpireCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/6/26 10:54
 * @desc
 */
public class ExpireCacheWrapper<K, V, T> implements IExpireCache<K, V> {
    protected volatile ICache<K, T> cache;
    protected Function<T, ExpireData<V>> decoder;
    protected Function<ExpireData<V>, T> encoder;
    protected Lock lock = new ReentrantLock();

    public ExpireCacheWrapper(ICache<K, T> cache, Function<T, ExpireData<V>> decoder, Function<ExpireData<V>, T> encoder) {
        this.cache = cache;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    public ExpireCacheWrapper(ICache<K, T> cache, Function<T, ExpireData<V>> decoder, Function<ExpireData<V>, T> encoder, Lock lock) {
        this.cache = cache;
        this.decoder = decoder;
        this.encoder = encoder;
        this.lock = lock;
    }

    protected ExpireData<V> decode(T value) {
        if (value == null) {
            return null;
        }
        return decoder.apply(value);
    }

    protected T encode(ExpireData<V> value) {
        if (value == null) {
            return null;
        }
        return encoder.apply(value);
    }

    protected ExpireData<V> getData(K key) {
        lock.lock();
        try {
            ExpireData<V> data = decode(cache.get(key));
            if (data == null) {
                return null;
            }
            if (data.getExpireTs() >= 0) {
                if (System.currentTimeMillis() > data.getExpireTs()) {
                    cache.remove(key);
                    return null;
                }
            }
            return data;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(K key, V value, long time, TimeUnit timeUnit) {
        lock.lock();
        try {
            long expireTs = timeUnit.toMillis(time) + System.currentTimeMillis();
            cache.set(key, encode(new ExpireData<>(value, expireTs)));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void expire(K key, long time, TimeUnit timeUnit) {
        lock.lock();
        try {
            ExpireData<V> data = getData(key);
            if (data != null) {
                long expireTs = timeUnit.toMillis(time) + System.currentTimeMillis();
                data.setExpireTs(expireTs);
                cache.set(key, encode(data));
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        lock.lock();
        try {
            ExpireData<V> data = getData(key);
            if (data == null) {
                return null;
            }
            long expireTs = data.getExpireTs();
            if (expireTs < 0) {
                return -1L;
            }
            return expireTs - System.currentTimeMillis();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            ExpireData<V> data = getData(key);
            if (data != null) {
                return data.getData();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(K key, V value) {
        lock.lock();
        try {
            cache.set(key, encode(new ExpireData<>(value)));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean exists(K key) {
        lock.lock();
        try {
            return cache.exists(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.lock();
        try {
            cache.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
