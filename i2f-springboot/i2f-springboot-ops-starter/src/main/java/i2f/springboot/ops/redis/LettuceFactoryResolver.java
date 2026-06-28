package i2f.springboot.ops.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author Ice2Faith
 * @date 2026/6/17 17:52
 * @desc
 */
public class LettuceFactoryResolver {
    /**
     * 创建 Lettuce 连接工厂
     */
    public static LettuceConnectionFactory createFactory(RedisStandaloneConfiguration config) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        return factory;
    }

    public static void closeFactory(RedisConnectionFactory factory) {
        if (factory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) factory;
            lettuceConnectionFactory.destroy();
        }
    }
}
