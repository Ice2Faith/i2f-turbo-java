package i2f.springboot.ai.mcp.server.provider.properties;

import i2f.ai.std.mcp.server.rule.McpServerExposeRule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/28 20:14
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.server.provider")
public class McpServerProviderProperties {
    protected List<McpServerExposeRule> rules;
    protected boolean defaultExpose = true;
}
