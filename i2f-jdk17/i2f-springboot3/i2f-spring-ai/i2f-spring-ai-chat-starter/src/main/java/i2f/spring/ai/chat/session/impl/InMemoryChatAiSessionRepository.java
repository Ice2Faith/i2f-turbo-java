package i2f.spring.ai.chat.session.impl;

import i2f.spring.ai.chat.session.ChatAiSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:25
 * @desc
 */
public class InMemoryChatAiSessionRepository implements ChatAiSessionRepository {
    protected final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> sessionMap = new ConcurrentHashMap<>();

    @Override
    public String create(String userId) {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    @Override
    public void save(String userId, String sessionId) {
        if (userId == null || sessionId == null) {
            return;
        }
        sessionMap.computeIfAbsent(userId, e -> new CopyOnWriteArrayList<>())
                .addIfAbsent(sessionId);
    }

    @Override
    public List<String> list(String userId, int lastN) {
        if (userId == null) {
            return new ArrayList<>();
        }
        CopyOnWriteArrayList<String> list = sessionMap.get(userId);
        if (list == null) {
            return new ArrayList<>();
        }
        if (lastN < 0) {
            return new ArrayList<>(list);
        }
        return list.stream().limit(lastN).collect(Collectors.toList());
    }

    @Override
    public void clear(String userId) {
        if (userId == null) {
            return;
        }
        sessionMap.remove(userId);
    }
}
