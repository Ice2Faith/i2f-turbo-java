package i2f.spring.ai;

import i2f.ai.std.ChatAi;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;

/**
 * @author Ice2Faith
 * @date 2025/10/31 20:06
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringChatAi implements ChatAi {
    protected ChatModel model;

    public SpringChatAi(ChatModel model) {
        this.model = model;
    }

    @Override
    public String chat(String question) {
        return model.call(question);
    }
}
