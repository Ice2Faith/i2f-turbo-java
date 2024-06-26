package i2f.functional.array.longs;

import i2f.functional.array.ILongArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface ILongArrayFunction4<V1, V2, V3, V4> extends ILongArrayFunction {
    long[] apply(V1 v1, V2 v2, V3 v3, V4 v4);

    static <V1, V2, V3, V4> ILongArrayFunction4<V1, V2, V3, V4> of(ILongArrayFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
