package i2f.functional.array.bytes.except.impl;

import i2f.functional.array.bytes.IByteArrayFunction0;
import i2f.functional.array.bytes.except.IExByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IExByteArrayFunction0 extends IExByteArrayFunction {
    byte[] apply() throws Throwable;
    static IExByteArrayFunction0 of(IExByteArrayFunction0 ret){
        return ret;
    }
}
