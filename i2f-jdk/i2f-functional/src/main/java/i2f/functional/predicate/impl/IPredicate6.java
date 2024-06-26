package i2f.functional.predicate.impl;

import i2f.functional.predicate.IPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IPredicate6<V1, V2, V3, V4, V5, V6> extends IPredicate {
    boolean test(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6);

    static <V1, V2, V3, V4, V5, V6> IPredicate6<V1, V2, V3, V4, V5, V6> of(IPredicate6<V1, V2, V3, V4, V5, V6> ret) {
        return ret;
    }
}
