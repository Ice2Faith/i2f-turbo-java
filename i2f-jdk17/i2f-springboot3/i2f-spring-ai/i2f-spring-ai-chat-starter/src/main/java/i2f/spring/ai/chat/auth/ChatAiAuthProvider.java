package i2f.spring.ai.chat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:03
 * @desc
 */
public interface ChatAiAuthProvider {
    String getUserId(HttpServletRequest request, HttpServletResponse response);
}
