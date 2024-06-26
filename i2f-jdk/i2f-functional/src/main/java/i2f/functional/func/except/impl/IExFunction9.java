package i2f.functional.func.except.impl;

import i2f.functional.func.except.IExFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IExFunction9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> extends IExFunction {
    R apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) throws Throwable;

    static <R, V1, V2, V3, V4, V5, V6, V7, V8, V9> IExFunction9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> of(IExFunction9<R, V1, V2, V3, V4, V5, V6, V7, V8, V9> ret) {
        return ret;
    }
}
