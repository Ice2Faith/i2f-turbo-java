package i2f.functional.base.chars;

import i2f.functional.base.ICharFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface ICharFunction1<V1> extends ICharFunction {
    char apply(V1 v1);

    static <V1> ICharFunction1<V1> of(ICharFunction1<V1> ret) {
        return ret;
    }
}
