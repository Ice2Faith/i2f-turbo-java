package i2f.springboot.limit.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:24
 */
@Data
@Slf4j
@Component
public class LimitRedisHolder {

    @Autowired
    private ApplicationContext applicationContext;

    private final AtomicReference<RedisTemplate> redisHolder = new AtomicReference<>();

    public RedisTemplate getRedisTemplate() {
        RedisTemplate ret = redisHolder.get();
        if (ret != null) {
            return ret;
        }
        try {
            String[] names = applicationContext.getBeanNamesForType(RedisTemplate.class);
            for (String name : names) {
                RedisTemplate bean = applicationContext.getBean(name, RedisTemplate.class);
                if (bean != null) {
                    redisHolder.set(bean);
                    return bean;
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        throw new IllegalStateException("missing redis template!");
    }
}
