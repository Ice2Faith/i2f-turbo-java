package i2f.functional.base.bytes;

import i2f.functional.base.IByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IByteFunction4<V1, V2, V3, V4> extends IByteFunction {
    byte apply(V1 v1, V2 v2, V3 v3, V4 v4);

    static <V1, V2, V3, V4> IByteFunction4<V1, V2, V3, V4> of(IByteFunction4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
