package i2f.functional.base.floats;

import i2f.functional.base.IFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IFloatFunction2<V1, V2> extends IFloatFunction {
    float apply(V1 v1, V2 v2);
    static<V1, V2> IFloatFunction2<V1, V2> of(IFloatFunction2<V1, V2> ret){
        return ret;
    }
}
