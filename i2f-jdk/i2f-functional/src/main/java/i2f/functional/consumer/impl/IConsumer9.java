package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> extends IConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9);

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9> IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> of(IConsumer9<V1, V2, V3, V4, V5, V6, V7, V8, V9> ret) {
        return ret;
    }
}
