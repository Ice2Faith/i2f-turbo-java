package i2f.extension.ai.openai.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.extension.ai.openai.impl.OpenAiAi;
import i2f.extension.ai.openai.model.OpenAiJsonSerializer;
import i2f.extension.ai.openai.model.OpenAiModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 9:27
 * @desc
 */
public class TestOpenAiAgent {
    public static void main(String[] args) {
        AiAgent agent = new AiAgent()
                .model(OpenAiModel.builder()
                        .baseUrl(OpenAiAi.DASHSCOPE_BASE_URL)
                        .apiKey(System.getenv("DASHSCOPE_AI_API_KEY"))
                        .model(OpenAiAi.DEFAULT_MODEL)
                        .build())
                .jsonSerializer(OpenAiJsonSerializer.INSTANCE);

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(new TestToolComponent()))
                , new AiAgentContext());
        System.out.println(resp);
    }
}
