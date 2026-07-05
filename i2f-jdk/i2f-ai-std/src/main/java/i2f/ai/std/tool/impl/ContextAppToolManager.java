package i2f.ai.std.tool.impl;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;
import i2f.mutator.BaseMutator;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 15:49
 * @desc
 */
@Data
@NoArgsConstructor
public class ContextAppToolManager extends AbstractAppToolManager implements BaseMutator<ContextAppToolManager> {
    protected IContext context;
    protected JsonSchemaAnnotationResolver annotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
    protected IJsonSerializer jsonSerializer;
    protected IProxyInvocationHandler invocationHandler;

    @Override
    public List<ToolDefinition> getTools() {
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        List<ToolDefinition> ret = new ArrayList<>(definitionMap.values());
        return ret;
    }

}
