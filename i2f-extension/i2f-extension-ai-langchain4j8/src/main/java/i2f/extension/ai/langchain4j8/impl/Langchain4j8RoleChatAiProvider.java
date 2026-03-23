package i2f.extension.ai.langchain4j8.impl;

import i2f.ai.std.RoleChatAi;
import i2f.ai.std.RoleChatAiProvider;
import i2f.context.impl.ListableContext;
import i2f.context.std.IContext;
import i2f.extension.ai.langchain4j8.tool.Langchain4j8ToolDefinition;
import i2f.extension.ai.langchain4j8.tool.Langchain4j8ToolHelper;
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
public class Langchain4j8RoleChatAiProvider implements RoleChatAiProvider {
    protected String baseUrl = Langchain4j8Ai.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String model = "qwen-plus";
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "langchain4j8";
    }

    @Override
    public RoleChatAi getChatAi() {
        Langchain4j8RoleChatAi ret = new Langchain4j8RoleChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
        if (model != null) {
            ret.setModel(model);
        }
        if (context != null) {
            Map<String, Langchain4j8ToolDefinition> map = Langchain4j8ToolHelper.parseTools(context);
            ret.setToolDefinitionMap(map);
        }
        return ret;
    }
}
