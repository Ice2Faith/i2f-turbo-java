package i2f.functional.predicate.impl;

import i2f.functional.predicate.IPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IPredicate4<V1, V2, V3, V4> extends IPredicate {
    boolean test(V1 v1, V2 v2, V3 v3, V4 v4);

    static <V1, V2, V3, V4> IPredicate4<V1, V2, V3, V4> of(IPredicate4<V1, V2, V3, V4> ret) {
        return ret;
    }
}
