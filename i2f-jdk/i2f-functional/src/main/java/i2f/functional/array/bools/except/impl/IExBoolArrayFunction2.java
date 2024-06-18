package i2f.functional.array.bools.except.impl;

import i2f.functional.array.bools.except.IExBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IExBoolArrayFunction2<V1, V2> extends IExBoolArrayFunction {
    boolean[] apply(V1 v1, V2 v2) throws Throwable;

    static <V1, V2> IExBoolArrayFunction2<V1, V2> of(IExBoolArrayFunction2<V1, V2> ret) {
        return ret;
    }
}
