package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 14:18
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolApprovalDto {
    protected String tool_call_id;
    protected String functionName;
    protected boolean reject;
    protected String rejectReason;
}
