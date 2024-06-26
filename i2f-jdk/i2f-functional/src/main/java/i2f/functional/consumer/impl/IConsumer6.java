package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer6<V1, V2, V3, V4, V5, V6> extends IConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6);

    static <V1, V2, V3, V4, V5, V6> IConsumer6<V1, V2, V3, V4, V5, V6> of(IConsumer6<V1, V2, V3, V4, V5, V6> ret) {
        return ret;
    }
}
