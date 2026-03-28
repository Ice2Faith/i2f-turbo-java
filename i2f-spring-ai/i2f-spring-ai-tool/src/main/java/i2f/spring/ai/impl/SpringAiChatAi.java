package i2f.spring.ai.impl;


import i2f.ai.std.ChatAi;
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
public class SpringAiChatAi implements ChatAi {
    protected String baseUrl = SpringAiAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = SpringAiAi.DEFAULT_MODEL;
    protected String respJsonAdditionalUserMessage = SpringAiAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, SpringAiToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String question) {
        SpringAiAi ai = new SpringAiAi();
        ai.setBaseUrl(baseUrl);
        ai.setApiKey(apiKey);
        ai.setSystem(system);
        ai.setModel(model);
        ai.setRespJsonAdditionalUserMessage(respJsonAdditionalUserMessage);
        ai.setToolDefinitionMap(toolDefinitionMap);
        ai.setQuestion(question);
        return ai.callAsString();
    }
}
