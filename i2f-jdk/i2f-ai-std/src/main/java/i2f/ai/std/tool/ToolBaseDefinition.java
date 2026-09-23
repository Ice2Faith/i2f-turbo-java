package i2f.ai.std.tool;

import i2f.ai.std.tool.definition.impl.DefaultToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:21
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolBaseDefinition extends DefaultToolDefinition {
    protected List<String> parameterNames;
}
