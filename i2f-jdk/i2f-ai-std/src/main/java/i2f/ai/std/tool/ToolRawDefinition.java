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
    protected Map<String, Object> functionJsonSchema;
    protected String functionName;
    protected String functionDescription;
    protected Map<String, Object> functionParametersJsonSchema;
    protected List<String> functionParameterNames;
    protected Method bindMethod;
    protected Class<?> bindClass;
    protected Object bindTarget;
    protected Set<String> tags;
}