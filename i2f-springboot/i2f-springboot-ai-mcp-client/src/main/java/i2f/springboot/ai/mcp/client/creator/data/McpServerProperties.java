package i2f.springboot.ai.mcp.client.creator.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/10/15 15:55
 * @desc mcp.json
 */
@ConfigurationProperties(prefix = McpServerProperties.CONFIG_PREFIX)
@Data
@NoArgsConstructor
public class McpServerProperties {
    public static final String CONFIG_PREFIX="i2f.springboot.ai.mcp.client.creator";
    protected Map<String, McpServerItemProperties> mcpServers;
    protected Map<String, McpServerItemProperties> servers;
}
