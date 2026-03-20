package i2f.extension.ai.dashscope.impl;

import i2f.ai.std.ChatAi;
import i2f.extension.ai.dashscope.tool.DashScopeToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 8:53
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeChatAi implements ChatAi {
    protected String apiKey;
    protected String system;
    protected String model = DashScopeAi.DEFAULT_MODEL;
    protected DashScopeAi.GenResultFormat resultFormat = DashScopeAi.GenResultFormat.TEXT;
    protected DashScopeAi.GenResponseFormat responseFormat = DashScopeAi.GenResponseFormat.TEXT;
    protected String respJsonAdditionalUserMessage = DashScopeAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, DashScopeToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String question) {
        DashScopeAi ai = new DashScopeAi();
        ai.setApiKey(apiKey);
        ai.setSystem(system);
        ai.setModel(model);
        ai.setResultFormat(resultFormat);
        ai.setResponseFormat(responseFormat);
        ai.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        ai.setToolDefinitionMap(toolDefinitionMap);
        ai.setQuestion(question);
        return ai.callAsString();
    }
}
