package i2f.springboot.swl.spring;

import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.cache.std.expire.IExpireCache;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/7/11 22:32
 * @desc
 */
@ConditionalOnExpression("${i2f.swl.missing.enable:true}")
@Data
@NoArgsConstructor
public class SwlMissingBeanConfiguration {

    @ConditionalOnMissingBean(IExpireCache.class)
    @Bean
    public IExpireCache<String, Object> concurrentHashMapExpireCache() {
        return new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    }
}
