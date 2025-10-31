package i2f.spring.ai;

import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
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
public class SpringChatAiProvider implements ChatAiProvider {
    public static final String NAME = "spring";
    protected String name = NAME;
    protected ChatModel model;

    public SpringChatAiProvider(ChatModel model) {
        this.model = model;
    }

    public SpringChatAiProvider(String name, ChatModel model) {
        this.name = name;
        this.model = model;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ChatAi getChatAi() {
        return new SpringChatAi(model);
    }
}
