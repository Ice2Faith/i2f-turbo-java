package i2f.functional.array.bytes.except.impl;

import i2f.functional.array.bytes.except.IExByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IExByteArrayFunction1<V1> extends IExByteArrayFunction {
    byte[] apply(V1 v1) throws Throwable;
    static<V1> IExByteArrayFunction1<V1> of(IExByteArrayFunction1<V1> ret){
        return ret;
    }
}
