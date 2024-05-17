package i2f.functional.array.doubles;

import i2f.functional.array.IDoubleArrayFunction;
import i2f.functional.array.floats.except.impl.IExFloatArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:43
 * @desc
 */
@FunctionalInterface
public interface IDoubleArrayFunction0 extends IDoubleArrayFunction {
    double[] apply();
    static IDoubleArrayFunction0 of(IDoubleArrayFunction0 ret){
        return ret;
    }
}
