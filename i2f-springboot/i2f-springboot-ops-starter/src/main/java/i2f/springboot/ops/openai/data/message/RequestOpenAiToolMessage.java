package i2f.springboot.ops.openai.data.message;

import i2f.ai.rest.openai.model.data.OpenAiMessage;
import i2f.ai.rest.openai.model.data.OpenAiToolCall;
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
public class RequestOpenAiToolMessage implements OpenAiMessage {
    protected final String role = OpsOpenAiConsts.REQUEST_TOOL;
    protected OpenAiToolCall call;
    protected String content;

    public RequestOpenAiToolMessage(OpenAiToolCall call) {
        this.call = call;
        this.content = createContent();
    }

    @Override
    public String role() {
        return role;
    }

    public String createContent() {
        this.content = "call_id=" + call.getId() + "\n" +
                "function_name=" + call.getFunction().getName() + "\n" +
                "arguments=" + call.getFunction().getArguments();
        return this.content;
    }

    @Override
    public String content() {
        return createContent();
    }
}
