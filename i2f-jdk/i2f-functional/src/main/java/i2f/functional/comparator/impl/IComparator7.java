package i2f.functional.comparator.impl;

import i2f.functional.comparator.IComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IComparator7<V1, V2, V3, V4, V5, V6, V7> extends IComparator {
    int compare(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7);

    static <V1, V2, V3, V4, V5, V6, V7> IComparator7<V1, V2, V3, V4, V5, V6, V7> of(IComparator7<V1, V2, V3, V4, V5, V6, V7> ret) {
        return ret;
    }
}
