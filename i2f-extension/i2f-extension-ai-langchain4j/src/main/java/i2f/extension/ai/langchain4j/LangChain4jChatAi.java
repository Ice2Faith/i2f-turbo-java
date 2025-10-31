package i2f.extension.ai.langchain4j;

import dev.langchain4j.model.chat.ChatModel;
import i2f.ai.std.ChatAi;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/10/31 20:12
 * @desc
 */
@Data
@NoArgsConstructor
public class LangChain4jChatAi implements ChatAi {
    protected ChatModel model;

    public LangChain4jChatAi(ChatModel model) {
        this.model = model;
    }

    @Override
    public String chat(String question) {
        return model.chat(question);
    }
}
