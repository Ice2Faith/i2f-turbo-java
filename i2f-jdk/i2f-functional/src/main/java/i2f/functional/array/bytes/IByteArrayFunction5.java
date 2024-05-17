package i2f.functional.array.bytes;

import i2f.functional.array.IByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IByteArrayFunction5<V1, V2, V3, V4, V5> extends IByteArrayFunction {
    byte[] apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);
    static<V1, V2, V3, V4, V5> IByteArrayFunction5<V1, V2, V3, V4, V5> of(IByteArrayFunction5<V1, V2, V3, V4, V5> ret){
        return ret;
    }
}
