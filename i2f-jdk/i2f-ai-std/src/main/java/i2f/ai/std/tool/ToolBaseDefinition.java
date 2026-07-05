package i2f.ai.std.tool;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:21
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolBaseDefinition implements ToolDefinition, BaseMutator<ToolBaseDefinition> {
    protected Map<String, Object> jsonSchema;
    protected String name;
    protected String description;
    protected Map<String, Object> parametersJsonSchema;
    protected List<String> parameterNames;
}
