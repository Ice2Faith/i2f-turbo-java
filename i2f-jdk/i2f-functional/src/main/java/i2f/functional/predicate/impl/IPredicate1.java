package i2f.functional.predicate.impl;

import i2f.functional.predicate.IPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IPredicate1<V1> extends IPredicate {
    boolean test(V1 v1);
    static<V1> IPredicate1<V1> of(IPredicate1<V1> ret){
        return ret;
    }
}
