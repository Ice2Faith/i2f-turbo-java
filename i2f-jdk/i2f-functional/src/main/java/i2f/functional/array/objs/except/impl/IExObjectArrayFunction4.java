package i2f.functional.array.objs.except.impl;

import i2f.functional.array.objs.except.IExObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IExObjectArrayFunction4<T, V1, V2, V3, V4> extends IExObjectArrayFunction {
    T[] apply(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <T, V1, V2, V3, V4> IExObjectArrayFunction4<T, V1, V2, V3, V4> of(IExObjectArrayFunction4<T, V1, V2, V3, V4> ret) {
        return ret;
    }
}
