package i2f.spring.ai.chat.controller;

import i2f.spring.ai.chat.data.dto.ChatHistoryVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:30
 * @desc
 */
@Data
@NoArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatAiHistoryController {

    @Autowired
    private ChatMemory chatMemory;

    @GetMapping(value = "/list/{sessionId}")
    public List<ChatHistoryVo> list(@PathVariable("sessionId") String sessionId,
                                    @RequestParam(value = "lastN", required = false) Integer lastN) {
        if (lastN == null) {
            lastN = Integer.MAX_VALUE;
        }
        List<Message> messages = chatMemory.get(sessionId, lastN);
        return messages.stream()
                .map(e -> new ChatHistoryVo(e.getMessageType(), e.getText()))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/clear/{sessionId}")
    public boolean clear(@PathVariable("sessionId") String sessionId) {
        chatMemory.clear(sessionId);
        return true;
    }
}
