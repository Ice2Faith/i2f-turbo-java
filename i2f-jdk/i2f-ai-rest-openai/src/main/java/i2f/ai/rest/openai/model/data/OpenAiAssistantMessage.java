package i2f.ai.rest.openai.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiAssistantMessage implements OpenAiMessage, BaseMutator<OpenAiAssistantMessage> {
    protected final String role = OpenAiConsts.ASSISTANT;
    protected String content;
    protected List<OpenAiToolCall> tool_calls;

    public OpenAiAssistantMessage(String content) {
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
