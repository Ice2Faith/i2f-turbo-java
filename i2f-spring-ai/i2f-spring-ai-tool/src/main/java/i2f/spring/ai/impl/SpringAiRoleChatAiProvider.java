package i2f.spring.ai.impl;


import i2f.ai.std.RoleChatAi;
import i2f.ai.std.RoleChatAiProvider;
import i2f.context.impl.ListableContext;
import i2f.context.std.IContext;
import i2f.spring.ai.tool.SpringAiToolDefinition;
import i2f.spring.ai.tool.SpringAiToolHelper;
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
public class SpringAiRoleChatAiProvider implements RoleChatAiProvider {
    protected String baseUrl = SpringAiAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = "qwen-plus";
    protected String respJsonAdditionalUserMessage = SpringAiAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "springai";
    }

    @Override
    public RoleChatAi getChatAi() {
        SpringAiRoleChatAi ret = new SpringAiRoleChatAi();
        ret.setBaseUrl(baseUrl);
        ret.setApiKey(apiKey);
        if (model != null) {
            ret.setModel(model);
        }
        if (respJsonAdditionalUserMessage != null && !respJsonAdditionalUserMessage.isEmpty()) {
            ret.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        }
        if (context != null) {
            Map<String, SpringAiToolDefinition> map = SpringAiToolHelper.parseTools(context);
            ret.setToolDefinitionMap(map);
        }
        return ret;
    }
}
