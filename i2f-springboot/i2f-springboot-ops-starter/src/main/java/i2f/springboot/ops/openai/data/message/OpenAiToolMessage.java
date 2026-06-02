package i2f.springboot.ops.openai.data.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiToolMessage implements OpenAiMessage {
    protected final String role = OpenAiConsts.TOOL;
    protected String tool_call_id;
    protected String content;

    public OpenAiToolMessage(String tool_call_id, String content) {
        this.tool_call_id = tool_call_id;
        this.content = content;
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String content() {
        return content;
    }
}
