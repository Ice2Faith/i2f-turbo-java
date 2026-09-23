package i2f.ai.std.mcp.impl;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/7/5 16:02
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableAppMcpToolProvider extends AbstractAppMcpToolProvider implements BaseMutator<ListableAppMcpToolProvider> {
    public static final String DEFAULT_NAME = "app_list";
    public static final String DEFAULT_DESCRIPTION = "application embed tools";
    protected String name = DEFAULT_NAME;
    protected String description = DEFAULT_DESCRIPTION;
    protected final CopyOnWriteArrayList<ToolRawDefinition> tools = new CopyOnWriteArrayList<>();
    protected IJsonSerializer jsonSerializer;
    protected IProxyInvocationHandler invocationHandler;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ToolDefinition> getTools() {
        return new ArrayList<>(tools);
    }
}
