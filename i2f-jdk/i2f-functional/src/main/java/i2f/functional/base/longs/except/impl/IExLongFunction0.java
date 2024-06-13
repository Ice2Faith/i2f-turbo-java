package i2f.functional.base.longs.except.impl;

import i2f.functional.base.longs.except.IExLongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface IExLongFunction0 extends IExLongFunction {
    long apply() throws Throwable;

    static IExLongFunction0 of(IExLongFunction0 ret) {
        return ret;
    }
}
