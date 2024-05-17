package i2f.functional.array.shorts;

import i2f.functional.array.IShortArrayFunction;
import i2f.functional.base.bytes.except.impl.IExByteFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IShortArrayFunction0 extends IShortArrayFunction {
    short[] apply();
    static IShortArrayFunction0 of(IShortArrayFunction0 ret){
        return ret;
    }
}
