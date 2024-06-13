package i2f.functional.array.objs.except.impl;

import i2f.functional.array.objs.except.IExObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IExObjectArrayFunction1<T, V1> extends IExObjectArrayFunction {
    T[] apply(V1 v1) throws Throwable;

    static <T, V1> IExObjectArrayFunction1<T, V1> of(IExObjectArrayFunction1<T, V1> ret) {
        return ret;
    }
}
