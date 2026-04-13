package i2f.lru;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/4/5 17:59
 * @desc
 */
public final class ScopeValue<T> implements AutoCloseable {
    private final T resource;
    private final ExConsumer<T> cleaner;

    public ScopeValue(T resource, ExConsumer<T> cleaner) {
        Objects.requireNonNull(resource, "resource cannot be null!");
        Objects.requireNonNull(cleaner, "cleaner cannot be null!");
        this.resource = resource;
        this.cleaner = cleaner;
    }

    public static <T> ScopeValue<T> ofSupplier(Supplier<T> supplier, ExConsumer<T> cleaner) {
        return new ScopeValue<>(supplier.get(), cleaner);
    }

    public static <T extends AutoCloseable> ScopeValue<T> ofSupplier(Supplier<T> supplier) {
        return new ScopeValue<>(supplier.get(), AutoCloseable::close);
    }

    public static <T> ScopeValue<T> of(T resource, ExConsumer<T> cleaner) {
        return new ScopeValue<>(resource, cleaner);
    }

    public static <T> ScopeValue<ThreadLocal<T>> of(ThreadLocal<T> resource) {
        return of(resource, ThreadLocal::remove);
    }

    public static <T extends AutoCloseable> ScopeValue<T> of(T res) {
        return of(res, AutoCloseable::close);
    }

    public static <T extends Lock> ScopeValue<T> of(T lock) {
        lock.lock();
        return of(lock, Lock::unlock);
    }

    public UncheckedScopeValue<T> unchecked(){
        return UncheckedScopeValue.of(this.resource, cleaner::accept);
    }

    @FunctionalInterface
    public static interface ExConsumer<T> {
        void accept(T value) throws Exception;
    }

    public T get() {
        return this.resource;
    }

    public ExConsumer<T> cleaner(){
        return this.cleaner;
    }

    @Override
    public void close() throws Exception {
        cleaner.accept(resource);
    }
}
