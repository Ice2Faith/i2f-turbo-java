package i2f.springboot.redisson;

import i2f.lock.ILock;
import i2f.lock.ILockProvider;
import i2f.springboot.redisson.lock.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

public class RedissonLockProvider implements ILockProvider {
    public static final String NAME = "redisson";

    protected String keyPrefix = "lock:";

    protected RedissonClient redissonClient;

    public RedissonLockProvider(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RedissonLockProvider(String keyPrefix, RedissonClient redissonClient) {
        this.keyPrefix = keyPrefix;
        this.redissonClient = redissonClient;
    }

    @Override
    public String name() {
        return NAME;
    }

    public String getLockKey(String key) {
        String prefix = keyPrefix;
        if (prefix == null || prefix.isEmpty()) {
            return key;
        }
        if (prefix.endsWith(":")) {
            return prefix + key;
        }
        return prefix + ":" + key;
    }

    @Override
    public ILock getLock(String key) {
        return new RedissonLock(getRedisLock(key));
    }

    public RLock getRedisLock(String lockName) {
        return redissonClient.getLock(getLockKey(lockName));
    }

    public RReadWriteLock getReadWriteLock(String lockName) {
        return redissonClient.getReadWriteLock(getLockKey(lockName));
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
