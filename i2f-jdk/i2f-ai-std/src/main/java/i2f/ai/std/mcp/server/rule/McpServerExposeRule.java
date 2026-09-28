package i2f.ai.std.mcp.server.rule;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/28 19:53
 * @desc
 */
@Data
@NoArgsConstructor
public class McpServerExposeRule {
    protected boolean expose;
    protected String pattern;
}
