package i2f.extension.ai.langchain4j8.test;

import com.google.gson.Gson;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.extension.ai.langchain4j8.impl.Langchain4j8Ai;
import i2f.extension.ai.langchain4j8.model.Langchain4j8Model;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 9:27
 * @desc
 */
public class TestLangchain4j8Agent {
    public static void main(String[] args){
        AiAgent agent = new AiAgent()
                .model(Langchain4j8Model.openai()
                        .baseUrl(Langchain4j8Ai.DASHSCOPE_BASE_URL)
                        .apiKey(System.getenv("DASHSCOPE_AI_API_KEY"))
                        .model(Langchain4j8Ai.DEFAULT_MODEL)
                        .build())
                .jsonSerializer(new IJsonSerializer() {
                    protected Gson gson = new Gson();

                    @Override
                    public Map<String, Object> deserializeAsMap(String enc) {
                        return gson.fromJson(enc, new TypeToken<Map<String, Object>>() {
                        }.getType());
                    }

                    @Override
                    public String serialize(Object data) {
                        return gson.toJson(data);
                    }

                    @Override
                    public Object deserialize(String enc) {
                        return gson.fromJson(enc, Object.class);
                    }

                    @Override
                    public Object deserialize(String enc, Class<?> clazz) {
                        return gson.fromJson(enc, clazz);
                    }

                    @Override
                    public Object deserialize(String enc, Object type) {
                        return gson.fromJson(enc, (Type) type);
                    }
                });

        AiAgentResponse resp = agent.generate(new AiRequest()
                        .user("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么")
                        .tools(ToolRawHelper.parseTools(new TestToolComponent()))
                ,new AiAgentContext());
        System.out.println(resp);
    }
}
