package i2f.functional.base.shorts.except.impl;

import i2f.functional.base.shorts.except.IExShortFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IExShortFunction3<V1, V2, V3> extends IExShortFunction {
    short apply(V1 v1, V2 v2, V3 v3) throws Throwable;
    static<V1, V2, V3> IExShortFunction3<V1, V2, V3> of(IExShortFunction3<V1, V2, V3> ret){
        return ret;
    }
}
