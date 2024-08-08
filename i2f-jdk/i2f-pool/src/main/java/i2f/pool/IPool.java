package i2f.pool;

/**
 * @author Ice2Faith
 * @date 2024/8/8 15:23
 * @desc
 */
public interface IPool<T> {
    T require();

    void release(T obj);
}
