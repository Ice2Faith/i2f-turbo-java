package i2f.functional.comparator.except.impl;

import i2f.functional.comparator.except.IExComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IExComparator10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> extends IExComparator {
    int compare(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10) throws Throwable;

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> IExComparator10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> of(IExComparator10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> ret) {
        return ret;
    }
}
