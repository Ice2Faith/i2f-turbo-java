package i2f.limit.impl;

import i2f.limit.ILimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/22 14:27
 * @desc 令牌桶限流器
 * 纯时间计算、无多余现成设计、及其轻量
 */
public class TokenBucketRateLimiter implements ILimiter {

    protected double ratePerSecond;
    protected double capacity;
    protected double currentTokens;
    protected long lastRefillTime;

    protected final ReentrantLock lock = new ReentrantLock();
    protected final Condition notEmpty = lock.newCondition();

    // 最小等待时间，避免忙等
    protected static final long MIN_WAIT_NANOS = 100_000; // 0.1ms

    /**
     * @param ratePerSecond 每秒产生的令牌数，小于1即可表示每分钟等单位，进行换算即可
     * @param capacity      桶的最大容量，用于缓冲
     */
    public TokenBucketRateLimiter(double ratePerSecond, double capacity) {
        if (ratePerSecond <= 0 || capacity <= 0) {
            throw new IllegalArgumentException("ratePerSecond and capacity both must > 0");
        }
        this.ratePerSecond = ratePerSecond;
        this.capacity = capacity;
        // 初始化立即填满桶
        this.currentTokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    /**
     * 根据时间差补充令牌
     */
    private void refill() {
        long now = System.nanoTime();

        // 时间回拨保护
        if (now < lastRefillTime) {
            lastRefillTime = now;
            return;
        }

        double elapsedTime = (now - lastRefillTime) / 1_000_000_000.0;
        if (elapsedTime > 0) {
            double tokensToAdd = elapsedTime * ratePerSecond;
            currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
            lastRefillTime = now;
            if (currentTokens > 0) {
                // 只要有令牌可消费时，唤醒等待线程
                notEmpty.signalAll();
            }
        }

    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return tryAcquire(1.0, timeout, unit);
    }


    @Override
    public boolean tryAcquire() {
        return tryAcquire(1.0);
    }


    /**
     * 阻塞等到 permits 个令牌，设置最大等待超时时间
     *
     * @param permits
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryAcquire(double permits, long timeout, TimeUnit unit) throws InterruptedException {
        if (permits <= 0) {
            throw new IllegalArgumentException("permits must > 0");
        }
        if (permits > capacity) {
            return false;
        }
        long timeoutNanos = unit.toNanos(timeout);

        if (!lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS)) {
            return false;
        }

        try {
            long deadline = System.nanoTime() + timeoutNanos;

            while (true) {
                refill();

                if (currentTokens >= permits) {
                    currentTokens -= permits;
                    return true;
                }

                long remainingNanos = deadline - System.nanoTime();
                if (remainingNanos <= 0) {
                    return false;
                }

                double tokensNeeded = permits - currentTokens;
                long waitNanos = (long) ((tokensNeeded / ratePerSecond) * 1_000_000_000);
                long actualWaitNanos = Math.min(waitNanos, remainingNanos);

                notEmpty.awaitNanos(Math.max(actualWaitNanos, MIN_WAIT_NANOS));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 立即尝试获取 permits 个令牌，获取不到也立即返回
     *
     * @param permits
     * @return
     */
    public boolean tryAcquire(double permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("permits must > 0");
        }
        if (permits > capacity) {
            return false;
        }
        lock.lock();
        try {
            refill();
            if (currentTokens >= permits) {
                currentTokens -= permits;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public double ratePerSecond() {
        lock.lock();
        try {
            return ratePerSecond;
        } finally {
            lock.unlock();
        }
    }

    public double capacity() {
        lock.lock();
        try {
            return capacity;
        } finally {
            lock.unlock();
        }
    }

    public double currentTokens() {
        lock.lock();
        try {
            return currentTokens;
        } finally {
            lock.unlock();
        }
    }

    public void ratePerSecond(double ratePerSecond) {
        if (ratePerSecond <= 0) {
            throw new IllegalArgumentException("ratePerSecond must > 0");
        }
        lock.lock();
        try {
            this.ratePerSecond = ratePerSecond;
        } finally {
            lock.unlock();
        }
    }

    public void capacity(double capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must > 0");
        }
        lock.lock();
        try {
            this.capacity = capacity;
        } finally {
            lock.unlock();
        }
    }

}