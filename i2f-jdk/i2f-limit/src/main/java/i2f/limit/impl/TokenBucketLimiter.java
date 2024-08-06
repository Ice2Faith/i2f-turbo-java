package i2f.limit.impl;

import i2f.clock.SystemClock;
import i2f.limit.Limiter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/8/6 9:00
 * @desc 限流桶
 * 限制每秒的QPS
 */
@Data
@NoArgsConstructor
public class TokenBucketLimiter implements Limiter {
    protected AtomicInteger nullCnt = new AtomicInteger(1);
    /**
     * key: 限流键
     * value: {
     * key: 可用令牌数
     * value: 最后访问时间戳
     * }
     */
    protected ConcurrentHashMap<String, Map.Entry<AtomicInteger, AtomicLong>> bucket = new ConcurrentHashMap<>();
    protected AtomicInteger limitCount = new AtomicInteger(300);
    protected AtomicLong maxKeepaliveMillSeconds = new AtomicLong(30 * 60 * 1000);

    protected AtomicBoolean initialed = new AtomicBoolean(false);
    protected long timePeriod = 1;
    protected TimeUnit timeUnit = TimeUnit.SECONDS;
    protected ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "limit-thread");
            thread.setDaemon(true);
            return thread;
        }
    });

    public TokenBucketLimiter(int limitCount) {
        this.limitCount.set(limitCount);
    }

    public TokenBucketLimiter(int limitCount, long timePeriod, TimeUnit timeUnit) {
        this.limitCount.set(limitCount);
        this.timePeriod = timePeriod;
        this.timeUnit = timeUnit;
    }


    public void init() {
        if (initialed.getAndSet(true)) {
            return;
        }
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                CopyOnWriteArraySet<String> removeSet = new CopyOnWriteArraySet<>();
                long ts = SystemClock.currentTimeMillis();
                bucket.replaceAll((k, v) -> {
                    long lts = v.getValue().get();
                    if (ts - lts > maxKeepaliveMillSeconds.get()) {
                        removeSet.add(k);
                    }
                    v.getKey().updateAndGet((val) -> {
                        val = Math.min(limitCount.get(), val + 1);
                        return val;
                    });
                    return v;
                });
                for (String k : removeSet) {
                    bucket.remove(k);
                }
                nullCnt.updateAndGet((val) -> {
                    val = Math.min(limitCount.get(), val + 1);
                    return val;
                });
            }
        }, 0, Math.max(1, timePeriod), timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    @Override
    public boolean require(String name) {
        if (!initialed.get()) {
            init();
        }
        AtomicInteger cnt = null;
        if (name == null) {
            cnt = nullCnt;
        } else {
            Map.Entry<AtomicInteger, AtomicLong> entry = bucket.computeIfAbsent(name, (k) -> new AbstractMap.SimpleEntry<>(new AtomicInteger(1), new AtomicLong(SystemClock.currentTimeMillis())));
            cnt = entry.getKey();
            entry.getValue().set(SystemClock.currentTimeMillis());
        }
        int val = cnt.getAndUpdate((v) -> {
            v = Math.max(v - 1, 0);
            return v;
        });
        return val > 0;
    }

}
