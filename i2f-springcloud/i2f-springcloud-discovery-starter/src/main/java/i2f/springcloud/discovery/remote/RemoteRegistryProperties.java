package i2f.springcloud.discovery.remote;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2025/9/25 14:25
 */
@ConfigurationProperties(prefix = "i2f.springcloud.discovery.registry")
@Data
@NoArgsConstructor
public class RemoteRegistryProperties {
    private String baseUrl = "http://localhost:9999";
    private String registryPath = "/api/registry";
    private String pullPath = "/api/services";
    private String secretKey = "123456";
    private long heartBeatSeconds = 10;
    private long pullServiceSeconds = 30;
    private int order = 0;
}
