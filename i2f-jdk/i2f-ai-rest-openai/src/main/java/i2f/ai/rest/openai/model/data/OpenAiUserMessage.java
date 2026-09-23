package i2f.ai.rest.openai.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiUserMessage implements OpenAiMessage, BaseMutator<OpenAiUserMessage> {
    protected final String role = OpenAiConsts.USER;
    protected String content;

    public OpenAiUserMessage(String content) {
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
