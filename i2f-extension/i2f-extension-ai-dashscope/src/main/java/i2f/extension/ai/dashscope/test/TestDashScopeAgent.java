package i2f.extension.ai.dashscope.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.extension.ai.dashscope.impl.DashScopeAi;
import i2f.extension.ai.dashscope.model.DashScopeJsonSerializer;
import i2f.extension.ai.dashscope.model.DashScopeModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 9:27
 * @desc
 */
public class TestDashScopeAgent {
    public static void main(String[] args) {
        AiAgent agent = new AiAgent()
                .model(new DashScopeModel()
                        .apiKey(System.getenv("DASHSCOPE_AI_API_KEY"))
                        .model(DashScopeAi.DEFAULT_MODEL)
                )
                .jsonSerializer(DashScopeJsonSerializer.INSTANCE);

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(JsonSchemaAnnotationResolver.INSTANCE, new TestToolComponent()))
                , new AiAgentContext());
        System.out.println(resp);
    }
}
