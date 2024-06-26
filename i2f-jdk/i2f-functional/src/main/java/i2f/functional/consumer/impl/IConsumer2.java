package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer2<V1, V2> extends IConsumer {
    void accept(V1 v1, V2 v2);

    static <V1, V2> IConsumer2<V1, V2> of(IConsumer2<V1, V2> ret) {
        return ret;
    }
}
