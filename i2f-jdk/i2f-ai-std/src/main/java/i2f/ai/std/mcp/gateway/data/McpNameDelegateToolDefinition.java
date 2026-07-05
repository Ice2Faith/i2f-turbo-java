package i2f.ai.std.mcp.gateway.data;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.definition.impl.DelegateToolDefinition;
import i2f.ai.std.tool.schema.JsonSchema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:53
 * @desc
 */
public class McpNameDelegateToolDefinition extends DelegateToolDefinition {
    protected String name;

    public McpNameDelegateToolDefinition(String name, ToolDefinition delegate) {
        super(delegate);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getJsonSchema() {
        Map<String, Object> raw = super.getJsonSchema();
        Map<String, Object> ret = new LinkedHashMap<>(raw);
        ret.put(JsonSchema.SchemaField.NAME, name);
        return ret;
    }
}
