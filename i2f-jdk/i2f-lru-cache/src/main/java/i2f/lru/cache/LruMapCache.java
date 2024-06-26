package i2f.lru.cache;

import i2f.cache.impl.container.MapCache;
import i2f.lru.LruMap;

/**
 * @author Ice2Faith
 * @date 2024/6/26 11:35
 * @desc
 */
public class LruMapCache<K, V> extends MapCache<K, V> {

    public LruMapCache() {
        super(new LruMap<>());
    }

    public LruMapCache(int maxSize) {
        super(new LruMap<>(maxSize));
    }

    public LruMapCache(LruMap<K, V> map) {
        super(map);
    }
}
