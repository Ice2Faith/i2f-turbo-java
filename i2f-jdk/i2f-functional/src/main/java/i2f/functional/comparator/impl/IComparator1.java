package i2f.functional.comparator.impl;

import i2f.functional.comparator.IComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IComparator1<V1> extends IComparator {
    int compare(V1 v1);

    static <V1> IComparator1<V1> of(IComparator1<V1> ret) {
        return ret;
    }
}
