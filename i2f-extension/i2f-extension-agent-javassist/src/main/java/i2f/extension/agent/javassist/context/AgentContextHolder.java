package i2f.extension.agent.javassist.context;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/8/2 11:33
 * @desc
 */
public class AgentContextHolder {
    public static final AgentContextHolder HOLDER = new AgentContextHolder();

    public static volatile Instrumentation instrumentation;
    public static volatile String agentArg;

    public static volatile CopyOnWriteArrayList<ClassFileTransformer> transformers = new CopyOnWriteArrayList<>();

    public static volatile Object springApplicationContext;

    public static volatile CopyOnWriteArrayList<Object> globalList = new CopyOnWriteArrayList<>();

    public static volatile ConcurrentHashMap<String, Object> globalMap = new ConcurrentHashMap<>();

}
