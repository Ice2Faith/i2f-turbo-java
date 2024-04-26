package i2f.atomic;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Ice2Faith
 * @date 2024/4/23 9:36
 * @desc
 */
public class AtomicDoubleArray implements Serializable {
    private final AtomicLongArray array;

    public AtomicDoubleArray(int length) {
        this.array = new AtomicLongArray(length);
    }

    public AtomicDoubleArray(double[] array) {
        long[] arr = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = Double.doubleToRawLongBits(array[i]);
        }
        this.array = new AtomicLongArray(arr);
    }

    public final int length() {
        return array.length();
    }

    public final double get(int i) {
        return Double.longBitsToDouble(array.get(i));
    }

    public final void set(int i, double newValue) {
        array.set(i, Double.doubleToRawLongBits(newValue));
    }

    public final void lazySet(int i, double newValue) {
        array.lazySet(i, Double.doubleToRawLongBits(newValue));
    }

    public final double getAndSet(int i, double newValue) {
        return Double.longBitsToDouble(array.getAndSet(i, Double.doubleToRawLongBits(newValue)));
    }

    public final boolean compareAndSet(int i, double expect, double update) {
        return array.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final boolean weakCompareAndSet(int i, double expect, double update) {
        return array.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final double getAndIncrement(int i) {
        return getAndAdd(i, 1.0);
    }

    public final double getAndDecrement(int i) {
        return getAndAdd(i, -1.0);
    }

    public final double getAndAdd(int i, double delta) {
        double current;
        double next;
        do {
            current = this.get(i);
            double nextVal = current + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!compareAndSet(i, current, next));
        return current;
    }

    public final double incrementAndGet(int i) {
        return addAndGet(i, 1.0);
    }

    public final double decrementAndGet(int i) {
        return addAndGet(i, -1.0);
    }

    public double addAndGet(int i, double delta) {
        double current;
        double next;
        do {
            current = this.get(i);
            next = current + delta;
        } while (!compareAndSet(i, current, next));

        return next;
    }

    public final double getAndUpdate(int i, DoubleUnaryOperator updateFunction) {
        double current;
        double next;
        do {
            current = this.get(i);
            double nextVal = updateFunction.applyAsDouble(current);
            next = Double.doubleToRawLongBits(nextVal);
        } while (!compareAndSet(i, current, next));
        return current;
    }

    public final double updateAndGet(int i, DoubleUnaryOperator updateFunction) {
        double current;
        double next;
        do {
            current = this.get(i);
            next = updateFunction.applyAsDouble(current);
        } while (!compareAndSet(i, current, next));

        return next;
    }

    public final double getAndAccumulate(int i, double x,
                                         DoubleBinaryOperator accumulatorFunction) {
        double current;
        double next;
        do {
            current = this.get(i);
            double nextVal = accumulatorFunction.applyAsDouble(current, x);
            next = Double.doubleToRawLongBits(nextVal);
        } while (!compareAndSet(i, current, next));
        return current;
    }

    public final double accumulateAndGet(int i, double x,
                                         DoubleBinaryOperator accumulatorFunction) {
        double current;
        double next;
        do {
            current = this.get(i);
            next = accumulatorFunction.applyAsDouble(current, x);
        } while (!compareAndSet(i, current, next));

        return next;
    }

    @Override
    public String toString() {
        int iMax = array.length() - 1;
        if (iMax == -1) {
            return "[]";
        }

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(get(i));
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(',').append(' ');
        }
    }
}
