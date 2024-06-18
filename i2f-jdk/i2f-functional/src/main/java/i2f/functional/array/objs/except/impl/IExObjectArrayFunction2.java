package i2f.functional.array.objs.except.impl;

import i2f.functional.array.objs.except.IExObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IExObjectArrayFunction2<T, V1, V2> extends IExObjectArrayFunction {
    T[] apply(V1 v1, V2 v2) throws Throwable;

    static <T, V1, V2> IExObjectArrayFunction2<T, V1, V2> of(IExObjectArrayFunction2<T, V1, V2> ret) {
        return ret;
    }
}
