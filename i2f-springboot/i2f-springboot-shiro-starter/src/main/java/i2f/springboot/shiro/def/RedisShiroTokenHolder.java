package i2f.springboot.shiro.def;

import i2f.cache.std.expire.IExpireCache;
import i2f.extension.redis.cache.RedisCache;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2022/4/7 11:05
 * @desc
 */
@ConditionalOnBean({
        RedisCache.class
})
@Data
@Slf4j
public class RedisShiroTokenHolder extends AbstractShiroTokenHolder implements InitializingBean {
    public static final int EXPIRE_TIME = 30;
    public static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.MINUTES;

    @Autowired
    protected RedisCache redisCache;

    protected IExpireCache<String, Object> cache;

    @Override
    protected int getExpireTime() {
        return EXPIRE_TIME;
    }

    @Override
    protected TimeUnit getExpireTimeUnit() {
        return EXPIRE_TIME_UNIT;
    }

    @Override
    protected IExpireCache<String, Object> getCache() {
        return cache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = redisCache;
        log.info("RedisShiroTokenHolder config done.");
    }
}
