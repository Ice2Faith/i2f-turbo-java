package i2f.extension.ai.openai.impl;


import i2f.ai.std.ChatAi;
import i2f.extension.ai.openai.tool.OpenAiToolDefinition;
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
public class OpenAiChatAi implements ChatAi {
    protected String baseUrl = OpenAiAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = OpenAiAi.DEFAULT_MODEL;
    protected String respJsonAdditionalUserMessage = OpenAiAi.DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, OpenAiToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String question) {
        OpenAiAi ai = new OpenAiAi();
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
