package i2f.extension.ai.langchain4j.impl;

import i2f.ai.std.RoleChatAi;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolDefinition;
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
public class Langchain4jRoleChatAi implements RoleChatAi {
    protected String baseUrl = Langchain4jAi.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String model = Langchain4jAi.DEFAULT_MODEL;
    protected Map<String, Langchain4jToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String role, String question) {
        Langchain4jAi ai = new Langchain4jAi();
        ai.setBaseUrl(baseUrl);
        ai.setApiKey(apiKey);
        ai.setSystem(role);
        ai.setModel(model);
        ai.setToolDefinitionMap(toolDefinitionMap);
        ai.setQuestion(question);
        return ai.callAsString();
    }
}
