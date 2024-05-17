package i2f.functional.array.ints;

import i2f.functional.array.IIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IIntArrayFunction1<V1> extends IIntArrayFunction {
    int[] apply(V1 v1);
    static<V1> IIntArrayFunction1<V1> of(IIntArrayFunction1<V1> ret){
        return ret;
    }
}
