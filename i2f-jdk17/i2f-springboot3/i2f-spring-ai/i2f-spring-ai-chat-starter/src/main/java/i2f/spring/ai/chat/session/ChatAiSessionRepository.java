package i2f.spring.ai.chat.session;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:49
 * @desc
 */
public interface ChatAiSessionRepository {
    String create(String userId);

    void save(String userId, String sessionId);

    List<String> list(String userId, int lastN);

    void clear(String userId);
}
