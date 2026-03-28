package i2f.spring.ai.impl;


import i2f.ai.std.RoleChatAi;
import i2f.spring.ai.tool.SpringAiToolDefinition;
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
public class SpringAiRoleChatAi implements RoleChatAi {
    protected String baseUrl = SpringAiAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String model = SpringAiAi.DEFAULT_MODEL;
    protected String respJsonAdditionalUserMessage = SpringAiAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, SpringAiToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String role, String question) {
        SpringAiAi ai = new SpringAiAi();
        ai.setBaseUrl(baseUrl);
        ai.setApiKey(apiKey);
        ai.setSystem(role);
        ai.setModel(model);
        ai.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        ai.setToolDefinitionMap(toolDefinitionMap);
        ai.setQuestion(question);
        return ai.callAsString();
    }
}
