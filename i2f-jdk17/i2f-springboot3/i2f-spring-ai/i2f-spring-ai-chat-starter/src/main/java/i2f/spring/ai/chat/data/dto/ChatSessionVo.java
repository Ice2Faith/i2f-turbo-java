package i2f.spring.ai.chat.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/4/26 16:12
 * @desc
 */
@Data
@NoArgsConstructor
public class ChatSessionVo {
    protected String id;
    protected String title;

    public ChatSessionVo(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
