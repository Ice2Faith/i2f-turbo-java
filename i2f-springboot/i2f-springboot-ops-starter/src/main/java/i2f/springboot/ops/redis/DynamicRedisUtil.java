package i2f.springboot.ops.redis;

import i2f.springboot.ops.redis.data.RedisMeta;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2026/5/22 20:07
 * @desc
 */
public class DynamicRedisUtil {

    // 定义两种客户端在类路径中的全限定名
    private static final String LETTUCE_CLIENT_CLASS = "io.lettuce.core.RedisClient";
    private static final String JEDIS_CLIENT_CLASS = "redis.clients.jedis.Jedis";

    /**
     * 根据传入的 Redis 地址信息，自动识别并创建一个 RedisTemplate
     */
    public static RedisTemplate<String, Object> getDynamicRedisTemplate(RedisMeta meta) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 统一配置目标 Redis 的基本信息
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(meta.getHost());
        redisConfig.setPort(meta.getPort() <= 0 ? 6379 : meta.getPort());
        String password = meta.getPassword();
        if (password != null && !password.isEmpty()) {
            redisConfig.setPassword(RedisPassword.of(password));
        }
        if (meta.getDatabase() >= 0) {
            redisConfig.setDatabase(meta.getDatabase());
        }

        // 核心逻辑：自动检测类路径中存在的客户端
        if (ClassUtils.isPresent(LETTUCE_CLIENT_CLASS, null)) {
            // 如果存在 Lettuce，优先使用 Lettuce（线程安全，非阻塞）
            template.setConnectionFactory(createLettuceFactory(redisConfig));
        } else if (ClassUtils.isPresent(JEDIS_CLIENT_CLASS, null)) {
            // 如果没有 Lettuce 但有 Jedis，则使用 Jedis
            template.setConnectionFactory(createJedisFactory(redisConfig));
        } else {
            throw new IllegalStateException("未找到可用的 Redis 客户端！请引入 lettuce-core 或 jedis 依赖。");
        }

        // 统一设置序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    public static void destroy(RedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            return;
        }
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            return;
        }
        if (factory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) factory;
            lettuceConnectionFactory.destroy();
        } else if (factory instanceof JedisConnectionFactory) {
            JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) factory;
            jedisConnectionFactory.destroy();
        }
    }

    /**
     * 创建 Lettuce 连接工厂
     */
    private static LettuceConnectionFactory createLettuceFactory(RedisStandaloneConfiguration config) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * 创建 Jedis 连接工厂（自带连接池配置）
     */
    private static JedisConnectionFactory createJedisFactory(RedisStandaloneConfiguration config) {
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
}
