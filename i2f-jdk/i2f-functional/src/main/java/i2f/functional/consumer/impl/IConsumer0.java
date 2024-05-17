package i2f.functional.consumer.impl;

import i2f.functional.consumer.IConsumer;
import i2f.functional.func.except.impl.IExFunction0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IConsumer0 extends IConsumer {
    void accept();
    static IConsumer0 of(IConsumer0 ret){
        return ret;
    }
}
