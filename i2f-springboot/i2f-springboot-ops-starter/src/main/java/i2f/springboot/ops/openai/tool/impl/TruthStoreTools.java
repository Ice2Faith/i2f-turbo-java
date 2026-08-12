package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Ice2Faith
 * @date 2026/7/17 9:32
 * @desc
 */
@ConditionalOnExpression("${ai.tools.truth.enable:true}")
@Component
@Tools
public class TruthStoreTools {
    private static final Set<String> exposeTools;
    private final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = TruthStoreTools.class.getDeclaredMethods();
        for (Method method : methods) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann == null) {
                continue;
            }
            String name = method.getName();
            String value = ann.value();
            if (value != null && !value.isEmpty()) {
                name = value;
            }
            names.add(name);
        }
        exposeTools = Collections.unmodifiableSet(names);
    }

    public static Set<String> toolNames() {
        return new HashSet<>(exposeTools);
    }

    public static String convertSystemPrompt(){
        /*language=markdown*/
        return "# 事实系统\n" +
                "\n" +
                "- 事实系统，系统提供了一个会话级别的事实存储体系\n" +
                "- 可以用于存储较为简短的、关键的信息\n" +
                "- 使用工具 `"+STORE_TRUTH+"` 进行存储（注意遵守工具的使用描述）\n" +
                "- 存储的内容，将会在后续通过系统消息方式注入到对话内容中\n" +
                "- 因此，你可以非常方便的知道你记录了哪些关键信息";
    }

    public static final String STORE_TRUTH="store_truth";
    @Tool(
            value=STORE_TRUTH,
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "store the truth content, it will provide as system message on after conversation. Rule: make it brief and precise. Note: old truth content will be replaced, work on chat session level."
    )
    public boolean store_truth(
            @ToolParam(value = "content", description = "the truth text content, such key information, number, rule, etc. any you want stored.")
            String content
    ) {
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        req.setTruthContent(content);
        return true;
    }
}
