package i2f.cache.container;

import i2f.cache.base.ICache;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/6/25 13:58
 * @desc
 */
public interface IContainerCache<K, V> extends ICache<K, V> {
    Collection<K> keys();

    void clean();

    int size();

    void forEach(Consumer<K> consumer);
}
