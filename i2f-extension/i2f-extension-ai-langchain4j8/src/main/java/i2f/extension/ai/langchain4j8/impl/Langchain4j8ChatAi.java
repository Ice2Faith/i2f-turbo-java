package i2f.extension.ai.langchain4j8.impl;

import i2f.ai.std.ChatAi;
import i2f.extension.ai.langchain4j8.tool.Langchain4j8ToolDefinition;
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
public class Langchain4j8ChatAi implements ChatAi {
    protected String baseUrl = Langchain4j8Ai.DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String model = Langchain4j8Ai.DEFAULT_MODEL;
    protected Map<String, Langchain4j8ToolDefinition> toolDefinitionMap = new LinkedHashMap<>();

    @Override
    public String chat(String question) {
        Langchain4j8Ai ai = new Langchain4j8Ai();
        ai.setBaseUrl(baseUrl);
        ai.setApiKey(apiKey);
        ai.setSystem(system);
        ai.setModel(model);
        ai.setToolDefinitionMap(toolDefinitionMap);
        ai.setQuestion(question);
        return ai.callAsString();
    }
}
