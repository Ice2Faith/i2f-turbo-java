package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer5<V1, V2, V3, V4, V5> extends IConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);

    static <V1, V2, V3, V4, V5> IConsumer5<V1, V2, V3, V4, V5> of(IConsumer5<V1, V2, V3, V4, V5> ret) {
        return ret;
    }
}
