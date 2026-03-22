package i2f.extension.ai.dashscope.tool;

import com.alibaba.dashscope.tools.ToolFunction;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
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
    protected String functionName;
    protected List<String> functionParameterNames;
    protected Method bindMethod;
    protected Class<?> bindClass;
    protected Object bindTarget;

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