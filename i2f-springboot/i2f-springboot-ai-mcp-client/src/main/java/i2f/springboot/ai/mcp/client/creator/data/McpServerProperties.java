package i2f.springboot.ai.mcp.client.official.v2024.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/10/15 15:55
 * @desc mcp.json
 */
@Data
@NoArgsConstructor
public class McpServerProperties {
    protected Map<String, McpServerItemProperties> mcpServers;
    protected Map<String, McpServerItemProperties> servers;
}
