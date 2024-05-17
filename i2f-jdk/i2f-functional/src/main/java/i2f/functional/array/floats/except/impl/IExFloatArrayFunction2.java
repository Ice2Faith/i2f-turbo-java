package i2f.functional.array.floats.except.impl;

import i2f.functional.array.floats.except.IExFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IExFloatArrayFunction2<V1, V2> extends IExFloatArrayFunction {
    float[] apply(V1 v1, V2 v2) throws Throwable;
    static<V1, V2> IExFloatArrayFunction2<V1, V2> of(IExFloatArrayFunction2<V1, V2> ret){
        return ret;
    }
}
