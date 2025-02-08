package i2f.springboot.shiro.def;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.springboot.shiro.ShiroAutoConfiguration;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@ConditionalOnBean(ShiroAutoConfiguration.class)
@ConditionalOnMissingBean(AbstractShiroTokenHolder.class)
@Slf4j
@Component
public class DefaultShiroTokenHolder extends AbstractShiroTokenHolder {
    public static final int EXPIRE_TIME = 30;
    public static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.MINUTES;

    protected IExpireCache<String, Object> cache = new ObjectExpireCacheWrapper<>(new MapCache<>());

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
}
