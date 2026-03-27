package i2f.extension.ai.langchain4j.impl;

import i2f.ai.std.RoleChatAi;
import i2f.ai.std.RoleChatAiProvider;
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
public class Langchain4jRoleChatAiProvider implements RoleChatAiProvider {
    protected String baseUrl = Langchain4jAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String model = "qwen-plus";
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "langchain4j";
    }

    @Override
    public RoleChatAi getChatAi() {
        Langchain4jRoleChatAi ret = new Langchain4jRoleChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
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
