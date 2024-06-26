package i2f.lru.cache;

import i2f.cache.impl.container.ObjectMapCache;
import i2f.lru.LruMap;

/**
 * @author Ice2Faith
 * @date 2024/6/26 11:35
 * @desc
 */
public class LruMapObjectCache<K> extends ObjectMapCache<K> {

    public LruMapObjectCache() {
        super(new LruMap<>());
    }

    public LruMapObjectCache(int maxSize) {
        super(new LruMap<>(maxSize));
    }

    public LruMapObjectCache(LruMap<K, Object> map) {
        super(map);
    }
}
