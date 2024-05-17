package i2f.functional.base.floats.except.impl;

import i2f.functional.base.floats.IFloatFunction0;
import i2f.functional.base.floats.except.IExFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IExFloatFunction0 extends IExFloatFunction {
    float apply() throws Throwable;
    static IExFloatFunction0 of(IExFloatFunction0 ret){
        return ret;
    }
}
