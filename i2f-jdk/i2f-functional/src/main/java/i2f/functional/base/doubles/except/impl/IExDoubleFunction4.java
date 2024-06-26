package i2f.functional.base.doubles.except.impl;

import i2f.functional.base.doubles.except.IExDoubleFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:27
 * @desc
 */
@FunctionalInterface
public interface IExDoubleFunction4<V1, V2, V3, V4> extends IExDoubleFunction {
    double apply(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <V1, V2, V3, V4> IExDoubleFunction4<V1, V2, V3, V4> of(IExDoubleFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
