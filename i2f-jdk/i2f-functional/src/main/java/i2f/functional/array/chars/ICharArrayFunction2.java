package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction2<V1, V2> extends ICharArrayFunction {
    char[] apply(V1 v1, V2 v2);
    static<V1, V2> ICharArrayFunction2<V1, V2> of(ICharArrayFunction2<V1, V2> ret){
        return ret;
    }
}
