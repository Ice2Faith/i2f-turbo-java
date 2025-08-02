package i2f.cache.std.ext;

import i2f.cache.std.container.IContainerCache;
import i2f.cache.std.expire.IExpireCache;

/**
 * @author Ice2Faith
 * @date 2024/6/25 14:08
 * @desc
 */
public interface IExpireContainerCache<K, V> extends IContainerCache<K, V>, IExpireCache<K, V> {
}
