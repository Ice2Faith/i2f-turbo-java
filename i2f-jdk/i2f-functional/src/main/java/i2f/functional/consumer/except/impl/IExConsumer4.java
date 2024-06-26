package i2f.functional.consumer.except.impl;

import i2f.functional.consumer.except.IExConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IExConsumer4<V1, V2, V3, V4> extends IExConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;

    static <V1, V2, V3, V4> IExConsumer4<V1, V2, V3, V4> of(IExConsumer4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
