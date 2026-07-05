package i2f.ai.std.mcp.gateway.data;

import i2f.ai.std.tool.ToolBaseCallRequest;

/**
 * @author Ice2Faith
 * @date 2026/7/5 15:36
 * @desc
 */
public class McpNameDelegateToolBaseCallRequest extends ToolBaseCallRequest {
    protected String realName;
    protected ToolBaseCallRequest delegate;

    public McpNameDelegateToolBaseCallRequest(String realName, ToolBaseCallRequest delegate) {
        this.realName = realName;
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return realName;
    }

    @Override
    public void setName(String name) {
        realName = name;
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void setId(String id) {
        delegate.setId(id);
    }

    @Override
    public String getArguments() {
        return delegate.getArguments();
    }

    @Override
    public void setArguments(String arguments) {
        delegate.setArguments(arguments);
    }

    public ToolBaseCallRequest getDelegate() {
        return delegate;
    }
}
