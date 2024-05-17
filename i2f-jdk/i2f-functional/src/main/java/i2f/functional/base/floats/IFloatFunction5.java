package i2f.functional.base.floats;

import i2f.functional.base.IFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IFloatFunction5<V1, V2, V3, V4, V5> extends IFloatFunction {
    float apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);
    static<V1, V2, V3, V4, V5> IFloatFunction5<V1, V2, V3, V4, V5> of(IFloatFunction5<V1, V2, V3, V4, V5> ret){
        return ret;
    }
}
