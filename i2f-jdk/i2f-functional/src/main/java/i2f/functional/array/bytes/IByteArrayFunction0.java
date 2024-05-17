package i2f.functional.array.bytes;

import i2f.functional.array.IByteArrayFunction;
import i2f.functional.array.chars.except.impl.IExCharArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IByteArrayFunction0 extends IByteArrayFunction {
    byte[] apply();
    static IByteArrayFunction0 of(IByteArrayFunction0 ret){
        return ret;
    }
}
