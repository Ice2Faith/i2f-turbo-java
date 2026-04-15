package i2f.ai.std.memory.impl;

import i2f.ai.std.memory.AiChatMemory;
import i2f.ai.std.model.message.AiMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Ice2Faith
 * @date 2026/4/1 21:21
 * @desc
 */
@Data
@NoArgsConstructor
public class InMemoryAiChatMemory implements AiChatMemory {
    protected final ConcurrentHashMap<String, ConcurrentLinkedDeque<AiMessage>> store = new ConcurrentHashMap<>();
    protected int maxConversationMessageCount = 20;

    public InMemoryAiChatMemory(int maxConversationMessageCount) {
        this.maxConversationMessageCount = maxConversationMessageCount;
    }

    public InMemoryAiChatMemory maxConversationMessageCount(int maxConversationMessageCount) {
        this.maxConversationMessageCount = maxConversationMessageCount;
        return this;
    }

    @Override
    public void add(String conversationId, AiMessage message) {
        ConcurrentLinkedDeque<AiMessage> list = store.computeIfAbsent(conversationId, k -> new ConcurrentLinkedDeque<>());
        list.add(message);
        if (maxConversationMessageCount > 0) {
            while (list.size() > maxConversationMessageCount) {
                list.removeFirst();
            }
        }
    }

    @Override
    public void clear(String conversationId) {
        store.remove(conversationId);
    }

    @Override
    public List<AiMessage> get(String conversationId) {
        ConcurrentLinkedDeque<AiMessage> list = store.get(conversationId);
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }
}
