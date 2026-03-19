package i2f.extension.ai.dashscope.impl;

import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
import i2f.context.impl.ListableContext;
import i2f.context.std.IContext;
import i2f.extension.ai.dashscope.tool.ToolDefinition;
import i2f.extension.ai.dashscope.tool.ToolHelper;
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
public class DashScopeChatAiProvider implements ChatAiProvider {
    protected String apiKey;
    protected String system;
    protected String model = "qwen-plus";
    protected DashScopeAi.GenResultFormat resultFormat = DashScopeAi.GenResultFormat.TEXT;
    protected DashScopeAi.GenResponseFormat responseFormat = DashScopeAi.GenResponseFormat.TEXT;
    protected String respJsonAdditionalUserMessage = DashScopeAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected IContext context = new ListableContext();

    @Override
    public String name() {
        return "dashscope";
    }

    @Override
    public ChatAi getChatAi() {
        DashScopeChatAi ret = new DashScopeChatAi();
        ret.setApiKey(apiKey);
        ret.setSystem(system);
        if (model != null) {
            ret.setModel(model);
        }
        if (resultFormat != null) {
            ret.setResultFormat(resultFormat);
        }
        if (responseFormat != null) {
            ret.setResponseFormat(responseFormat);
        }
        if (respJsonAdditionalUserMessage != null && !respJsonAdditionalUserMessage.isEmpty()) {
            ret.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        }
        if (context != null) {
            Map<String, ToolDefinition> map = ToolHelper.parseTools(context);
            ret.setToolDefinitionMap(map);
        }
        return ret;
    }
}
