package i2f.functional.array.chars.except.impl;

import i2f.functional.array.chars.ICharArrayFunction0;
import i2f.functional.array.chars.except.IExCharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface IExCharArrayFunction0 extends IExCharArrayFunction {
    char[] apply() throws Throwable;
    static IExCharArrayFunction0 of(IExCharArrayFunction0 ret){
        return ret;
    }
}
