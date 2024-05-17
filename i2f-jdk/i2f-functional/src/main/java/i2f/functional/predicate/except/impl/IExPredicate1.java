package i2f.functional.predicate.except.impl;

import i2f.functional.predicate.except.IExPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IExPredicate1<V1> extends IExPredicate {
    boolean test(V1 v1) throws Throwable;
    static<V1> IExPredicate1<V1> of(IExPredicate1<V1> ret){
        return ret;
    }
}
