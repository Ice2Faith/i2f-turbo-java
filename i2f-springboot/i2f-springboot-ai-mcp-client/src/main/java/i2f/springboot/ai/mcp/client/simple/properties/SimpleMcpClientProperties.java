package i2f.springboot.ai.mcp.client.simple.properties;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/21 15:04
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = SimpleMcpClientProperties.CONFIG_PREFIX)
public class SimpleMcpClientProperties {
    public static final String CONFIG_PREFIX = "i2f.springboot.ai.mcp.client.simple";
    protected List<InstanceConfig> instances;

    @Data
    @NoArgsConstructor
    public static class InstanceConfig {
        protected Boolean enable;
        protected String baseUrl;
        protected String appId;
        protected String appKey;
        protected String hmacName = HttpSimpleMcpConstants.DEFAULT_HMAC_NAME;
        protected String name;
        protected String description;
    }
}
