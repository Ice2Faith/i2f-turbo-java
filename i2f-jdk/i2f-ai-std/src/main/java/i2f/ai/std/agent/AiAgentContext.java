package i2f.ai.std.agent;

import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.proxy.std.IProxyInvocationHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2026/3/25 16:49
 * @desc
 */
@Data
@NoArgsConstructor
public class AiAgentContext {
    public static final Map<String, SkillDefinition> DEFAULT_SKILLS_MAP = SkillsHelper.scanFileSystemSkills();
    public static final InheritableThreadLocal<AiAgentContext> CONTEXT=new InheritableThreadLocal<>();

    protected final AtomicBoolean interrupt = new AtomicBoolean(false);

    protected boolean enableSkills = true;
    protected boolean enableRag = true;
    protected int ragTopCount = 5;
    protected boolean enableRagAct = true;

    protected Map<String, SkillDefinition> skillsMap = new HashMap<>(DEFAULT_SKILLS_MAP);

    protected final AtomicInteger maxKeepMessageCount = new AtomicInteger(20);
    protected final AtomicBoolean keepFirstUserMessage = new AtomicBoolean(true);
    protected final AtomicBoolean compressHistoryMessage = new AtomicBoolean(true);
    protected final AtomicInteger compressHistoryCount = new AtomicInteger(16);

    protected final AtomicInteger maxAllToolCallCount = new AtomicInteger(100);
    protected final AtomicInteger maxSingleToolCallCount = new AtomicInteger(10);
    protected final AtomicInteger maxSingleToolSameArgumentFailureCount = new AtomicInteger(2);

    protected final AtomicBoolean enableParallelToolCall=new AtomicBoolean(true);
    protected ExecutorService toolExecPool;

    protected IProxyInvocationHandler toolInterceptor;

    protected final ConcurrentHashMap<String,Object> sharedContext=new ConcurrentHashMap<>();
}
