package i2f.ai.rest.openai.model.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolMessage implements OpenAiMessage, BaseBuilder<OpenAiToolMessage> {
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
