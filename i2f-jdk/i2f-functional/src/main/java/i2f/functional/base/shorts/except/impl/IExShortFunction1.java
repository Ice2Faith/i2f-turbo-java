package i2f.functional.base.shorts.except.impl;

import i2f.functional.base.shorts.except.IExShortFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IExShortFunction1<V1> extends IExShortFunction {
    short apply(V1 v1) throws Throwable;

    static <V1> IExShortFunction1<V1> of(IExShortFunction1<V1> ret) {
        return ret;
    }
}
