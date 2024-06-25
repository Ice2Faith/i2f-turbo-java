package i2f.cache;

import i2f.cache.container.IContainerCache;
import i2f.cache.expire.IExpireCache;
import i2f.cache.persist.IPersistCache;

/**
 * @author Ice2Faith
 * @date 2024/6/25 14:08
 * @desc
 */
public interface IDefaultCache<K, V> extends IContainerCache<K, V>, IExpireCache<K, V>, IPersistCache<K, V> {
}
