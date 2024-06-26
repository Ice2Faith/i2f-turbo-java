package i2f.lru.cache;

import i2f.cache.impl.container.StringObjectMapCache;
import i2f.lru.LruMap;

/**
 * @author Ice2Faith
 * @date 2024/6/26 11:35
 * @desc
 */
public class LruMapStringObjectCache extends StringObjectMapCache {

    public LruMapStringObjectCache() {
        super(new LruMap<>());
    }

    public LruMapStringObjectCache(int maxSize) {
        super(new LruMap<>(maxSize));
    }

    public LruMapStringObjectCache(LruMap<String, Object> map) {
        super(map);
    }
}
