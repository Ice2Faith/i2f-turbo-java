package i2f.springboot.ai.mcp.client.properties;

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
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.client.simple")
public class SimpleMcpClientProperties {
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
