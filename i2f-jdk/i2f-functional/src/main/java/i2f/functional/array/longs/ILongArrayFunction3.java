package i2f.functional.array.longs;

import i2f.functional.array.ILongArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface ILongArrayFunction3<V1, V2, V3> extends ILongArrayFunction {
    long[] apply(V1 v1, V2 v2, V3 v3);
    static<V1, V2, V3> ILongArrayFunction3<V1, V2, V3> of(ILongArrayFunction3<V1, V2, V3> ret){
        return ret;
    }
}
