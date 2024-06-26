package i2f.functional.base.floats;

import i2f.functional.base.IFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IFloatFunction3<V1, V2, V3> extends IFloatFunction {
    float apply(V1 v1, V2 v2, V3 v3);

    static <V1, V2, V3> IFloatFunction3<V1, V2, V3> of(IFloatFunction3<V1, V2, V3> ret) {
        return ret;
    }
}
