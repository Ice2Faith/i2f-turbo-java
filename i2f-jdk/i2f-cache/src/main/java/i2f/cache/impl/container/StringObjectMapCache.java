package i2f.cache.impl.container;

import i2f.cache.container.IStringObjectContainerCache;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/26 11:44
 * @desc
 */
public class StringObjectMapCache extends MapCache<String, Object> implements IStringObjectContainerCache {
    public StringObjectMapCache() {
    }

    public StringObjectMapCache(Map<String, Object> map) {
        super(map);
    }
}
