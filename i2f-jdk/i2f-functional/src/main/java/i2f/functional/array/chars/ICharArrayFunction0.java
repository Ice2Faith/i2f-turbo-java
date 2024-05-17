package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;
import i2f.functional.array.doubles.except.impl.IExDoubleArrayFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction0 extends ICharArrayFunction {
    char[] apply();
    static ICharArrayFunction0 of(ICharArrayFunction0 ret){
        return ret;
    }
}
