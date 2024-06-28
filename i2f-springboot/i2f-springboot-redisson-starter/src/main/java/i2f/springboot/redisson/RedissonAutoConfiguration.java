package i2f.springboot.redisson;

import i2f.springboot.redisson.aop.RedissonLockAop;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

/**
 * Redisson配置类
 */
@Slf4j
@Import({
        RedissonLockProvider.class,
        RedissonAtomic.class,
        RedissonLockAop.class
})
@Data
@ConfigurationProperties(prefix = "i2f.redission")
public class RedissonAutoConfiguration {
    /**
     * redisson协议前缀
     */
    private static final String SCHEMA_PREFIX = "redis://";

    /**
     * 锁超时时间
     */
    private long lockWatchdogTimeout = 3000;

    /**
     * 定时ping时间
     */
    private int pingConnectionInterval = 3000;

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        RedisProperties.Cluster redisPropertiesCluster = redisProperties.getCluster();
        if (redisPropertiesCluster != null) {
            //集群redis
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (String cluster : redisPropertiesCluster.getNodes()) {
                clusterServersConfig.addNodeAddress(SCHEMA_PREFIX + cluster);
            }
            if (StringUtils.hasText(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
            if (redisProperties.getTimeout() != null) {
                clusterServersConfig.setTimeout((int) redisProperties.getTimeout().toMillis());
            }
            clusterServersConfig.setPingConnectionInterval(pingConnectionInterval);
        } else if (StringUtils.hasText(redisProperties.getHost())) {
            //单点redis
            SingleServerConfig singleServerConfig = config.useSingleServer().
                    setAddress(SCHEMA_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort());
            if (StringUtils.hasText(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
            if (redisProperties.getTimeout() != null) {
                singleServerConfig.setTimeout((int) redisProperties.getTimeout().toMillis());
            }
            singleServerConfig.setPingConnectionInterval(pingConnectionInterval);
            singleServerConfig.setDatabase(redisProperties.getDatabase());
        } else if (sentinel != null) {
            //哨兵模式
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(sentinel.getMaster());
            for (String node : sentinel.getNodes()) {
                sentinelServersConfig.addSentinelAddress(SCHEMA_PREFIX + node);
            }
            if (StringUtils.hasText(redisProperties.getPassword())) {
                sentinelServersConfig.setPassword(redisProperties.getPassword());
            }
            if (redisProperties.getTimeout() != null) {
                sentinelServersConfig.setTimeout((int) redisProperties.getTimeout().toMillis());
            }
            sentinelServersConfig.setPingConnectionInterval(pingConnectionInterval);
            sentinelServersConfig.setDatabase(redisProperties.getDatabase());
        }
        config.setLockWatchdogTimeout(lockWatchdogTimeout);
        return Redisson.create(config);
    }
}
