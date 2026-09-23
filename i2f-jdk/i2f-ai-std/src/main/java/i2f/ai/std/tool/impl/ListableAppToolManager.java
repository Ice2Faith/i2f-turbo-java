package i2f.ai.std.tool.impl;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/7/5 15:49
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableAppToolManager extends AbstractAppToolManager implements BaseMutator<ListableAppToolManager> {
    protected final CopyOnWriteArrayList<ToolRawDefinition> tools = new CopyOnWriteArrayList<>();
    protected IJsonSerializer jsonSerializer;
    protected IProxyInvocationHandler invocationHandler;

    @Override
    public List<ToolDefinition> getTools() {
        return new CopyOnWriteArrayList<>(tools);
    }

}
