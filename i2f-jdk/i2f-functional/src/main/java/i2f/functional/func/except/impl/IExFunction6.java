package i2f.functional.func.except.impl;

import i2f.functional.func.except.IExFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IExFunction6<R, V1, V2, V3, V4, V5, V6> extends IExFunction {
    R apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) throws Throwable;

    static <R, V1, V2, V3, V4, V5, V6> IExFunction6<R, V1, V2, V3, V4, V5, V6> of(IExFunction6<R, V1, V2, V3, V4, V5, V6> ret) {
        return ret;
    }
}
