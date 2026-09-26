package i2f.springboot.ai.mcp.server.stream.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/26 19:53
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.server.stream")
public class OfficialMcpServerProperties {
    protected String serverName = "i2f-mcp-server";
    protected String serverVersion = "1.0.0";

    protected BearerTokenOptions bearerToken = new BearerTokenOptions();

    @Data
    @NoArgsConstructor
    public static class BearerTokenOptions {
        protected boolean enable = true;
        protected List<String> allowTokens;
    }
}
