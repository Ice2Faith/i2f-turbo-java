package i2f.ai.std.model.message.impl;

import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:23
 * @desc
 */
@Data
@NoArgsConstructor
public class ToolMessage implements AiMessage {
    protected String id;
    protected String text;
    protected ToolCallRequest request;
    protected ToolRawDefinition definition;
    protected Object rawMessage;

    public ToolMessage(String text) {
        this.text = text;
    }

    public ToolMessage(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public Type type() {
        return Type.TOOL;
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
