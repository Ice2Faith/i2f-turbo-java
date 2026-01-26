package i2f.lru;

import i2f.clock.SystemClock;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/21 16:39
 * @desc 带缓存的 Supplier
 * 用于在某些场景中，需要获取一些数据或者状态
 * 获取的开销比较大，或者需要的得到的值不怎么会变化的情况
 * 能够有效改善获取的开销
 * 比如，获取一些JVM参数，操作系统参数等这些不变量
 */
public class CachedSupplier<T> implements Supplier<T> {
    protected final AtomicReference<Map.Entry<T, Long>> holder = new AtomicReference<>();
    protected Supplier<T> supplier;
    protected final AtomicLong expireMillSeconds = new AtomicLong(-1);
    protected final AtomicBoolean allowCacheNull = new AtomicBoolean(true);

    public CachedSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public CachedSupplier(Supplier<T> supplier, long expireMillSeconds) {
        this.supplier = supplier;
        this.expireMillSeconds.set(expireMillSeconds);
    }

    public CachedSupplier(Supplier<T> supplier, long expireMillSeconds, boolean allowCacheNull) {
        this.supplier = supplier;
        this.expireMillSeconds.set(expireMillSeconds);
        this.allowCacheNull.set(allowCacheNull);
    }

    public static <T> CachedSupplier<T> of(Supplier<T> supplier) {
        return new CachedSupplier<>(supplier);
    }

    public static <T> CachedSupplier<T> of(Supplier<T> supplier, long expireMillSeconds) {
        return new CachedSupplier<>(supplier, expireMillSeconds);
    }

    public static <T> CachedSupplier<T> of(Supplier<T> supplier, Duration expireDuration) {
        return new CachedSupplier<>(supplier, expireDuration.toMillis());
    }

    public static <T> CachedSupplier<T> of(Supplier<T> supplier, long expireMillSeconds, boolean allowCacheNull) {
        return new CachedSupplier<>(supplier, expireMillSeconds, allowCacheNull);
    }

    public static <T> CachedSupplier<T> of(Supplier<T> supplier, Duration expireDuration, boolean allowCacheNull) {
        return new CachedSupplier<>(supplier, expireDuration.toMillis(), allowCacheNull);
    }

    public CachedSupplier<T> withExpireMillSeconds(long expireMillSeconds) {
        this.expireMillSeconds.set(expireMillSeconds);
        return this;
    }

    public CachedSupplier<T> withExpireDuration(Duration expireDuration) {
        this.expireMillSeconds.set(expireDuration.toMillis());
        return this;
    }

    public CachedSupplier<T> withAllowCacheNull(boolean allowCacheNull) {
        this.allowCacheNull.set(allowCacheNull);
        return this;
    }

    public CachedSupplier<T> invalidate() {
        holder.set(null);
        return this;
    }

    @Override
    public T get() {
        Map.Entry<T, Long> ref = innerGet();
        if (ref != null) {
            return ref.getKey();
        }
        return holder.updateAndGet(v -> {
            Map.Entry<T, Long> cv = innerGet();
            if (cv != null) {
                return cv;
            }
            T value = supplier.get();
            long d = expireMillSeconds.get();
            long ts = (d >= 0) ? (SystemClock.currentTimeMillis() + d) : (-1);
            return new AbstractMap.SimpleEntry<>(value, ts);
        }).getKey();
    }


    protected Map.Entry<T, Long> innerGet() {
        Map.Entry<T, Long> ref = holder.get();
        if (ref != null) {
            if (expireMillSeconds.get() < 0 || SystemClock.currentTimeMillis() <= ref.getValue()) {
                T ret = ref.getKey();
                if (allowCacheNull.get() || ret != null) {
                    return ref;
                }
            }
        }
        return null;
    }
}
