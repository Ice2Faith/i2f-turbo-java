package i2f.functional.array.ints.except.impl;

import i2f.functional.array.ints.except.IExIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IExIntArrayFunction2<V1, V2> extends IExIntArrayFunction {
    int[] apply(V1 v1, V2 v2) throws Throwable;

    static <V1, V2> IExIntArrayFunction2<V1, V2> of(IExIntArrayFunction2<V1, V2> ret) {
        return ret;
    }
}
