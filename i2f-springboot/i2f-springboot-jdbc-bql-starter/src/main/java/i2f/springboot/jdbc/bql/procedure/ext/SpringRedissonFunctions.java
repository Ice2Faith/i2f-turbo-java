package i2f.springboot.jdbc.bql.procedure.ext;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author Ice2Faith
 * @date 2025/9/17 9:39
 */
public class SpringRedissonFunctions {
    public static volatile RedissonClient redissonClient;

    public static void redis_lock(Object key) {
        RLock lock = redissonClient.getLock(String.valueOf(key));
        lock.lock();
    }

    public static void redis_unlock(Object key) {
        RLock lock = redissonClient.getLock(String.valueOf(key));
        lock.unlock();
    }

}
