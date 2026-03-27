package i2f.ai.std.model.message.tool;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:31
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolCallRequest {
    protected String id;
    protected String name;
    protected String arguments;
    protected Object rawRequest;
}
