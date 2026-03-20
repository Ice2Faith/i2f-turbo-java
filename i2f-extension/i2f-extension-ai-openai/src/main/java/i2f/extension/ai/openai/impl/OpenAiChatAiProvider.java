package i2f.extension.ai.openai.impl;


import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
import i2f.context.impl.ListableContext;
import i2f.context.std.IContext;
import i2f.extension.ai.openai.tool.OpenAiToolDefinition;
import i2f.extension.ai.openai.tool.OpenAiToolHelper;
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
public class OpenAiChatAiProvider implements ChatAiProvider {
    protected String baseUrl = OpenAiAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = "qwen-plus";
    protected String respJsonAdditionalUserMessage = OpenAiAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "openai";
    }

    @Override
    public ChatAi getChatAi() {
        OpenAiChatAi ret = new OpenAiChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
        ret.setSystem(system);
        if (model != null) {
            ret.setModel(model);
        }
        if (respJsonAdditionalUserMessage != null && !respJsonAdditionalUserMessage.isEmpty()) {
            ret.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        }
        if (context != null) {
            Map<String, OpenAiToolDefinition> map = OpenAiToolHelper.parseTools(context);
            ret.setToolDefinitionMap(map);
        }
        return ret;
    }
}
