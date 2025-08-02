package i2f.cache.std.expire;

import i2f.cache.std.base.ICache;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/6/25 13:58
 * @desc
 */
public interface IExpireCache<K, V> extends ICache<K, V> {
    default boolean preferSetAndTtl() {
        return true;
    }

    void set(K key, V value, long time, TimeUnit timeUnit);

    void expire(K key, long time, TimeUnit timeUnit);

    Long getExpire(K key, TimeUnit timeUnit);

}
