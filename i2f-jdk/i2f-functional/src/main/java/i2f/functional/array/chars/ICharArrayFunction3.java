package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction3<V1, V2, V3> extends ICharArrayFunction {
    char[] apply(V1 v1, V2 v2, V3 v3);

    static <V1, V2, V3> ICharArrayFunction3<V1, V2, V3> of(ICharArrayFunction3<V1, V2, V3> ret) {
        return ret;
    }
}
