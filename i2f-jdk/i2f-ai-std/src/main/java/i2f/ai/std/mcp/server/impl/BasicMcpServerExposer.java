package i2f.ai.std.mcp.server.impl;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.mcp.server.McpServerExposer;
import i2f.ai.std.mcp.server.rule.McpServerExposeRule;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.match.impl.AntMatcher;
import i2f.match.std.IMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/28 19:51
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicMcpServerExposer implements McpServerExposer {
    protected List<McpServerExposeRule> rules;
    protected boolean defaultExpose = true;
    protected IMatcher matcher = new AntMatcher(".");

    @Override
    public boolean expose(ToolDefinition tool) {
        Boolean ret = null;
        String name = tool.getName();
        if (rules != null) {
            for (McpServerExposeRule rule : rules) {
                String pattern = rule.getPattern();
                if (pattern == null || matcher.matches(name, pattern)) {
                    ret = rule.isExpose();
                }
            }
        }
        if (ret != null) {
            return ret;
        }
        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(tool);
        if (rawTool != null) {
            Method method = rawTool.getBindMethod();
            if (method != null) {
                McpServerExpose ann = method.getDeclaredAnnotation(McpServerExpose.class);
                if (ann == null) {
                    Class<?> declaringClass = method.getDeclaringClass();
                    ann = declaringClass.getDeclaredAnnotation(McpServerExpose.class);
                }
                if (ann != null) {
                    ret = ann.value();
                }
            }
        }
        if (ret != null) {
            return ret;
        }
        return defaultExpose;
    }
}
