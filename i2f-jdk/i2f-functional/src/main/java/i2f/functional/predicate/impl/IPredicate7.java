package i2f.functional.predicate.impl;

import i2f.functional.predicate.IPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IPredicate7<V1, V2, V3, V4, V5, V6, V7> extends IPredicate {
    boolean test(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7);

    static <V1, V2, V3, V4, V5, V6, V7> IPredicate7<V1, V2, V3, V4, V5, V6, V7> of(IPredicate7<V1, V2, V3, V4, V5, V6, V7> ret) {
        return ret;
    }
}
