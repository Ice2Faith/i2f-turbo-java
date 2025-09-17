package i2f.springboot.jdbc.bql.procedure.ext;

import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/9/17 8:43
 */
public class SpringRedisFunctions {
    public static volatile RedisTemplate<Object, Object> redisTemplate;

    public static void redis_set(Object key, Object value) {
        redisTemplate.opsForValue().set(String.valueOf(key), value);
    }

    public static void redis_set(Object key, Object value, Object ttlSeconds) {
        long ttl = new BigDecimal(String.valueOf(ttlSeconds)).longValue();
        redisTemplate.opsForValue().set(String.valueOf(key), value, ttl, TimeUnit.SECONDS);
    }

    public static Object redis_get(Object key) {
        return redisTemplate.opsForValue().get(String.valueOf(key));
    }

    public static void redis_del(Object key) {
        redisTemplate.delete(String.valueOf(key));
    }

    public static void redis_set_ttl(Object key, Object ttlSeconds) {
        long ttl = new BigDecimal(String.valueOf(ttlSeconds)).longValue();
        redisTemplate.expire(String.valueOf(key), ttl, TimeUnit.SECONDS);
    }

    public static Long redis_get_ttl(Object key) {
        return redisTemplate.getExpire(String.valueOf(key), TimeUnit.SECONDS);
    }

    public static Long redis_long_inc(Object key) {
        return redisTemplate.opsForValue().increment(String.valueOf(key));
    }

    public static Long redis_long_inc(Object key, Object delta) {
        long num = new BigDecimal(String.valueOf(delta)).longValue();
        return redisTemplate.opsForValue().increment(String.valueOf(key), num);
    }

    public static Long redis_long_dec(Object key) {
        return redisTemplate.opsForValue().decrement(String.valueOf(key));
    }

    public static Long redis_long_dec(Object key, Object delta) {
        long num = new BigDecimal(String.valueOf(delta)).longValue();
        return redisTemplate.opsForValue().decrement(String.valueOf(key), num);
    }
}
