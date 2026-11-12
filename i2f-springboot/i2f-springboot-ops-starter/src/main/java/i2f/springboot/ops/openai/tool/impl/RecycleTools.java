package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Ice2Faith
 * @date 2026/7/17 9:32
 * @desc
 */
@McpServerExpose(false)
@ConditionalOnExpression("${ai.tools.recycle.enable:true}")
@Component
@Tools
public class RecycleTools {
    private static final Set<String> exposeTools;
    private final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = RecycleTools.class.getDeclaredMethods();
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

    public static final String PROTOCOL = "ref://";

    public static Object recycleArguments(Object arguments, int recycleMinLength, Map<String, String> recycleMap) {
        if (arguments == null) {
            return arguments;
        }

        if (arguments instanceof CharSequence) {
            CharSequence str = (CharSequence) arguments;
            if (str.length() > recycleMinLength) {
                String refKey = PROTOCOL + UUID.randomUUID().toString().replace("-", "");

                String content = String.valueOf(arguments);
                String rep = "[recycled] tool content recycled; use url '" + refKey + "' on tool `" + REF_CONTENT + "` to got origin content if you need!";
                if (rep.length() < content.length()) {
                    recycleMap.put(refKey, content);
                    return rep;
                }
            }
        } else if (arguments instanceof Map) {
            Map map = (Map) arguments;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                value = recycleArguments(value, recycleMinLength, recycleMap);
                map.put(key, value);
            }
        } else if (arguments instanceof List) {
            List col = (List) arguments;
            for (int i = 0; i < col.size(); i++) {
                Object obj = col.get(i);
                obj = recycleArguments(obj, recycleMinLength, recycleMap);
                col.set(i, obj);
            }
        } else if (arguments.getClass().isArray()) {
            int len = Array.getLength(arguments);
            for (int i = 0; i < len; i++) {
                Object obj = Array.get(arguments, i);
                obj = recycleArguments(obj, recycleMinLength, recycleMap);
                Array.set(arguments, i, obj);
            }
        }
        return arguments;
    }

    public static final String REF_CONTENT = "ref_content";

    @Tool(
            value = REF_CONTENT,
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "get origin content which be recycled by '" + PROTOCOL + "xxx' url."
    )
    public Map<String, Object> ref_content(
            @ToolParam(value = "ref", description = "the ref url, for example '" + PROTOCOL + "xxx'.")
            String ref
    ) {
        String content = null;
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        if (req != null) {
            Map<String, String> recycleMap = req.getRecycleMap();
            if (recycleMap != null) {
                content = recycleMap.get(ref);
            }
        }

        Map<String, Object> ret = new HashMap<>();
        if (content == null) {
            ret.put("content", null);
            ret.put("hints", "missing content found!");
            return ret;
        }
        ret.put("content", content);
        ret.put("hints", "content has found.");
        return ret;
    }
}
