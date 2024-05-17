package i2f.functional.array.ints;

import i2f.functional.array.IIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IIntArrayFunction4<V1, V2, V3, V4> extends IIntArrayFunction {
    int[] apply(V1 v1, V2 v2, V3 v3, V4 v4);
    static<V1, V2, V3, V4> IIntArrayFunction4<V1, V2, V3, V4> of(IIntArrayFunction4<V1, V2, V3, V4> ret){
        return ret;
    }
}
