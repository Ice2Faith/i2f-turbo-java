package i2f.functional.array.chars.except.impl;

import i2f.functional.array.chars.except.IExCharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface IExCharArrayFunction4<V1, V2, V3, V4> extends IExCharArrayFunction {
    char[] apply(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <V1, V2, V3, V4> IExCharArrayFunction4<V1, V2, V3, V4> of(IExCharArrayFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
