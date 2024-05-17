package i2f.functional.comparator.except.impl;

import i2f.functional.comparator.except.IExComparator;
import i2f.functional.comparator.impl.IComparator0;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IExComparator0 extends IExComparator {
    int compare() throws Throwable;
    static IExComparator0 of(IExComparator0 ret){
        return ret;
    }
}
