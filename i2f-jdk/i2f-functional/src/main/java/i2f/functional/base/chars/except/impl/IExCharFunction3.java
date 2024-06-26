package i2f.functional.base.chars.except.impl;

import i2f.functional.base.chars.except.IExCharFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface IExCharFunction3<V1, V2, V3> extends IExCharFunction {
    char apply(V1 v1, V2 v2, V3 v3) throws Throwable;

    static <V1, V2, V3> IExCharFunction3<V1, V2, V3> of(IExCharFunction3<V1, V2, V3> ret) {
        return ret;
    }
}
