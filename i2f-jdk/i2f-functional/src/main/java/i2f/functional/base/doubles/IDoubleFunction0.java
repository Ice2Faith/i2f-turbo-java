package i2f.functional.base.doubles;

import i2f.functional.base.IDoubleFunction;
import i2f.functional.base.floats.except.impl.IExFloatFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:27
 * @desc
 */
@FunctionalInterface
public interface IDoubleFunction0 extends IDoubleFunction {
    double apply();
    static IDoubleFunction0 of(IDoubleFunction0 ret){
        return ret;
    }
}
