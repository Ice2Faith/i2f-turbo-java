package i2f.extension.ai.dashscope.test;

import i2f.ai.std.ChatAi;
import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.test.TestSchemaPojo;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.context.impl.ListableContext;
import i2f.extension.ai.dashscope.impl.DashScopeChatAiProvider;
import i2f.extension.ai.dashscope.tool.DashScopeToolDefinition;
import i2f.extension.ai.dashscope.tool.DashScopeToolHelper;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
public class TestDashScopeAi {
    public static void main(String[] args) throws Exception {
        Map<String, Object> schema = JsonSchema.getTypeJsonSchema(TestSchemaPojo.class);
        System.out.println(schema);

        Map<String, DashScopeToolDefinition> map = DashScopeToolHelper.parseTools(TestToolComponent.class);
        System.out.println(map);

        DashScopeChatAiProvider provider = new DashScopeChatAiProvider();
        provider.setApiKey(System.getenv("DASHSCOPE_AI_API_KEY"));
        ListableContext context = new ListableContext();
        context.addBean(new TestToolComponent());
        context.addBean(new SkillsTools());
        provider.setContext(context);

        Map<String, SkillDefinition> skillMap = SkillsHelper.scanFileSystemSkills();
        if(!skillMap.isEmpty()){
            StringBuilder builder=new StringBuilder();

            builder.append("你拥有以下技能，如果有需要使用这些技能的时候，先阅读该技能的详细教程，" +
                    "根据教程内容，决定是否还需要更多关于教程的资源需要调用。最终回答用户的问题。").append("\n");

            builder.append("## 技能列表").append("\n");
            for (Map.Entry<String, SkillDefinition> entry : skillMap.entrySet()) {
                SkillDefinition definition = entry.getValue();
                builder.append("### ").append(definition.getName()).append("\n")
                        .append("- ").append(definition.getDescription()).append("\n")
                        .append("\n");
            }

            provider.setSystem(builder.toString());
        }

        ChatAi chatAi = provider.getChatAi();
        String ret = chatAi.chat("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么");
        System.out.println(ret);
    }
}
