package i2f.functional.adapt;

import i2f.functional.array.bools.except.impl.IExBoolArrayFunction0;
import i2f.functional.func.impl.IFunction2;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:29
 * @desc
 */
@FunctionalInterface
public interface IBuilder<R, T, V> extends IFunction2<R, T, V> {
    static <R, T, V> IBuilder<R, T, V> of(IBuilder<R, T, V> ret){
        return ret;
    }
}
