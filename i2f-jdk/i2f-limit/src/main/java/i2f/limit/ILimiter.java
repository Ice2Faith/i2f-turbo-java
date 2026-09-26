package i2f.limit;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/9/22 14:27
 * @desc 限流器
 */
public interface ILimiter {
    boolean tryAcquire();

    boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException;
}
