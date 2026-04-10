package i2f.ai.std.tool;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolRawDefinition extends ToolBaseDefinition {
    protected transient Method bindMethod;
    protected transient Class<?> bindClass;
    protected transient Object bindTarget;
    protected Set<String> tags;
}