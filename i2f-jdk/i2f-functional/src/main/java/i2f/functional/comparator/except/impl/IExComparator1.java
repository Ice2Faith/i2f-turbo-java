package i2f.functional.comparator.except.impl;

import i2f.functional.comparator.except.IExComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IExComparator1<V1> extends IExComparator {
    int compare(V1 v1) throws Throwable;
    static<V1> IExComparator1<V1> of(IExComparator1<V1> ret){
        return ret;
    }
}
