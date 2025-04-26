package i2f.spring.ai.chat.auth.impl;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:40
 * @desc
 */
public class CookieChatAiAuthProvider implements ChatAiAuthProvider {
    public static final String COOKIE_CHAT_AI_USER_ID_KEY = "chatAiUserId";

    @Override
    public String getUserId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userId = null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (COOKIE_CHAT_AI_USER_ID_KEY.equals(name)) {
                String value = cookie.getValue();
                if (value != null && !value.isEmpty()) {
                    userId = value;
                    break;
                }
            }
        }
        if (userId == null || userId.isEmpty()) {
            userId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }

        Cookie cookie = new Cookie(COOKIE_CHAT_AI_USER_ID_KEY, userId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
        response.addCookie(cookie);

        return userId;
    }
}
