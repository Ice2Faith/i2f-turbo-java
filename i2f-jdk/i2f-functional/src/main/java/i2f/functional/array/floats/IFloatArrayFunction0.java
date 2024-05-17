package i2f.functional.array.floats;

import i2f.functional.array.IFloatArrayFunction;
import i2f.functional.array.ints.except.impl.IExIntArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IFloatArrayFunction0 extends IFloatArrayFunction {
    float[] apply();
    static IFloatArrayFunction0 of(IFloatArrayFunction0 ret){
        return ret;
    }
}
