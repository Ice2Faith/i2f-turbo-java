package i2f.ai.std.mcp.gateway.data;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.definition.impl.DelegateToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;

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
    public FunctionJsonSchema getJsonSchema() {
        FunctionJsonSchema raw = super.getJsonSchema();
        FunctionJsonSchema ret = raw.copyOf();
        ret.setName(name);
        return ret;
    }
}
