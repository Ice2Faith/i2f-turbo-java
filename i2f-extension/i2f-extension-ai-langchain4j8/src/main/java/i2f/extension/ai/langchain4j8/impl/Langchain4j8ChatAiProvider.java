package i2f.extension.ai.langchain4j8.impl;

import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
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
public class Langchain4j8ChatAiProvider implements ChatAiProvider {
    protected String baseUrl = Langchain4j8Ai.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = "qwen-plus";
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "langchain4j8";
    }

    @Override
    public ChatAi getChatAi() {
        Langchain4j8ChatAi ret = new Langchain4j8ChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
        ret.setSystem(system);
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
