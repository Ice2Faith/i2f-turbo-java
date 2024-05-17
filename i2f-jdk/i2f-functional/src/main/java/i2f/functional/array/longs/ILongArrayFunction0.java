package i2f.functional.array.longs;

import i2f.functional.array.ILongArrayFunction;
import i2f.functional.array.objs.IObjectArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface ILongArrayFunction0 extends ILongArrayFunction {
    long[] apply();
    static ILongArrayFunction0 of(ILongArrayFunction0 ret){
        return ret;
    }
}
