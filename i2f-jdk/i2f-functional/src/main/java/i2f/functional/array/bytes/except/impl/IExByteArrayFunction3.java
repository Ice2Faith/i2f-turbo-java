package i2f.functional.array.bytes.except.impl;

import i2f.functional.array.bytes.except.IExByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IExByteArrayFunction3<V1, V2, V3> extends IExByteArrayFunction {
    byte[] apply(V1 v1, V2 v2, V3 v3) throws Throwable;
    static<V1, V2, V3> IExByteArrayFunction3<V1, V2, V3> of(IExByteArrayFunction3<V1, V2, V3> ret){
        return ret;
    }
}
