package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.tool.impl.a2a.AgentTools;
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

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "store the truth content, it will provide as system message on after conversation. Note: old truth content will be replaced."
    )
    public boolean store_truth(
            @ToolParam(value = "content", description = "the truth text content, such key information, number, rule, etc. any you want stored.")
            String content
    ) {
        OpenAiOperateDto req = AgentTools.REQUEST_HOLDER.get();
        req.setTruthContent(content);
        return true;
    }
}
