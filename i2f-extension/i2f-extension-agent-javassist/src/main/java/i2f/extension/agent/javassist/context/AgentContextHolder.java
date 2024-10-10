package i2f.extension.agent.javassist.context;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/8/2 11:33
 * @desc
 */
public class AgentContextHolder {
    public static final AgentContextHolder HOLDER = new AgentContextHolder();

    public static final CopyOnWriteArrayList<Predicate<Throwable>> THROWABLE_LISTENER = new CopyOnWriteArrayList<>();
    public static final LinkedBlockingQueue<Throwable> THROWABLE_QUEUE = new LinkedBlockingQueue<>();
    public static final AtomicBoolean THROWABLE_THREAD_RUNNING = new AtomicBoolean(false);
    public static final AtomicInteger THROWABLE_MAX_QUEUE_SIZE = new AtomicInteger(8192);

    public static volatile Instrumentation instrumentation;
    public static volatile String agentArg;

    public static volatile CopyOnWriteArrayList<ClassFileTransformer> transformers = new CopyOnWriteArrayList<>();

    public static volatile Object springApplication;

    public static volatile Object springApplicationContext;

    public static volatile CopyOnWriteArrayList<Object> globalList = new CopyOnWriteArrayList<>();

    public static volatile ConcurrentHashMap<String, Object> globalMap = new ConcurrentHashMap<>();

    public static void notifyThrowable(Throwable e) {
        System.out.println("notify-throwable-invoke:" + e.getClass());
        while (THROWABLE_QUEUE.size() > THROWABLE_MAX_QUEUE_SIZE.get()) {
            THROWABLE_QUEUE.poll();
        }
        THROWABLE_QUEUE.add(e);
        triggerThrowableDispatchThread();
    }

    public static void triggerThrowableDispatchThread() {
        if (THROWABLE_THREAD_RUNNING.getAndSet(true)) {
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                runLoopThreadDispatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "throwable-dispatcher");
        thread.setDaemon(true);
        thread.start();
    }

    public static void runLoopThreadDispatch() throws InterruptedException {
        while (true) {
            while (THROWABLE_QUEUE.size() > THROWABLE_MAX_QUEUE_SIZE.get()) {
                THROWABLE_QUEUE.poll();
            }
            Throwable e = THROWABLE_QUEUE.take();
            System.out.println("dispatch-throwable-invoke:" + e.getClass());
            for (Predicate<Throwable> listener : THROWABLE_LISTENER) {
                if (listener == null) {
                    continue;
                }
                try {
                    boolean isBreak = listener.test(e);
                    if (isBreak) {
                        break;
                    }
                } catch (Throwable ex) {
                    e.printStackTrace();
                }
            }
        }

    }
}
