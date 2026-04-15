package i2f.lru;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/4/5 17:59
 * @desc
 */
public final class UncheckedScopeValue<T> implements UncheckedAutoCloseable {
    private final T resource;
    private final ExConsumer<T> cleaner;

    public UncheckedScopeValue(T resource, ExConsumer<T> cleaner) {
        Objects.requireNonNull(resource, "resource cannot be null!");
        Objects.requireNonNull(cleaner, "cleaner cannot be null!");
        this.resource = resource;
        this.cleaner = cleaner;
    }

    public static <T> UncheckedScopeValue<T> ofSupplier(Supplier<T> supplier, ExConsumer<T> cleaner) {
        return new UncheckedScopeValue<>(supplier.get(), cleaner);
    }

    public static <T extends AutoCloseable> UncheckedScopeValue<T> ofSupplier(Supplier<T> supplier) {
        return new UncheckedScopeValue<>(supplier.get(), AutoCloseable::close);
    }

    public static <T> UncheckedScopeValue<T> of(T resource, ExConsumer<T> cleaner) {
        return new UncheckedScopeValue<>(resource, cleaner);
    }

    public static <T> UncheckedScopeValue<ThreadLocal<T>> of(ThreadLocal<T> resource) {
        return of(resource, ThreadLocal::remove);
    }

    public static <T extends AutoCloseable> UncheckedScopeValue<T> of(T res) {
        return of(res, AutoCloseable::close);
    }

    public static <T> UncheckedScopeValue<T> of(ScopeValue<T> res) {
        return of(res.get(), res.cleaner()::accept);
    }

    public static <T extends Lock> UncheckedScopeValue<T> of(T lock) {
        lock.lock();
        return of(lock, Lock::unlock);
    }

    @FunctionalInterface
    public static interface ExConsumer<T> {
        void accept(T value) throws Throwable;
    }

    public T get() {
        return this.resource;
    }

    public ExConsumer<T> cleaner() {
        return this.cleaner;
    }

    @Override
    public void doClose() throws Throwable {
        cleaner.accept(resource);
    }
}
