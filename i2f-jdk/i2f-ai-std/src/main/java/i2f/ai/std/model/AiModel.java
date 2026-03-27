package i2f.ai.std.model;

import i2f.ai.std.model.message.impl.AssistantMessage;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:19
 * @desc
 */
public interface AiModel {
    AssistantMessage generate(AiRequest req);
}
