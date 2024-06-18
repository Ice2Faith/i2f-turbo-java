package i2f.functional.func.impl;

import i2f.functional.func.IFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IFunction3<R, V1, V2, V3> extends IFunction {
    R apply(V1 v1, V2 v2, V3 v3);

    static <R, V1, V2, V3> IFunction3<R, V1, V2, V3> of(IFunction3<R, V1, V2, V3> ret) {
        return ret;
    }
}
