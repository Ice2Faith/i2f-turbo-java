package i2f.check.filter.impl.bigdata.hash;

/**
 * @author Ice2Faith
 * @date 2022/4/27 13:56
 * @desc
 */
public interface IRepeatDecider<T> {
    boolean save(T data, long cnt);
}
