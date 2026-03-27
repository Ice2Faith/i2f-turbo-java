package i2f.extension.ai.dashscope.test;

import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.extension.ai.dashscope.impl.DashScopeAi;
import i2f.extension.ai.dashscope.model.DashScopeModel;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

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
                .jsonSerializer(new IJsonSerializer() {

                    @Override
                    public Map<String, Object> deserializeAsMap(String enc) {
                        return JsonUtils.fromJson(enc, new TypeToken<Map<String, Object>>() {
                        }.getType());
                    }

                    @Override
                    public String serialize(Object data) {
                        return JsonUtils.toJson(data);
                    }

                    @Override
                    public Object deserialize(String enc) {
                        return JsonUtils.fromJson(enc, Object.class);
                    }

                    @Override
                    public Object deserialize(String enc, Class<?> clazz) {
                        return JsonUtils.fromJson(enc, clazz);
                    }

                    @Override
                    public Object deserialize(String enc, Object type) {
                        return JsonUtils.fromJson(enc, (Type) type);
                    }
                });

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(new TestToolComponent()))
                , new AiAgentContext());
        System.out.println(resp);
    }
}
