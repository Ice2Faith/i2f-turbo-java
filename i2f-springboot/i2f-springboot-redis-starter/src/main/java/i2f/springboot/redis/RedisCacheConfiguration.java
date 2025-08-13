package i2f.springboot.redis;

import i2f.extension.redis.api.IRedisClient;
import i2f.extension.redis.cache.RedisCache;
import i2f.spring.redis.SpringRedisClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author Ice2Faith
 * @date 2024/6/27 9:35
 * @desc
 */
@AutoConfigureAfter({RedisAutoConfiguration.class,RedisTemplateAutoConfiguration.class})
@ConditionalOnExpression("${i2f.spring.redis.redis-cache.enable:true}")
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.spring.redis.redis-cache")
public class RedisCacheConfiguration {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Jackson2JsonRedisSerializer<Object> serializer;

    private String clientPrefix;

    private String cachePrefix;

    @ConditionalOnMissingBean(IRedisClient.class)
    @Bean
    public IRedisClient redisClient() {
        return new SpringRedisClient(clientPrefix, redisTemplate);
    }

    @ConditionalOnMissingBean(RedisCache.class)
    @Bean
    public RedisCache redisCache(IRedisClient redisClient) {
        return new RedisCache(cachePrefix, redisClient, (obj) -> {
            try {
                byte[] bytes = serializer.serialize(obj);
                return new String(bytes, "UTF-8");
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }, (str) -> {
            try {
                if (str == null) {
                    return null;
                }
                byte[] bytes = str.getBytes("UTF-8");
                return serializer.deserialize(bytes);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        });
    }
}
