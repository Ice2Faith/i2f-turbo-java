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
import java.util.Set;

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
                // 先根据 tag 排除，再根据 tag 纳入，最后按照更加严谨的表达式匹配
                // 可以配合使用，优先级 表达式 > include ? exclude
                List<String> excludeTags = rule.getExcludeTags();
                if (excludeTags != null && !excludeTags.isEmpty()) {
                    Set<String> tags = tool.getTags();
                    if (tags != null && !tags.isEmpty()) {
                        for (String includeTag : excludeTags) {
                            if (tags.contains(includeTag)) {
                                ret = rule.isExpose();
                            }
                        }
                    }
                }

                List<String> includeTags = rule.getIncludeTags();
                if (includeTags != null && !includeTags.isEmpty()) {
                    Set<String> tags = tool.getTags();
                    if (tags != null && !tags.isEmpty()) {
                        for (String includeTag : includeTags) {
                            if (tags.contains(includeTag)) {
                                ret = rule.isExpose();
                            }
                        }
                    }
                }

                String pattern = rule.getPattern();
                if (pattern != null && !pattern.isEmpty()) {
                    if( matcher.matches(name, pattern)) {
                        ret = rule.isExpose();
                    }
                }

                if(ret!=null){
                    break;
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
