package i2f.spring.ai.chat.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.MessageType;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:12
 * @desc
 */
@Data
@NoArgsConstructor
public class ChatHistoryVo {
    protected MessageType messageType;
    protected String content;

    public ChatHistoryVo(MessageType messageType, String content) {
        this.messageType = messageType;
        this.content = content;
    }
}
