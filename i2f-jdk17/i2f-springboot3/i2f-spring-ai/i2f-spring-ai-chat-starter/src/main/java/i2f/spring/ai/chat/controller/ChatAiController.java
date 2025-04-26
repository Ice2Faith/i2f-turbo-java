package i2f.spring.ai.chat.controller;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.properties.ChatAiProperties;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:30
 * @desc
 */
@ConditionalOnExpression("${i2f.ai.chat.api.chat.enable:true}")
@Data
@NoArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatAiController {
    public static final String HEADER_CHAT_AI_SESSION_ID_KEY = "chatAiSessionId";

    @Autowired
    private ChatAiProperties chatAiProperties;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatAiSessionRepository chatAiSessionRepository;

    @Autowired
    private ChatAiAuthProvider chatAiAuthProvider;

    @GetMapping(value = "/chat", produces = "text/html; charset=utf-8")
    public String getChat(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "message") String message,
                          @RequestParam(value = "sessionId", required = false) String sessionId) {
        return exec(request, response, message, sessionId)
                .call()
                .content();
    }

    @GetMapping(value = "/stream", produces = "text/html; charset=utf-8")
    public Flux<String> getStream(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "message") String message,
                                  @RequestParam(value = "sessionId", required = false) String sessionId) {

        return exec(request, response, message, sessionId)
                .stream()
                .content();
    }

    @PostMapping(value = "/chat", produces = "text/html; charset=utf-8")
    public String postChat(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody String message,
                           @RequestParam(value = "sessionId", required = false) String sessionId) {
        return exec(request, response, message, sessionId)
                .call()
                .content();
    }

    @PostMapping(value = "/stream", produces = "text/html; charset=utf-8")
    public Flux<String> postStream(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody String message,
                                   @RequestParam(value = "sessionId", required = false) String sessionId) {

        return exec(request, response, message, sessionId)
                .stream()
                .content();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(value = "/api/chat", produces = "application/json; charset=utf-8")
    public String getJsonChat(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "message") String message,
                              @RequestParam(value = "sessionId", required = false) String sessionId) {
        return exec(request, response, message, sessionId)
                .call()
                .content();
    }

    @GetMapping(value = "/api/stream", produces = "application/json; charset=utf-8")
    public Flux<String> getJsonStream(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "message") String message,
                                      @RequestParam(value = "sessionId", required = false) String sessionId) {

        return exec(request, response, message, sessionId)
                .stream()
                .content();
    }

    @PostMapping(value = "/api/chat", produces = "application/json; charset=utf-8")
    public String postJsonChat(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody String message,
                               @RequestParam(value = "sessionId", required = false) String sessionId) {
        return exec(request, response, message, sessionId)
                .call()
                .content();
    }

    @PostMapping(value = "/api/stream", produces = "application/json; charset=utf-8")
    public Flux<String> postJsonStream(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody String message,
                                       @RequestParam(value = "sessionId", required = false) String sessionId) {

        return exec(request, response, message, sessionId)
                .stream()
                .content();
    }

    public ChatClient.ChatClientRequestSpec exec(HttpServletRequest request, HttpServletResponse response,
                                                 String message, String sessionId) {
        String conversationId = getOrNewSessionId(request, response, sessionId);
        response.setHeader(HEADER_CHAT_AI_SESSION_ID_KEY, conversationId);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_CHAT_AI_SESSION_ID_KEY);
        return chatClient.prompt()
                .advisors(e -> e.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .user(message);
    }

    public String getOrNewSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        if (chatAiProperties.isAllowCookieSessionId()) {
            if (sessionId == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        String name = cookie.getName();
                        if (HEADER_CHAT_AI_SESSION_ID_KEY.equals(name)) {
                            String value = cookie.getValue();
                            if (value != null && !value.isEmpty()) {
                                sessionId = value;
                                break;
                            }
                        }
                    }
                }
            }
        }

        String userId = chatAiAuthProvider.getUserId(request, response);
        if (sessionId == null) {
            if (chatAiProperties.isAllowAutoCreateSessionId()) {
                sessionId = chatAiSessionRepository.create(userId);
            }
        }

        if (sessionId == null) {
            throw new IllegalStateException("invalid session id!");
        }

        if (chatAiProperties.isAllowCookieSessionId()) {
            Cookie cookie = new Cookie(HEADER_CHAT_AI_SESSION_ID_KEY, sessionId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
            response.addCookie(cookie);
        }

        chatAiSessionRepository.save(userId, sessionId);

        return sessionId;
    }
}
