package i2f.cache.impl.container;

import i2f.cache.container.IObjectContainerCache;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/26 11:44
 * @desc
 */
public class ObjectMapCache<K> extends MapCache<K, Object> implements IObjectContainerCache<K> {
    public ObjectMapCache() {
    }

    public ObjectMapCache(Map<K, Object> map) {
        super(map);
    }
}
