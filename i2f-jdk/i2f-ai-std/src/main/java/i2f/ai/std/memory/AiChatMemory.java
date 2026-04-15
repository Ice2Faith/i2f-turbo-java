package i2f.ai.std.memory;

import i2f.ai.std.model.message.AiMessage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/4/1 21:18
 * @desc
 */
public interface AiChatMemory {
    void add(String conversationId, AiMessage message);

    default String newConversationId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    default void add(String conversationId, Collection<AiMessage> messages) {
        for (AiMessage message : messages) {
            add(conversationId, message);
        }
    }

    void clear(String conversationId);

    List<AiMessage> get(String conversationId);
}
