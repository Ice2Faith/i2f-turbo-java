package i2f.functional.base.bytes;

import i2f.functional.base.IByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IByteFunction3<V1, V2, V3> extends IByteFunction {
    byte apply(V1 v1, V2 v2, V3 v3);

    static <V1, V2, V3> IByteFunction3<V1, V2, V3> of(IByteFunction3<V1, V2, V3> ret) {
        return ret;
    }
}
