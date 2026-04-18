package i2f.extension.ai.dashscope.tool;

import com.alibaba.dashscope.tools.ToolFunction;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 11:25
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeToolDefinition {
    protected ToolFunction function;
    protected ToolRawDefinition rawDefinition;

    public static Map<String, DashScopeToolDefinition> parseTools(IContext context) {
        return DashScopeToolHelper.parseTools(context);
    }

    public static Map<String, DashScopeToolDefinition> parseTools(Collection<Object> beans) {
        return DashScopeToolHelper.parseTools(beans);
    }

    public static Map<String, DashScopeToolDefinition> parseTools(Object... beans) {
        return DashScopeToolHelper.parseTools(beans);
    }
}