package i2f.ai.std.agent;

import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.proxy.std.IProxyInvocationHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/3/25 16:49
 * @desc
 */
@Data
@NoArgsConstructor
public class AiAgentContext {
    public static final Map<String, SkillDefinition> DEFAULT_SKILLS_MAP = SkillsHelper.scanFileSystemSkills();
    public static final InheritableThreadLocal<AiAgentContext> CONTEXT = new InheritableThreadLocal<>();

    protected final AtomicBoolean interrupt = new AtomicBoolean(false);

    protected boolean enableSkills = true;
    protected boolean enableRag = true;
    protected int ragTopCount = 5;
    protected boolean enableRagAct = true;
    protected boolean enableStructOutput = false;
    protected Class<?> outputType = null;

    protected List<Predicate<Set<String>>> toolTagsFilterChain = new ArrayList<>();
    protected List<Predicate<Set<String>>> skillTagsFilterChain = new ArrayList<>();

    protected Map<String, SkillDefinition> skillsMap = new HashMap<>(DEFAULT_SKILLS_MAP);

    protected final AtomicInteger maxKeepMessageCount = new AtomicInteger(20);
    protected final AtomicBoolean keepFirstUserMessage = new AtomicBoolean(true);
    protected final AtomicBoolean compressHistoryMessage = new AtomicBoolean(true);
    protected final AtomicInteger compressHistoryCount = new AtomicInteger(16);

    protected final AtomicInteger maxAllToolCallCount = new AtomicInteger(100);
    protected final AtomicInteger maxSingleToolCallCount = new AtomicInteger(10);
    protected final AtomicInteger maxSingleToolSameArgumentFailureCount = new AtomicInteger(2);

    protected final AtomicBoolean enableParallelToolCall = new AtomicBoolean(true);
    protected ExecutorService toolExecPool;

    protected IProxyInvocationHandler toolInterceptor;

    protected final ConcurrentHashMap<String, Object> sharedContext = new ConcurrentHashMap<>();

    public static boolean hasAnyTagsFilter(Collection<String> tags, Collection<String> requiredTags) {
        if (requiredTags == null || requiredTags.isEmpty()) {
            return true;
        }
        if (tags == null || tags.isEmpty()) {
            return true;
        }
        for (String requiredTag : requiredTags) {
            if (tags.contains(requiredTag)) {
                return true;
            }
        }
        return false;
    }

    public AiAgentContext enableSkills(boolean enableSkills) {
        this.enableSkills = enableSkills;
        return this;
    }

    public AiAgentContext enableRag(boolean enableRag) {
        this.enableRag = enableRag;
        return this;
    }

    public AiAgentContext ragTopCount(int ragTopCount) {
        this.ragTopCount = ragTopCount;
        return this;
    }

    public AiAgentContext enableRagAct(boolean enableRagAct) {
        this.enableRagAct = enableRagAct;
        return this;
    }

    public AiAgentContext enableStructOutput(boolean enableStructOutput) {
        this.enableStructOutput = enableStructOutput;
        return this;
    }

    public AiAgentContext outputType(Class<?> outputType) {
        this.outputType = outputType;
        return this;
    }

    public AiAgentContext toolTagsFilterChain(List<Predicate<Set<String>>> tagsFilterChain) {
        this.toolTagsFilterChain = tagsFilterChain;
        return this;
    }

    public AiAgentContext addToolTagsFilter(Predicate<Set<String>> tagsFilter) {
        if (this.toolTagsFilterChain == null) {
            this.toolTagsFilterChain = new ArrayList<>();
        }
        if (tagsFilter != null) {
            this.toolTagsFilterChain.add(tagsFilter);
        }
        return this;
    }

    public AiAgentContext skillTagsFilterChain(List<Predicate<Set<String>>> tagsFilterChain) {
        this.skillTagsFilterChain = tagsFilterChain;
        return this;
    }

    public AiAgentContext addSkillTagsFilter(Predicate<Set<String>> tagsFilter) {
        if (this.skillTagsFilterChain == null) {
            this.skillTagsFilterChain = new ArrayList<>();
        }
        if (tagsFilter != null) {
            this.skillTagsFilterChain.add(tagsFilter);
        }
        return this;
    }

    public AiAgentContext skillsMap(Map<String, SkillDefinition> skillsMap) {
        this.skillsMap = skillsMap;
        return this;
    }


    public AiAgentContext skill(SkillDefinition definition) {
        if (skillsMap == null) {
            skillsMap = new HashMap<>();
        }
        skillsMap.put(definition.getName(), definition);
        return this;
    }

    public AiAgentContext skills(Map<String, SkillDefinition> map) {
        if (skillsMap == null) {
            skillsMap = new HashMap<>();
        }
        skillsMap.putAll(map);
        return this;
    }

    public AiAgentContext skills(List<SkillDefinition> list) {
        if (skillsMap == null) {
            skillsMap = new HashMap<>();
        }
        for (SkillDefinition definition : list) {
            skillsMap.put(definition.getName(), definition);
        }
        return this;
    }

    public AiAgentContext maxKeepMessageCount(int count) {
        this.maxKeepMessageCount.set(count);
        return this;
    }

    public AiAgentContext keepFirstUserMessage(boolean enable) {
        this.keepFirstUserMessage.set(enable);
        return this;
    }

    public AiAgentContext compressHistoryMessage(boolean enable) {
        this.compressHistoryMessage.set(enable);
        return this;
    }

    public AiAgentContext compressHistoryCount(int count) {
        this.compressHistoryCount.set(count);
        return this;
    }

    public AiAgentContext maxAllToolCallCount(int count) {
        this.maxAllToolCallCount.set(count);
        return this;
    }

    public AiAgentContext maxSingleToolCallCount(int count) {
        this.maxSingleToolCallCount.set(count);
        return this;
    }

    public AiAgentContext maxSingleToolSameArgumentFailureCount(int count) {
        this.maxSingleToolSameArgumentFailureCount.set(count);
        return this;
    }

    public AiAgentContext enableParallelToolCall(boolean enable) {
        this.enableParallelToolCall.set(enable);
        return this;
    }

    public AiAgentContext toolExecPool(ExecutorService toolExecPool) {
        this.toolExecPool = toolExecPool;
        return this;
    }

    public AiAgentContext toolInterceptor(IProxyInvocationHandler toolInterceptor) {
        this.toolInterceptor = toolInterceptor;
        return this;
    }

    public AiAgentContext shared(String key, Object value) {
        this.sharedContext.put(key, value);
        return this;
    }

    public AiAgentContext shared(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            this.sharedContext.put(entry.getKey(), entry.getValue());
        }
        return this;
    }
}
