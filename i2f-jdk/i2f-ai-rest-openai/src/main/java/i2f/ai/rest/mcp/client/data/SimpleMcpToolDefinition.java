package i2f.ai.rest.mcp.client.data;

import i2f.ai.std.tool.definition.ToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/7/13 10:32
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleMcpToolDefinition implements ToolDefinition {
    protected String name;
    protected String description;
    protected Map<String, Object> jsonSchema;
    protected Set<String> tags;
}
