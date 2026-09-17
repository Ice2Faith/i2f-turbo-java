package i2f.springboot.ai.mcp.client.stream.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 15:04
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.client.stream")
public class StreamMcpClientProperties {
    public static final String CONFIG_PREFIX="i2f.springboot.ai.mcp.client.stream";

    protected List<InstanceConfig> instances;

    @Data
    @NoArgsConstructor
    public static class InstanceConfig {
        protected Boolean enable;
        protected Boolean initial;
        protected String url;
        protected String bearerToken;
        protected Map<String, Object> headers;
        protected String name;
        protected String description;
    }

}
