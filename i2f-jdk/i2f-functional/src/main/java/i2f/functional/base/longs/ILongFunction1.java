package i2f.functional.base.longs;

import i2f.functional.base.ILongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface ILongFunction1<V1> extends ILongFunction {
    long apply(V1 v1);
    static<V1> ILongFunction1<V1> of(ILongFunction1<V1> ret){
        return ret;
    }
}
