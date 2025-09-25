package i2f.springcloud.discovery.server.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2025/9/25 16:00
 */
@ConfigurationProperties(prefix = "i2f.springcloud.discovery.server")
@Data
@NoArgsConstructor
public class DiscoveryServerProperties {
    private String secretKey = "123456";
    private long keepaliveSeconds = 30;

    private RedisManagerProperties redisManager=new RedisManagerProperties();

    @Data
    @NoArgsConstructor
    public static class RedisManagerProperties{
        private String prefix="";
    }
}
