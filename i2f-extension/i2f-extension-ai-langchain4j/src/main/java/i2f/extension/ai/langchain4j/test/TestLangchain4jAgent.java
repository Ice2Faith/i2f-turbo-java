package i2f.extension.ai.langchain4j.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.extension.ai.langchain4j.impl.Langchain4jAi;
import i2f.extension.ai.langchain4j.model.Langchain4jJsonSerializer;
import i2f.extension.ai.langchain4j.model.Langchain4jModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 9:27
 * @desc
 */
public class TestLangchain4jAgent {
    public static void main(String[] args) {
        AiAgent agent = new AiAgent()
                .model(Langchain4jModel.openai()
                        .baseUrl(Langchain4jAi.DASHSCOPE_BASE_URL)
                        .apiKey(System.getenv("DASHSCOPE_AI_API_KEY"))
                        .model(Langchain4jAi.DEFAULT_MODEL)
                        .build())
                .jsonSerializer(Langchain4jJsonSerializer.INSTANCE);

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(new TestToolComponent()))
                , new AiAgentContext());
        System.out.println(resp);
    }
}
