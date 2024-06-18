package i2f.functional.base.longs;

import i2f.functional.base.ILongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface ILongFunction2<V1, V2> extends ILongFunction {
    long apply(V1 v1, V2 v2);

    static <V1, V2> ILongFunction2<V1, V2> of(ILongFunction2<V1, V2> ret) {
        return ret;
    }
}
