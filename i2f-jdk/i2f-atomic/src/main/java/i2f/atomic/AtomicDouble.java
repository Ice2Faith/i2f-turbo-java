package i2f.atomic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Ice2Faith
 * @date 2024/4/23 9:22
 * @desc
 */
public class AtomicDouble extends Number implements Serializable {
    private static final long serialVersionUID = 0L;
    private transient volatile long value;
    private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");

    public AtomicDouble(double initialValue) {
        this.value = Double.doubleToRawLongBits(initialValue);
    }

    public AtomicDouble() {
    }

    public final double get() {
        return Double.longBitsToDouble(this.value);
    }

    public final void set(double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        this.value = next;
    }

    public final void lazySet(double newValue) {
        this.set(newValue);
    }

    public final double getAndSet(double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        return Double.longBitsToDouble(updater.getAndSet(this, next));
    }

    public final boolean compareAndSet(double expect, double update) {
        return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final boolean weakCompareAndSet(double expect, double update) {
        return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final double getAndIncrement() {
        return getAndAdd(1.0);
    }

    public final double getAndDecrement() {
        return getAndAdd(-1.0);
    }

    public final double getAndAdd(double delta) {
        long current;
        double currentVal;
        long next;
        do {
            current = this.value;
            currentVal = Double.longBitsToDouble(current);
            double nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!updater.compareAndSet(this, current, next));

        return currentVal;
    }

    public final double incrementAndGet() {
        return addAndGet(1.0);
    }

    public final double decrementAndGet() {
        return addAndGet(-1.0);
    }

    public final double addAndGet(double delta) {
        long current;
        double nextVal;
        long next;
        do {
            current = this.value;
            double currentVal = Double.longBitsToDouble(current);
            nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!updater.compareAndSet(this, current, next));

        return nextVal;
    }

    public final double getAndUpdate(DoubleUnaryOperator updateFunction) {
        double prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsDouble(prev);
        } while (!compareAndSet(prev, next));
        return prev;
    }

    public final double updateAndGet(DoubleUnaryOperator updateFunction) {
        double prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsDouble(prev);
        } while (!compareAndSet(prev, next));
        return next;
    }

    public final double getAndAccumulate(double x,
                                         DoubleBinaryOperator accumulatorFunction) {
        double prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsDouble(prev, x);
        } while (!compareAndSet(prev, next));
        return prev;
    }

    public final double accumulateAndGet(double x,
                                         DoubleBinaryOperator accumulatorFunction) {
        double prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsDouble(prev, x);
        } while (!compareAndSet(prev, next));
        return next;
    }

    @Override
    public String toString() {
        return Double.toString(this.get());
    }

    @Override
    public int intValue() {
        return (int) this.get();
    }

    @Override
    public long longValue() {
        return (long) this.get();
    }

    @Override
    public float floatValue() {
        return (float) this.get();
    }

    @Override
    public double doubleValue() {
        return this.get();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeDouble(this.get());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.set(s.readDouble());
    }
}

