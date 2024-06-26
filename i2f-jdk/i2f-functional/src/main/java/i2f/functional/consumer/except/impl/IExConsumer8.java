package i2f.functional.consumer.except.impl;

import i2f.functional.consumer.except.IExConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IExConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> extends IExConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) throws Throwable;

    static <V1, V2, V3, V4, V5, V6, V7, V8> IExConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> of(IExConsumer8<V1, V2, V3, V4, V5, V6, V7, V8> ret) {
        return ret;
    }
}
