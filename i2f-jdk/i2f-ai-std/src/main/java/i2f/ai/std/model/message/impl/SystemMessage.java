package i2f.ai.std.model.message.impl;

import i2f.ai.std.model.message.AiMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SystemMessage implements AiMessage {
    protected String text;
    protected Object rawMessage;

    public SystemMessage(String text) {
        this.text = text;
    }

    @Override
    public Type type() {
        return Type.SYSTEM;
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
