package i2f.extension.ai.langchain4j.impl;

import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
import i2f.context.impl.ListableContext;
import i2f.context.std.IContext;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolDefinition;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 8:52
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4jChatAiProvider implements ChatAiProvider {
    protected String baseUrl = Langchain4jAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = "qwen-plus";
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "langchain4j";
    }

    @Override
    public ChatAi getChatAi() {
        Langchain4jChatAi ret = new Langchain4jChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
        ret.setSystem(system);
        if (model != null) {
            ret.setModel(model);
        }
        if (context != null) {
            Map<String, Langchain4jToolDefinition> map = Langchain4jToolHelper.parseTools(context);
            ret.setToolDefinitionMap(map);
        }
        return ret;
    }
}
