package i2f.extension.ai.langchain4j;

import dev.langchain4j.model.chat.ChatModel;
import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/10/31 20:13
 * @desc
 */
@Data
@NoArgsConstructor
public class LangChain4jChatAiProvider implements ChatAiProvider {
    public static final String NAME = "langchain4j";
    protected String name = NAME;
    protected ChatModel model;

    public LangChain4jChatAiProvider(ChatModel model) {
        this.model = model;
    }

    public LangChain4jChatAiProvider(String name, ChatModel model) {
        this.name = name;
        this.model = model;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ChatAi getChatAi() {
        return new LangChain4jChatAi(model);
    }
}
