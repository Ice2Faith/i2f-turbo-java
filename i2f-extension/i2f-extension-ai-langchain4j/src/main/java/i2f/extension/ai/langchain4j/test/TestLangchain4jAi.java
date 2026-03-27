package i2f.extension.ai.langchain4j.test;

import i2f.ai.std.ChatAi;
import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.test.TestSchemaPojo;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.context.impl.ListableContext;
import i2f.extension.ai.langchain4j.impl.Langchain4jChatAiProvider;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolDefinition;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolHelper;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
public class TestLangchain4jAi {
    public static void main(String[] args) throws Exception {
        Map<String, Object> schema = JsonSchema.getTypeJsonSchema(TestSchemaPojo.class);
        System.out.println(schema);

        Map<String, Langchain4jToolDefinition> map = Langchain4jToolHelper.parseTools(TestToolComponent.class);
        System.out.println(map);

        Langchain4jChatAiProvider provider = new Langchain4jChatAiProvider();
        provider.setApiKey(System.getenv("DASHSCOPE_AI_API_KEY"));
        ListableContext context = new ListableContext();
        context.addBean(new TestToolComponent());
        context.addBean(new SkillsTools());
        provider.setContext(context);

        Map<String, SkillDefinition> skillMap = SkillsHelper.scanFileSystemSkills();
        String system = SkillsHelper.convertSkillDefinitionsAsSystemPrompt(skillMap);
        provider.setSystem(system);

        ChatAi chatAi = provider.getChatAi();
        String ret = chatAi.chat("北京的今天的天气怎么样，并且给出今天的日期，最后告诉我历史上的今天发生了什么");
        System.out.println(ret);
    }
}
