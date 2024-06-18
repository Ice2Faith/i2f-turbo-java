package i2f.functional.array.bools;

import i2f.functional.array.IBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IBoolArrayFunction1<V1> extends IBoolArrayFunction {
    boolean[] apply(V1 v1);

    static <V1> IBoolArrayFunction1<V1> of(IBoolArrayFunction1<V1> ret) {
        return ret;
    }
}
