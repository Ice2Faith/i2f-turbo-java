package i2f.springboot.ai.mcp.client.solon.properties;

import i2f.ai.std.tags.AiTagRule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.solon.ai.mcp.McpChannel;
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
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.client.solon")
public class SolonMcpClientProperties {
    public static final String CONFIG_PREFIX = "i2f.springboot.ai.mcp.client.solon";

    protected List<InstanceConfig> instances;

    @Data
    @NoArgsConstructor
    public static class InstanceConfig {
        protected Boolean enable;
        protected Boolean initial;
        protected String url;
        protected Channel channel = Channel.STREAMABLE;
        protected String bearerToken;
        protected Map<String, Object> headers;
        protected String name;
        protected String description;
        protected List<AiTagRule> tagRules;
    }

    public static enum Channel {
        STDIO(McpChannel.STDIO),
        SSE(McpChannel.SSE),
        STREAMABLE(McpChannel.STREAMABLE),
        STREAMABLE_STATELESS(McpChannel.STREAMABLE_STATELESS);

        private String channel;

        private Channel(String channel) {
            this.channel = channel;
        }

        public String channel() {
            return this.channel;
        }
    }
}
