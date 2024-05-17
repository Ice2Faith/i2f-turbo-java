package i2f.functional.base.chars;

import i2f.functional.base.ICharFunction;
import i2f.functional.base.doubles.IDoubleFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface ICharFunction0 extends ICharFunction {
    char apply();
    static ICharFunction0 of(ICharFunction0 ret){
        return ret;
    }
}
