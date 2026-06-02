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
public class OpenAiUserMessage implements OpenAiMessage {
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
