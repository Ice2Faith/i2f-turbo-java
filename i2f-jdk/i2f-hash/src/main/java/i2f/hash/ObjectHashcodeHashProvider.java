package i2f.hash;

/**
 * @author Ice2Faith
 * @date 2022/4/27 8:56
 * @desc
 */
public class ObjectHashcodeHashProvider<T> implements IHashProvider<T> {
    @Override
    public long hash(T obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }
}
