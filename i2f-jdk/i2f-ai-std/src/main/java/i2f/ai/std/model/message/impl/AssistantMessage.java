package i2f.ai.std.model.message.impl;

import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:22
 * @desc
 */
@Data
@NoArgsConstructor
public class AssistantMessage implements AiMessage {
    protected String text;
    protected FinishReason finishReason;
    protected List<ToolCallRequest> toolCallRequestList;
    protected Object rawMessage;

    public static enum FinishReason {
        STOP, TOOL_CALL
    }

    public AssistantMessage(String text) {
        this.text = text;
    }

    public AssistantMessage(String text, FinishReason finishReason) {
        this.text = text;
        this.finishReason = finishReason;
    }

    @Override
    public Type type() {
        return Type.ASSISTANT;
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
