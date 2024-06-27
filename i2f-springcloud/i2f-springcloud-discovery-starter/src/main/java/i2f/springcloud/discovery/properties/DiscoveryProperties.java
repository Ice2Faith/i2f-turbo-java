package i2f.springcloud.discovery.properties;

import i2f.springcloud.discovery.provider.DiscoveryServiceInstance;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/27 16:23
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "i2f.springcloud.discovery")
public class DiscoveryProperties {
    private Map<String, List<DiscoveryServiceInstance>> instances = new LinkedHashMap<>();
    private int order = 0;

}
