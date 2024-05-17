package i2f.functional.base.shorts;

import i2f.functional.base.IShortFunction;
import i2f.functional.comparator.except.impl.IExComparator0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IShortFunction0 extends IShortFunction {
    short apply();
    static IShortFunction0 of(IShortFunction0 ret){
        return ret;
    }
}
