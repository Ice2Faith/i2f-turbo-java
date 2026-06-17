package i2f.springboot.ops.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2026/6/17 17:53
 * @desc
 */
public class JedisFactoryResolver {

    /**
     * 创建 Jedis 连接工厂（自带连接池配置）
     */
    public static JedisConnectionFactory createJedisFactory(RedisStandaloneConfiguration config) {
        // Jedis 必须配置连接池以保证线程安全和性能
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(2);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxWait(Duration.ofMillis(3000));

        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(5))
                .usePooling()
                .poolConfig(poolConfig)
                .build();

        JedisConnectionFactory factory = new JedisConnectionFactory(config, clientConfig);
        factory.afterPropertiesSet();
        return factory;
    }

    public static void closeFactory(RedisConnectionFactory factory) {
        if (factory instanceof JedisConnectionFactory) {
            JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) factory;
            jedisConnectionFactory.destroy();
        }
    }
}
