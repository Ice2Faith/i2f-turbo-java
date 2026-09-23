package i2f.limit.impl;

import i2f.limit.IKeyedLimiter;
import i2f.limit.ILimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/9/22 14:40
 * @desc
 */
public class ConcurrentMapKeyedLimiter implements IKeyedLimiter {
    protected final ConcurrentHashMap<String, ILimiter> holder = new ConcurrentHashMap<>();
    protected final Function<String, ILimiter> producer;

    public ConcurrentMapKeyedLimiter(Function<String, ILimiter> producer) {
        this.producer = producer;
    }

    public ConcurrentMapKeyedLimiter(Supplier<ILimiter> supplier) {
        this(k -> supplier.get());
    }

    @Override
    public ILimiter get(String key) {
        return holder.computeIfAbsent(key, producer);
    }

    @Override
    public ILimiter remove(String key) {
        return holder.remove(key);
    }
}
