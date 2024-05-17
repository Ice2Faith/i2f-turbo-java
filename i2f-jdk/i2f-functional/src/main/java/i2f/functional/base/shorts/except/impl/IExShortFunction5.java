package i2f.functional.base.shorts.except.impl;

import i2f.functional.base.shorts.except.IExShortFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IExShortFunction5<V1, V2, V3, V4, V5> extends IExShortFunction {
    short apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) throws Throwable;
    static<V1, V2, V3, V4, V5> IExShortFunction5<V1, V2, V3, V4, V5> of(IExShortFunction5<V1, V2, V3, V4, V5> ret){
        return ret;
    }
}
