package i2f.spring.ai.chat.controller;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:30
 * @desc
 */
@Data
@NoArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatAiController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatAiSessionRepository chatAiSessionRepository;

    @Autowired
    private ChatAiAuthProvider chatAiAuthProvider;

    @RequestMapping(value = "/chat",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "text/html; charset=utf-8")
    public String chat(HttpServletRequest request, HttpServletResponse response,
                       String message,
                       @RequestParam(required = false) String sessionId) {

        String conversationId = getOrNewSessionId(request, response, sessionId);
        return chatClient.prompt()
                .advisors(e -> e.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .user(message)
                .call()
                .content();
    }

    @RequestMapping(value = "/stream",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "text/html; charset=utf-8")
    public Flux<String> stream(HttpServletRequest request, HttpServletResponse response,
                               String message,
                               @RequestParam(required = false) String sessionId) {
        String conversationId = getOrNewSessionId(request, response, sessionId);
        return chatClient.prompt()
                .advisors(e -> e.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .user(message)
                .stream()
                .content();
    }

    public String getOrNewSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        String userId = chatAiAuthProvider.getUserId(request, response);
        if (sessionId == null) {
            sessionId = chatAiSessionRepository.create(userId);
        }
        chatAiSessionRepository.save(userId, sessionId);
        return sessionId;
    }
}
