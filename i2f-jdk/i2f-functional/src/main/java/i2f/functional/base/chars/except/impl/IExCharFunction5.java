package i2f.functional.base.chars.except.impl;

import i2f.functional.base.chars.except.IExCharFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface IExCharFunction5<V1, V2, V3, V4, V5> extends IExCharFunction {
    char apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) throws Throwable;

    static <V1, V2, V3, V4, V5> IExCharFunction5<V1, V2, V3, V4, V5> of(IExCharFunction5<V1, V2, V3, V4, V5> ret) {
        return ret;
    }
}
