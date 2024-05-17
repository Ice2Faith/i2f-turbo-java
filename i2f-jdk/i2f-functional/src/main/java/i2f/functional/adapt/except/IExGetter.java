package i2f.functional.adapt.except;

import i2f.functional.array.bools.except.impl.IExBoolArrayFunction0;
import i2f.functional.func.except.impl.IExFunction1;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:27
 * @desc
 */
@FunctionalInterface
public interface IExGetter<R, T> extends IExFunction1<R, T> {
    static<R, T> IExGetter<R, T> of(IExGetter<R, T> ret){
        return ret;
    }
}
