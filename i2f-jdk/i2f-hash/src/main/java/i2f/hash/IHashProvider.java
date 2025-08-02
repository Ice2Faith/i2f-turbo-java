package i2f.hash;

/**
 * @author Ice2Faith
 * @date 2022/4/27 8:51
 * @desc
 */
public interface IHashProvider<T> {
    long hash(T obj);
}
