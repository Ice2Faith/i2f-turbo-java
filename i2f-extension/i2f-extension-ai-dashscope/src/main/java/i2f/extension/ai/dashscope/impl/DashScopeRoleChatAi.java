package i2f.extension.ai.dashscope.impl;

import i2f.ai.std.RoleChatAi;
import i2f.extension.ai.dashscope.tool.ToolDefinition;
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
public class DashScopeRoleChatAi implements RoleChatAi {
    protected String apiKey;
    protected String model = DashScopeAi.DEFAULT_MODEL;
    protected DashScopeAi.GenResultFormat resultFormat = DashScopeAi.GenResultFormat.TEXT;
    protected DashScopeAi.GenResponseFormat responseFormat = DashScopeAi.GenResponseFormat.TEXT;
    protected String respJsonAdditionalUserMessage = DashScopeAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, ToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String role, String question) {
        DashScopeAi ai = new DashScopeAi();
        ai.setApiKey(apiKey);
        ai.setSystem(role);
        ai.setModel(model);
        ai.setResultFormat(resultFormat);
        ai.setResponseFormat(responseFormat);
        ai.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        ai.setQuestion(question);
        ai.setToolDefinitionMap(toolDefinitionMap);
        return ai.callAsString();
    }
}
