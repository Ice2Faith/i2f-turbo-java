package i2f.ai.std.model.message.tool;

import i2f.ai.std.tool.ToolBaseCallRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:31
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolCallRequest extends ToolBaseCallRequest {
    protected transient Object rawRequest;
}
