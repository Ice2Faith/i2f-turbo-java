package i2f.spring.ai.chat.controller;

import i2f.spring.ai.chat.auth.ChatAiAuthProvider;
import i2f.spring.ai.chat.data.dto.ChatSessionVo;
import i2f.spring.ai.chat.session.ChatAiSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private ChatMemory chatMemory;

    @Autowired
    private ChatAiAuthProvider chatAiAuthProvider;

    @PostMapping(value = "/create", produces = "application/json; charset=utf-8")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        String userId = chatAiAuthProvider.getUserId(request, response);
        String sessionId = chatAiSessionRepository.create(userId);
        return sessionId;
    }

    @GetMapping(value = "/list")
    public List<ChatSessionVo> list(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "lastN", required = false) Integer lastN) {
        if (lastN == null) {
            lastN = Integer.MAX_VALUE;
        }
        String userId = chatAiAuthProvider.getUserId(request, response);
        List<String> sessionIds = chatAiSessionRepository.list(userId, lastN);

        List<ChatSessionVo> ret = new ArrayList<>();
        for (String sessionId : sessionIds) {
            String title = null;
            List<Message> messages = chatMemory.get(sessionId);
            for (Message message : messages) {
                if (message.getMessageType() == MessageType.USER) {
                    title = message.getText();
                }
            }
            ChatSessionVo vo = new ChatSessionVo(sessionId, title);
            ret.add(vo);
        }
        return ret;
    }

    @DeleteMapping(value = "/clear")
    public boolean clear(HttpServletRequest request, HttpServletResponse response) {
        String userId = chatAiAuthProvider.getUserId(request, response);
        chatAiSessionRepository.clear(userId);
        return true;
    }
}
