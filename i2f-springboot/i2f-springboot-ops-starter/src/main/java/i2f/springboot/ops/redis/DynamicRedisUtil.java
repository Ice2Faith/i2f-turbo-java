package i2f.springboot.ops.redis;

import i2f.springboot.ops.redis.data.RedisMeta;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;

/**
 * @author Ice2Faith
 * @date 2026/5/22 20:07
 * @desc
 */
public class DynamicRedisUtil {

    // 定义两种客户端在类路径中的全限定名
    private static final String LETTUCE_CLIENT_CLASS = "io.lettuce.core.RedisClient";
    private static final String JEDIS_CLIENT_CLASS = "redis.clients.jedis.Jedis";
    private static final String REDISSON_CLIENT_CLASS = "org.redisson.api.RedissonClient";

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
            template.setConnectionFactory(LettuceFactoryResolver.createFactory(redisConfig));
        } else if (ClassUtils.isPresent(JEDIS_CLIENT_CLASS, null)) {
            // 有 Jedis，则使用 Jedis
            template.setConnectionFactory(JedisFactoryResolver.createFactory(redisConfig));
        } else if (ClassUtils.isPresent(REDISSON_CLIENT_CLASS, null)) {
            // 有 Redisson，则使用 Redisson
            template.setConnectionFactory(RedissonFactoryResolver.createFactory(redisConfig));
        } else {
            throw new IllegalStateException("未找到可用的 Redis 客户端！请引入 lettuce-core / jedis / redisson 依赖。");
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
        LettuceFactoryResolver.closeFactory(factory);
        JedisFactoryResolver.closeFactory(factory);
        RedissonFactoryResolver.closeFactory(factory);
    }


}
