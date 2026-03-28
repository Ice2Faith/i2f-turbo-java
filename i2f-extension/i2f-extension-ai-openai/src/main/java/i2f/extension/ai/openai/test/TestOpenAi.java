package i2f.extension.ai.openai.test;


import i2f.ai.std.ChatAi;
import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.ai.std.tool.test.TestSchemaPojo;
import i2f.ai.std.tool.test.TestToolComponent;
import i2f.context.impl.ListableContext;
import i2f.extension.ai.openai.impl.OpenAiChatAiProvider;
import i2f.extension.ai.openai.tool.OpenAiToolDefinition;
import i2f.extension.ai.openai.tool.OpenAiToolHelper;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
public class TestOpenAi {
    public static void main(String[] args) throws Exception {
        Map<String, Object> schema = JsonSchema.getTypeJsonSchema(JsonSchemaAnnotationResolver.INSTANCE, TestSchemaPojo.class);
        System.out.println(schema);

        Map<String, OpenAiToolDefinition> map = OpenAiToolHelper.parseTools(TestToolComponent.class);
        System.out.println(map);

        OpenAiChatAiProvider provider = new OpenAiChatAiProvider();
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
