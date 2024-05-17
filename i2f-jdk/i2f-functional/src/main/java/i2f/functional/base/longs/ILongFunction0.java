package i2f.functional.base.longs;

import i2f.functional.base.ILongFunction;
import i2f.functional.base.shorts.IShortFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface ILongFunction0 extends ILongFunction {
    long apply();
    static ILongFunction0 of(ILongFunction0 ret){
        return ret;
    }
}
