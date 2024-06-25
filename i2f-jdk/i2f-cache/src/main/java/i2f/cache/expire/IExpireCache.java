package i2f.cache.expire;

import i2f.cache.base.ICache;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/6/25 13:58
 * @desc
 */
public interface IExpireCache<K, V> extends ICache<K, V> {
    void set(K key, V value, long time, TimeUnit timeUnit);

    void expire(K key, long time, TimeUnit timeUnit);

    long getExpire(K key, TimeUnit timeUnit);
}
