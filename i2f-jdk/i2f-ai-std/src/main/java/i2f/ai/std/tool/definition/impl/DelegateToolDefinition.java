package i2f.ai.std.tool.definition.impl;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:51
 * @desc
 */
public class DelegateToolDefinition implements ToolDefinition {
    protected ToolDefinition delegate;

    public DelegateToolDefinition(ToolDefinition delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public FunctionJsonSchema getJsonSchema() {
        return delegate.getJsonSchema();
    }

    public ToolDefinition getDelegate() {
        return delegate;
    }
}
