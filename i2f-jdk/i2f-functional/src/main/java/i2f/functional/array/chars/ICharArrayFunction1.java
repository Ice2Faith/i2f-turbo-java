package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction1<V1> extends ICharArrayFunction {
    char[] apply(V1 v1);
    static<V1> ICharArrayFunction1<V1> of(ICharArrayFunction1<V1> ret){
        return ret;
    }
}
