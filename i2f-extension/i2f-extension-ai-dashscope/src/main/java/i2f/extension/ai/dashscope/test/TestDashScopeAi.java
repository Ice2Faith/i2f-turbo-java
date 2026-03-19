package i2f.extension.ai.dashscope.test;

import i2f.ai.std.ChatAi;
import i2f.context.impl.ListableContext;
import i2f.extension.ai.dashscope.impl.DashScopeChatAiProvider;
import i2f.extension.ai.dashscope.schema.JsonSchema;
import i2f.extension.ai.dashscope.tool.ToolDefinition;
import i2f.extension.ai.dashscope.tool.ToolHelper;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
public class TestDashScopeAi {
    public static void main(String[] args) {
        Map<String, Object> schema = JsonSchema.getTypeJsonSchema(TestSchemaPojo.class);
        System.out.println(schema);

        Map<String, ToolDefinition> map = ToolHelper.parseTools(TestToolComponent.class);

        DashScopeChatAiProvider provider = new DashScopeChatAiProvider();
        provider.setApiKey(System.getenv("DASHSCOPE_AI_API_KEY"));
        ListableContext context = new ListableContext();
        context.addBean(new TestToolComponent());
        provider.setContext(context);

        ChatAi chatAi = provider.getChatAi();
        String ret = chatAi.chat("北京的今天的天气怎么样，并且给出今天的日期");
        System.out.println(ret);
    }
}
