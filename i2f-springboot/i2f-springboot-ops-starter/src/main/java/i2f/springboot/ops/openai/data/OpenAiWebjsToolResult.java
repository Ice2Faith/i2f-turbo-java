package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/14 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiWebjsToolResult {
    protected String tool_call_id;
    protected String content;
}
