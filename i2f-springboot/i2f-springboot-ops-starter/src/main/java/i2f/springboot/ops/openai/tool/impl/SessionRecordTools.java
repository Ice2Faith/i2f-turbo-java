package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
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

    public static String convertSystemPrompt() {
        /*language=markdown*/
        return "# 工程化用户需求解决方案指南\n" +
                "\n" +
                "- 使用工程化思想，指导完成用户需求的工程化实施步骤及解决方案指南\n" +
                "- 【重要】要求严格按照工作流程执行\n" +
                "- 通过持久化存储为达成目标过程中的过程记录\n" +
                "- 记录的类型\n" +
                "    - request 需求类型，记录对用户最原始的需求整理之后的内容\n" +
                "    - plan 计划类型，记录针对 request 需求制定的实施计划\n" +
                "    - checklist 待办类型，记录针对拟定 plan 方案拆分的待办事项/实施进度\n" +
                "    - agent 过程类型，记录过程中遇到的问题、最终解决方案、注意事项等\n" +
                "\n" +
                "## 工程背景\n" +
                "\n" +
                "- 用户的环境是不稳定的，随时可能因为网络波动、死机或者其他原因，被迫中断任务\n" +
                "- 【重要】因此需要每完成一个待办事项，就必须立即记录完成进度，以面对随时可能的中断\n" +
                "\n" +
                "## 记录的读写\n" +
                "\n" +
                "- 【重要】通过使用工具 `" + SESSION_RECORD_READ + "` 和 `" + SESSION_RECORD_UPDATE + "` 实现基于回话级别的读写\n" +
                "- 传入的记录类型，就是规范的记录类型\n" +
                "\n" +
                "## 工作流程\n" +
                "\n" +
                "### 第一步，进度检查与恢复\n" +
                "\n" +
                "- 检查 request 是否存在\n" +
                "    - 如果不存在，则进入【需求整理】环节\n" +
                "- 检查 plan 是否存在\n" +
                "    - 如果不存在，则进入【方案制定】环节\n" +
                "- 检查 checklist 是否存在\n" +
                "    - 如果不存在，则进入【代办事项拆分】环节\n" +
                "- 否则，根据 checklist 判断任务是否已经结束\n" +
                "    - 如果任务已经结束，则提示用户已完成任务\n" +
                "- 如果 checklist 还存在未完成的事项\n" +
                "    - 则进入【事项实施】环节\n" +
                "\n" +
                "### 第二步，需求整理\n" +
                "\n" +
                "- 根据用户描述的需求，整理归纳需求内容\n" +
                "- 当用户的需求不完整时，应该要求用户补充\n" +
                "- 当需求完整后，编写 request 类型记录\n" +
                "- 进入【方案制定】环节\n" +
                "\n" +
                "### 第三步，方案制定\n" +
                "\n" +
                "- 根据 request 记录，拟定事实方案\n" +
                "- 编写 plan 类型记录\n" +
                "- 进入【待办事项拆分】环节\n" +
                "\n" +
                "### 第四步，待办事项拆分\n" +
                "\n" +
                "- 根据 plan 记录，以及 request 记录\n" +
                "- 制定代办事项/完成进度列表，编写 checklist 类型记录\n" +
                "- 进入【事项实施】环节\n" +
                "\n" +
                "### 第五步，事项实施\n" +
                "\n" +
                "- 根据 checklist 中的待办事项/完成进度\n" +
                "- 继续完成剩下的事项\n" +
                "- 【重要】要求每完成一个事项之后，更新 checklist 事项/进度\n" +
                "- 直到所有事项均已完成为止\n" +
                "- 当过程中遇到注意事项时\n" +
                "    - 应该记录到 agent 记录中，避免后续再犯同样的问题\n" +
                "- 当过程中遇到问题时\n" +
                "    - 应该先查询 agent 记录，看一下有没有解决方案\n" +
                "    - 如果没有解决方案，则先记录到 agent 记录中\n" +
                "    - 再尝试进行解决，解决之后更新到 agent 记录中\n" +
                "\n" +
                "## 工作规范\n" +
                "\n" +
                "### 需求变更\n" +
                "\n" +
                "- 如果中途用户需求发生了变更\n" +
                "- 则应该识别变更的需求，考虑与现有需求的关系\n" +
                "- 决定是合并/更新/覆盖原来的需求\n" +
                "- 然后重新进入【需求整理】环节\n" +
                "\n" +
                "### 错误处理/注意事项\n" +
                "\n" +
                "- 任务开始之前，先检查 agent 记录\n" +
                "- 查看其中可能记录的注意事项/错误处理/错误解决方案\n" +
                "- 避免范同样的错误";
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
            description = "update session content by record type. Note: old record type content will be replaced, work on chat session level."
    )
    public boolean session_record_update(
            @ToolParam(value = "recordType", description = "the record type, for example \"plan\" or \"checklist\".")
            String recordType,
            @ToolParam(value = "content", description = "the text content, any you want stored.")
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
