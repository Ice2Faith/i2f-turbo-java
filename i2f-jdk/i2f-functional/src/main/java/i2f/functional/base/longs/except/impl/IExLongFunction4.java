package i2f.functional.base.longs.except.impl;

import i2f.functional.base.longs.except.IExLongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface IExLongFunction4<V1, V2, V3, V4> extends IExLongFunction {
    long apply(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <V1, V2, V3, V4> IExLongFunction4<V1, V2, V3, V4> of(IExLongFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
