package i2f.spring.ai.chat.auth.impl;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:04
 * @desc
 */
public class SessionIdChatAiAuthProvider implements ChatAiAuthProvider {

    @Override
    public String getUserId(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return session.getId();
    }

}
