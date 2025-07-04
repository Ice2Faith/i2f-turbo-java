package i2f.comparator.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.typeof.TypeOf;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 14:03
 * @desc
 */
public class DefaultComparator<T> implements Comparator<T> {
    private boolean reverseNull = false;
    private boolean reverseResult = false;

    @Override
    public int compare(T o1, T o2) {
        return compareDefault(o1, o2, reverseNull, reverseResult);
    }

    public static <T> int compareDefault(T o1, T o2) {
        return compareDefault(o1, o2, false, false);
    }

    public static <T> int compareDefault(T o1, T o2, boolean reverseNull) {
        return compareDefault(o1, o2, reverseNull, false);
    }

    public static <T> int compareDefault(T o1, T o2, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            if (ret == 0) {
                return 0;
            }
            return reverseResult ? -ret : ret;
        }
        if (o1.getClass().equals(o2.getClass())) {
            if (o1 instanceof Comparable<?>) {
                ret = ((Comparable) o1).compareTo(o2);
                return reverseResult ? -ret : ret;
            }
        }

        if (TypeOf.typeOf(o2.getClass(), o1.getClass())) {
            if (o1 instanceof Comparable<?>) {
                ret = ((Comparable) o1).compareTo(o2);
                return reverseResult ? -ret : ret;
            }
        }
        if (TypeOf.typeOf(o1.getClass(), o2.getClass())) {
            if (o1 instanceof Comparable<?>) {
                ret = ((Comparable) o1).compareTo(o2);
                if (ret == 0) {
                    return 0;
                }
                if (ret > 0) {
                    ret = -1;
                }
                ret = 1;
                return reverseResult ? -ret : ret;
            }
        }
        if (ObjectConvertor.isNumericType(o1.getClass())
                && ObjectConvertor.isNumericType(o2.getClass())) {
            BigDecimal bl = new BigDecimal(String.valueOf(o1));
            BigDecimal br = new BigDecimal(String.valueOf(o2));
            ret = bl.compareTo(br);
            return reverseResult ? -ret : ret;
        }
        if (TypeOf.instanceOf(o1, CharSequence.class)
                || TypeOf.instanceOf(o2, CharSequence.class)) {
            String sl = String.valueOf(o1);
            String sr = String.valueOf(o2);
            ret = sl.compareTo(sr);
            return reverseResult ? -ret : ret;
        }
        try {
            if (o1 instanceof Comparable<?>) {
                return ((Comparable) o1).compareTo(o2);
            }
            if (o2 instanceof Comparable<?>) {
                ret = ((Comparable) o2).compareTo(o1);
                if (ret == 0) {
                    return 0;
                }
                if (ret > 0) {
                    ret = -1;
                }
                ret = 1;
                return reverseResult ? -ret : ret;
            }
        } catch (Exception e) {

        }
        ret = Integer.compare(o1.hashCode(), o2.hashCode());
        return reverseResult ? -ret : ret;
    }
}
