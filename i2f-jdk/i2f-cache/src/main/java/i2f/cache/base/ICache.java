package i2f.cache.base;

/**
 * @author Ice2Faith
 * @date 2024/6/25 13:58
 * @desc
 */
public interface ICache<K, V> {
    V get(K key);

    void set(K key, V value);

    boolean exists(K key);

    void remove(K key);
}
