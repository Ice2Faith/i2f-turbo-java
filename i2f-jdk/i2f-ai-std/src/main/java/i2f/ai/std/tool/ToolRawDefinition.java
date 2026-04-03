package i2f.ai.std.tool;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolRawDefinition {
    protected Map<String, Object> jsonSchema;
    protected String name;
    protected String description;
    protected Map<String, Object> parametersJsonSchema;
    protected List<String> parameterNames;
    protected transient Method bindMethod;
    protected transient Class<?> bindClass;
    protected transient Object bindTarget;
    protected Set<String> tags;
}