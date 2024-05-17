package i2f.functional.array.longs;

import i2f.functional.array.ILongArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface ILongArrayFunction2<V1, V2> extends ILongArrayFunction {
    long[] apply(V1 v1, V2 v2);
    static<V1, V2> ILongArrayFunction2<V1, V2> of(ILongArrayFunction2<V1, V2> ret){
        return ret;
    }
}
