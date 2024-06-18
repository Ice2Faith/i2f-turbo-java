package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer1<V1> extends IConsumer {
    void accept(V1 v1);

    static <V1> IConsumer1<V1> of(IConsumer1<V1> ret) {
        return ret;
    }
}
