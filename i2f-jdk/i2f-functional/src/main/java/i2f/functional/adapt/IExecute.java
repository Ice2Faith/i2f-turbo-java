package i2f.functional.adapt;

import i2f.functional.array.bools.except.impl.IExBoolArrayFunction0;
import i2f.functional.consumer.impl.IConsumer1;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:30
 * @desc
 */
@FunctionalInterface
public interface IExecute<T> extends IConsumer1<T> {
    static<T> IExecute<T> of(IExecute<T> ret){
        return ret;
    }
}
