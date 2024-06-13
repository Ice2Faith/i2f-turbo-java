package i2f.functional.array.bools.except.impl;

import i2f.functional.array.bools.except.IExBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IExBoolArrayFunction4<V1, V2, V3, V4> extends IExBoolArrayFunction {
    boolean[] apply(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <V1, V2, V3, V4> IExBoolArrayFunction4<V1, V2, V3, V4> of(IExBoolArrayFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
