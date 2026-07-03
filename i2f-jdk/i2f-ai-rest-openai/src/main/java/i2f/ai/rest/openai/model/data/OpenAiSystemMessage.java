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
public class OpenAiSystemMessage implements OpenAiMessage, BaseBuilder<OpenAiSystemMessage> {
    protected final String role = OpenAiConsts.SYSTEM;
    protected String content;

    public OpenAiSystemMessage(String content) {
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
