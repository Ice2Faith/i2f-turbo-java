package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/8/12 18:51
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value="session_record",description = "提供基于session级别的持久化存储读写能力"))
@ConditionalOnExpression("${ai.tools.session-record.enable:true}")
@Component
@Tools
public class SessionRecordTools {
    public static final String TOOL_CONTEXT_KEY = "sessionRecordsMap";

    private static final Set<String> exposeTools;

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = SessionRecordTools.class.getDeclaredMethods();
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

    public static Map<String, String> replaceAllInContextHolder(Map<String, String> map) {
        Map<String, String> sessionRecordsMap = ToolCallContextHolder.getOr(TOOL_CONTEXT_KEY, ConcurrentHashMap::new);
        sessionRecordsMap.clear();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                sessionRecordsMap.put(entry.getKey(), entry.getValue());
            }
        }
        return sessionRecordsMap;
    }

    public static final String SESSION_RECORD_TYPES = "session_record_types";

    @Tool(
            value = SESSION_RECORD_TYPES,
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "list of record types from session. Note: work on chat session level."
    )
    public List<String> session_record_types() {
        List<String> ret = new ArrayList<>();
        Map<String, String> sessionRecordsMap = ToolCallContextHolder.get(TOOL_CONTEXT_KEY);
        if (sessionRecordsMap == null) {
            return ret;
        }
        ret.addAll(sessionRecordsMap.keySet());
        return ret;
    }

    public static final String SESSION_RECORD_READ = "session_record_read";

    @Tool(
            value = SESSION_RECORD_READ,
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "read record contents from session by record type(s). Note: work on chat session level."
    )
    public Map<String, String> session_record_read(
            @ToolParam(value = "recordTypes", description = "the record types, for example [\"agent\"] or [\"request\", \"plan\"].")
            List<String> recordTypes
    ) {
        Map<String, String> sessionRecordsMap = ToolCallContextHolder.get(TOOL_CONTEXT_KEY);
        Map<String, String> ret = new HashMap<>();
        if (sessionRecordsMap == null) {
            return ret;
        }
        if (recordTypes == null) {
            return ret;
        }
        for (String recordType : recordTypes) {
            String content = sessionRecordsMap.get(recordType);
            ret.put(recordType, content);
        }
        return ret;
    }

    public static final String SESSION_RECORD_UPDATE = "session_record_update";

    @Tool(
            value = SESSION_RECORD_UPDATE,
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "update/remove session content by record type. Note: old record type content will be replaced, if content is null will remove it, work on chat session level."
    )
    public boolean session_record_update(
            @ToolParam(value = "recordType", description = "the record type, for example \"plan\" or \"checklist\".")
            String recordType,
            @ToolParam(value = "content", description = "the text content, any you want stored, cloud be null means remove.")
            String content
    ) {
        Map<String, String> sessionRecordsMap = ToolCallContextHolder.getOr(TOOL_CONTEXT_KEY, ConcurrentHashMap::new);
        if (content == null) {
            sessionRecordsMap.remove(recordType);
        } else {
            sessionRecordsMap.put(recordType, content);
        }
        return true;
    }
}
