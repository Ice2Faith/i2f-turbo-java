package i2f.spring.ai.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.spring.ai.model.SpringAiJsonSerializer;
import i2f.spring.ai.model.SpringAiModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 9:27
 * @desc
 */
public class TestSpringAiAgent {
    public static void main(String[] args) {


        AiAgent agent = new AiAgent()
                .model(SpringAiModel.openai()
                        .baseUrl(SpringAiModel.DASHSCOPE_BASE_URL)
                        .apiKey(System.getenv("DASHSCOPE_AI_API_KEY"))
                        .model(SpringAiModel.DEFAULT_MODEL)
                        .build())
                .jsonSerializer(SpringAiJsonSerializer.INSTANCE);

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(new TestToolComponent()))
                , new AiAgentContext());
        System.out.println(resp);
    }
}
