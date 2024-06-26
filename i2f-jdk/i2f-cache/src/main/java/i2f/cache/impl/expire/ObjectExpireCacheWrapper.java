package i2f.cache.impl.expire;

import i2f.cache.base.ICache;

import java.util.concurrent.locks.Lock;

/**
 * @author Ice2Faith
 * @date 2024/6/26 10:54
 * @desc
 */
public class ObjectExpireCacheWrapper<K, V> extends ExpireCacheWrapper<K, V, Object> {
    public ObjectExpireCacheWrapper(ICache<K, Object> cache) {
        super(cache, (obj) -> (ExpireData<V>) obj, (data) -> data);
    }

    public ObjectExpireCacheWrapper(ICache<K, Object> cache, Lock lock) {
        super(cache, (obj) -> (ExpireData<V>) obj, (data) -> data, lock);
    }
}
