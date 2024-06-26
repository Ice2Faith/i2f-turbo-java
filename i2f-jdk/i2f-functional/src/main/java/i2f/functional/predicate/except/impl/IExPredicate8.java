package i2f.functional.predicate.except.impl;

import i2f.functional.predicate.except.IExPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IExPredicate8<V1, V2, V3, V4, V5, V6, V7, V8> extends IExPredicate {
    boolean test(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) throws Throwable;

    static <V1, V2, V3, V4, V5, V6, V7, V8> IExPredicate8<V1, V2, V3, V4, V5, V6, V7, V8> of(IExPredicate8<V1, V2, V3, V4, V5, V6, V7, V8> ret) {
        return ret;
    }
}
