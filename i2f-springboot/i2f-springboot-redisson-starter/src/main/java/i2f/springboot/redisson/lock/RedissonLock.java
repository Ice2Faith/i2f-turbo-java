package i2f.springboot.redisson.lock;

import i2f.lock.ILock;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/6/30 17:35
 * @desc
 */
public class RedissonLock implements ILock {
    private RLock rlock;

    public RedissonLock(RLock rlock) {
        this.rlock = rlock;
    }

    @Override
    public void lock() throws Throwable {
        rlock.lock();
    }

    @Override
    public void unlock() throws Throwable {
        rlock.unlock();
    }

    public void lock(long time, TimeUnit timeUnit) throws Throwable{
        rlock.lock(time,timeUnit);
    }
}
