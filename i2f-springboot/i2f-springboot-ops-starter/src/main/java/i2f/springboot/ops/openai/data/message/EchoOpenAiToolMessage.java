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

    public EchoOpenAiToolMessage(OpenAiToolMessage message, OpenAiToolCallFunction function) {
        this.message = message;
        this.function = function;
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String content() {
        return "call_id=" + message.getTool_call_id() + "\n"
                + "function_name=:" + function.getName() + "\n"
                + "call_result=" + message.getContent();
    }
}
