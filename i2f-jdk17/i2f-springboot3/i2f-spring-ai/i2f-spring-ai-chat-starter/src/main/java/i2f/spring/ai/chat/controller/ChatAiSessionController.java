package i2f.spring.ai.chat.controller;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:30
 * @desc
 */
@ConditionalOnExpression("${i2f.ai.chat.api.session.enable:true}")
@Data
@NoArgsConstructor
@RestController
@RequestMapping("/ai/session")
public class ChatAiSessionController {

    @Autowired
    private ChatAiSessionRepository chatAiSessionRepository;


    @Autowired
    private ChatAiAuthProvider chatAiAuthProvider;

    @PostMapping(value = "/create")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        String userId = chatAiAuthProvider.getUserId(request, response);
        String sessionId = chatAiSessionRepository.create(userId);
        return sessionId;
    }

    @GetMapping(value = "/list")
    public List<String> list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "lastN", required = false) Integer lastN) {
        if (lastN == null) {
            lastN = Integer.MAX_VALUE;
        }
        String userId = chatAiAuthProvider.getUserId(request, response);
        List<String> sessionIds = chatAiSessionRepository.list(userId, lastN);
        return sessionIds;
    }

    @DeleteMapping(value = "/clear")
    public boolean clear(HttpServletRequest request, HttpServletResponse response) {
        String userId = chatAiAuthProvider.getUserId(request, response);
        chatAiSessionRepository.clear(userId);
        return true;
    }
}
