package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer3<V1, V2, V3> extends IConsumer {
    void accept(V1 v1, V2 v2, V3 v3);
    static<V1, V2, V3> IConsumer3<V1, V2, V3> of(IConsumer3<V1, V2, V3> ret){
        return ret;
    }
}
