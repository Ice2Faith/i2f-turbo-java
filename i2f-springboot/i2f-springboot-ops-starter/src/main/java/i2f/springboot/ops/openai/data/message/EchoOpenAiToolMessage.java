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
public class EchoOpenAiToolMessage implements OpenAiMessage {
    protected final String role = OpenAiConsts.ECHO_TOOL;
    protected OpenAiToolMessage message;
    protected OpenAiToolCallFunction function;
    protected String content;

    public EchoOpenAiToolMessage(OpenAiToolMessage message, OpenAiToolCallFunction function) {
        this.message = message;
        this.function = function;
        this.content = createContent();
    }

    @Override
    public String role() {
        return role;
    }

    public String createContent() {
        this.content = "call_id=" + message.getTool_call_id() + "\n"
                + "function_name=" + function.getName() + "\n"
                + "call_result=" + message.getContent();
        return this.content;
    }

    @Override
    public String content() {
        return createContent();
    }
}
