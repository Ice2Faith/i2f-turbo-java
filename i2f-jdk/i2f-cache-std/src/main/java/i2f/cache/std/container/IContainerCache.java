package i2f.cache.std.container;

import i2f.cache.std.base.ICache;

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

    default int size() {
        return keys().size();
    }

    default void forEach(Consumer<K> consumer) {
        keys().forEach(consumer);
    }
}
