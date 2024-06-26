package i2f.functional.comparator.impl;

import i2f.functional.comparator.IComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IComparator5<V1, V2, V3, V4, V5> extends IComparator {
    int compare(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);

    static <V1, V2, V3, V4, V5> IComparator5<V1, V2, V3, V4, V5> of(IComparator5<V1, V2, V3, V4, V5> ret) {
        return ret;
    }
}
