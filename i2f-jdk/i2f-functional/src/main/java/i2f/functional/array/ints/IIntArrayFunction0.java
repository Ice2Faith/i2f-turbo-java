package i2f.functional.array.ints;

import i2f.functional.array.IIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IIntArrayFunction0 extends IIntArrayFunction {
    int[] apply();

    static IIntArrayFunction0 of(IIntArrayFunction0 ret) {
        return ret;
    }
}
