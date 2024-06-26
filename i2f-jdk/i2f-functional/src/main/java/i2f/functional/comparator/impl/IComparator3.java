package i2f.functional.comparator.impl;

import i2f.functional.comparator.IComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IComparator3<V1, V2, V3> extends IComparator {
    int compare(V1 v1, V2 v2, V3 v3);

    static <V1, V2, V3> IComparator3<V1, V2, V3> of(IComparator3<V1, V2, V3> ret) {
        return ret;
    }
}
