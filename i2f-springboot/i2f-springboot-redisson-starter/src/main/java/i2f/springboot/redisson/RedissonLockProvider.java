package i2f.springboot.redisson;

import i2f.lock.ILock;
import i2f.lock.ILockProvider;
import i2f.springboot.redisson.lock.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

public class RedissonLockProvider implements ILockProvider {

    private RedissonClient redissonClient;

    public RedissonLockProvider(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public ILock getLock(String key) {
        return new RedissonLock(getRedisLock(key));
    }

    public RLock getRedisLock(String lockName) {
        return redissonClient.getLock(lockName);
    }

    public RReadWriteLock getReadWriteLock(String lockName) {
        return redissonClient.getReadWriteLock(lockName);
    }

    public RLock getRedisLock(Class<?> clazz) {
        return getRedisLock(clazz.getName());
    }

    public RReadWriteLock getReadWriteLock(Class<?> clazz) {
        return getReadWriteLock(clazz.getName());
    }

    public RLock getRedisLock(Class<?> clazz, String subKey) {
        return getRedisLock(clazz.getName() + ":" + subKey);
    }

    public RReadWriteLock getReadWriteLock(Class<?> clazz, String subKey) {
        return getReadWriteLock(clazz.getName() + ":" + subKey);
    }
}
