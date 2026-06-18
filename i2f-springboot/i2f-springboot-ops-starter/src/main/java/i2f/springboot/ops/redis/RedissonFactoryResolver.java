package i2f.springboot.ops.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/6/18 15:24
 * @desc
 */
public class RedissonFactoryResolver {

    public static RedissonClient createClient(RedisStandaloneConfiguration cfg) {
        Config config = new Config();
        String prefix = "redis://";

        String password = null;
        RedisPassword pwd = cfg.getPassword();
        if (pwd != null && pwd.isPresent()) {
            password = new String(pwd.get());
        }
        config.useSingleServer()
                .setAddress(prefix + cfg.getHostName() + ":" + cfg.getPort())
                .setDatabase(cfg.getDatabase())
                .setUsername(cfg.getUsername())
                .setPassword(password)
                .setClientName("redisson-" + UUID.randomUUID().toString().replace("-", ""));

        RedissonClient client = Redisson.create(config);
        return client;
    }

    /**
     * 创建 Redisson 连接工厂
     */
    public static RedissonConnectionFactory createFactory(RedisStandaloneConfiguration cfg) {
        RedissonClient client = createClient(cfg);
        RedissonConnectionFactory factory = new RedissonConnectionFactory(client);
        return factory;
    }

    public static void closeFactory(RedisConnectionFactory factory) {
        if (factory instanceof RedissonConnectionFactory) {
            RedissonConnectionFactory redissonConnectionFactory = (RedissonConnectionFactory) factory;
            try {
                redissonConnectionFactory.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
