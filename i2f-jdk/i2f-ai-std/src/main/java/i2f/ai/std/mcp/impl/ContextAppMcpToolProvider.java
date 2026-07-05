package i2f.ai.std.mcp.impl;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.impl.ListableNamingContext;
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
 * @date 2026/7/5 16:02
 * @desc
 */
@Data
@NoArgsConstructor
public class ContextAppMcpToolProvider extends AbstractAppMcpToolProvider implements BaseMutator<ContextAppMcpToolProvider> {
    public static final String DEFAULT_NAME = "app-context";
    public static final String DEFAULT_DESCRIPTION = "application context embed tools";
    protected String name = DEFAULT_NAME;
    protected String description = DEFAULT_DESCRIPTION;
    protected IContext context = new ListableNamingContext();
    protected JsonSchemaAnnotationResolver annotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
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
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        return new ArrayList<>(definitionMap.values());
    }

}
