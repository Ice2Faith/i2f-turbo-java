package i2f.ai.std.tool;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/10 10:23
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolBaseCallRequest {
    protected String id;
    protected String name;
    protected String arguments;
}
