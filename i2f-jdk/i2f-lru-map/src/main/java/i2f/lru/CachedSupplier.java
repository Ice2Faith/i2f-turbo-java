package i2f.lru;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;
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
@Data
public class CachedSupplier<T> implements Supplier<T> {
    protected volatile Supplier<T> supplier;
    protected volatile long timeoutMillSeconds = -1;
    protected volatile boolean cacheNull = true;
    @Getter(value = AccessLevel.NONE)
    protected volatile long expireTs = -1;
    @Getter(value = AccessLevel.NONE)
    protected volatile AtomicReference<T> value;


    public CachedSupplier(Supplier<T> supplier) {
        this(supplier, -1);
    }

    public CachedSupplier(Supplier<T> supplier, long timeoutMillSeconds) {
        assert supplier != null;
        this.supplier = supplier;
        this.timeoutMillSeconds = timeoutMillSeconds;
    }

    @Override
    public T get() {
        synchronized (this) {
            if (this.value != null) {
                if (expireTs < 0 || System.currentTimeMillis() < expireTs) {
                    T ret = this.value.get();
                    if (cacheNull) {
                        return ret;
                    }
                    if (ret != null) {
                        return ret;
                    }
                }
            }
            AtomicReference<T> ref = new AtomicReference<>();
            ref.set(supplier.get());
            this.expireTs = -1;
            if (timeoutMillSeconds >= 0) {
                this.expireTs = System.currentTimeMillis() + timeoutMillSeconds;
            }
            this.value = ref;
            return this.value.get();
        }
    }

}
