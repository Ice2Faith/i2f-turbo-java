package i2f.ai.std.model.message.impl;

import i2f.ai.std.model.message.AiMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:23
 * @desc
 */
@Data
@NoArgsConstructor
public class UserMessage implements AiMessage {
    protected String text;
    protected Object rawMessage;

    public UserMessage(String text) {
        this.text = text;
    }

    @Override
    public Type type() {
        return Type.USER;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public Object rawMessage() {
        return rawMessage;
    }
}
