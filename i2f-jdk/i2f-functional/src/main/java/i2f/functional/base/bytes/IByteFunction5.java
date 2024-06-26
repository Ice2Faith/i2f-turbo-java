package i2f.functional.base.bytes;

import i2f.functional.base.IByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IByteFunction5<V1, V2, V3, V4, V5> extends IByteFunction {
    byte apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);

    static <V1, V2, V3, V4, V5> IByteFunction5<V1, V2, V3, V4, V5> of(IByteFunction5<V1, V2, V3, V4, V5> ret) {
        return ret;
    }
}
