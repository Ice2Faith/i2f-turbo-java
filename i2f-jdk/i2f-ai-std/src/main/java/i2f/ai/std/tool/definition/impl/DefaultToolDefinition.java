package i2f.ai.std.tool.definition.impl;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/7/13 10:32
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultToolDefinition implements ToolDefinition, BaseMutator<DefaultToolDefinition> {
    protected String name;
    protected String description;
    protected FunctionJsonSchema jsonSchema;
    protected Set<String> tags;
}
