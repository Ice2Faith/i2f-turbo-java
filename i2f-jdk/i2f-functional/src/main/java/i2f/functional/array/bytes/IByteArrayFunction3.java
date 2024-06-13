package i2f.functional.array.bytes;

import i2f.functional.array.IByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IByteArrayFunction3<V1, V2, V3> extends IByteArrayFunction {
    byte[] apply(V1 v1, V2 v2, V3 v3);

    static <V1, V2, V3> IByteArrayFunction3<V1, V2, V3> of(IByteArrayFunction3<V1, V2, V3> ret) {
        return ret;
    }
}
