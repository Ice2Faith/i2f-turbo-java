package i2f.springboot.swl.spring;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/7/11 22:32
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlMissingBeanConfiguration {

    @ConditionalOnMissingBean(IExpireCache.class)
    @Bean
    public IExpireCache<String,Object> concurrentHashMapExpireCache(){
        return new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    }
}
