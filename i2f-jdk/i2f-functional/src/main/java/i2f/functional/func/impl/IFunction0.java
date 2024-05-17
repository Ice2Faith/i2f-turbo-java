package i2f.functional.func.impl;

import i2f.functional.func.IFunction;
import i2f.functional.predicate.except.impl.IExPredicate0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IFunction0<R> extends IFunction {
    R apply();
    static<R> IFunction0<R> of(IFunction0<R> ret){
        return ret;
    }
}
