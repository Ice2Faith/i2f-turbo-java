package i2f.springboot.ai.mcp.client.official.v2024.data;

import i2f.ai.std.tags.AiTagRule;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/10/15 16:06
 * @desc
 */
@Data
@NoArgsConstructor
public class McpServerMetaProperties {
    protected List<AiTagRule> tagRules;
    protected Map<String, Object> properties;
}
