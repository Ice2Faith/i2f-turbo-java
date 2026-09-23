package i2f.springboot.redisson.limit;

import i2f.limit.ILimiter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/9/22 15:41
 * @desc
 */
public class RedissonRateLimiter implements ILimiter {
    protected final RRateLimiter limiter;

    public RedissonRateLimiter(RRateLimiter limiter) {
        this.limiter = limiter;
    }

    public RedissonRateLimiter(RedissonClient redisson, int ratePerSecond) {
        RRateLimiter limiter = redisson.getRateLimiter("redisson:rate:limiter:" + UUID.randomUUID().toString().replace("-", ""));
        limiter.setRate(RateType.OVERALL, ratePerSecond, 1, RateIntervalUnit.SECONDS);
        this.limiter = limiter;
    }

    @Override
    public boolean tryAcquire() {
        return limiter.tryAcquire();
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return limiter.tryAcquire(timeout, unit);
    }

    public boolean tryAcquire(int count) {
        return limiter.tryAcquire(count);
    }

    public boolean tryAcquire(int count, long timeout, TimeUnit unit) throws InterruptedException {
        return limiter.tryAcquire(count, timeout, unit);
    }
}
