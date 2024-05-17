package i2f.functional.base.floats;

import i2f.functional.base.IFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IFloatFunction1<V1> extends IFloatFunction {
    float apply(V1 v1);
    static<V1> IFloatFunction1<V1> of(IFloatFunction1<V1> ret){
        return ret;
    }
}
