package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction5<V1, V2, V3, V4, V5> extends ICharArrayFunction {
    char[] apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);
    static<V1, V2, V3, V4, V5> ICharArrayFunction5<V1, V2, V3, V4, V5> of(ICharArrayFunction5<V1, V2, V3, V4, V5> ret){
        return ret;
    }
}
