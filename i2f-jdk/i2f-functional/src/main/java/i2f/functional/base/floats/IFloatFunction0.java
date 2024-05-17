package i2f.functional.base.floats;

import i2f.functional.base.IFloatFunction;
import i2f.functional.base.longs.except.impl.IExLongFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IFloatFunction0 extends IFloatFunction {
    float apply();
    static IFloatFunction0 of(IFloatFunction0 ret){
        return ret;
    }
}
