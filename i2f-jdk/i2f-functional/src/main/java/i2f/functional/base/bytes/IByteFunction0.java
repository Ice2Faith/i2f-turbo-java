package i2f.functional.base.bytes;

import i2f.functional.base.IByteFunction;
import i2f.functional.base.chars.except.impl.IExCharFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IByteFunction0 extends IByteFunction {
    byte apply();
    static IByteFunction0 of(IByteFunction0 ret){
        return ret;
    }
}
