package i2f.functional.array.floats;

import i2f.functional.array.IFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IFloatArrayFunction1<V1> extends IFloatArrayFunction {
    float[] apply(V1 v1);
    static<V1>  IFloatArrayFunction1<V1>  of(IFloatArrayFunction1<V1>  ret){
        return ret;
    }
}
