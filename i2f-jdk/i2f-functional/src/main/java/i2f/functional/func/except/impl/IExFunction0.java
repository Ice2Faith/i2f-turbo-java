package i2f.functional.func.except.impl;

import i2f.functional.func.except.IExFunction;
import i2f.functional.func.impl.IFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IExFunction0<R> extends IExFunction {
    R apply() throws Throwable;
    static<R> IExFunction0<R> of(IExFunction0<R> ret){
        return ret;
    }
}
