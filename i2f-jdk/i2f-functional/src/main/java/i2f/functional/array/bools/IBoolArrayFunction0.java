package i2f.functional.array.bools;

import i2f.functional.array.IBoolArrayFunction;
import i2f.functional.array.bytes.except.impl.IExByteArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IBoolArrayFunction0 extends IBoolArrayFunction {
    boolean[] apply();
    static IBoolArrayFunction0 of(IBoolArrayFunction0 ret){
        return ret;
    }
}
