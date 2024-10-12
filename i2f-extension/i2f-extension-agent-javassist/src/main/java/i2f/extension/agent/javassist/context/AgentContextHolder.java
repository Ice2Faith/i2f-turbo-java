package i2f.extension.agent.javassist.context;

import i2f.clock.SystemClock;
import i2f.extension.agent.javassist.transformer.file.SystemOutFilePrintListener;
import i2f.extension.agent.javassist.transformer.http.SystemOutUrlOpenConnectionPrintListener;
import i2f.extension.agent.javassist.transformer.jdbc.SystemOutSqlPrintListener;
import i2f.extension.agent.javassist.transformer.process.SystemOutProcessStartPrintListener;
import i2f.extension.agent.javassist.transformer.rmi.SystemOutRmiNamingLookupPrintListener;
import i2f.extension.agent.javassist.transformer.shutdown.SystemErrorShutdownPrintListener;
import i2f.extension.agent.javassist.transformer.throwables.SystemErrorThrowablePrintListener;
import i2f.jvm.JvmUtil;
import i2f.reflect.ReflectResolver;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
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

    public static final CopyOnWriteArrayList<BiPredicate<String, Object>> SQL_LISTENER = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Predicate<File>> FILE_LISTENER = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<BiPredicate<Integer, StackTraceElement[]>> SHUTDOWN_LISTENER = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Predicate<ProcessBuilder>> PROCESS_START_LISTENER = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Predicate<URL>> URL_OPEN_CONNECTION_LISTENER = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Predicate<String>> RMI_NAMING_LOOKUP_LISTENER = new CopyOnWriteArrayList<>();

    public static volatile Instrumentation instrumentation;
    public static volatile String agentArg;

    public static volatile CopyOnWriteArrayList<ClassFileTransformer> transformers = new CopyOnWriteArrayList<>();

    public static volatile Object springApplication;

    public static volatile Object springApplicationContext;

    public static volatile CopyOnWriteArrayList<Object> globalList = new CopyOnWriteArrayList<>();

    public static volatile ConcurrentHashMap<String, Object> globalMap = new ConcurrentHashMap<>();

    public static final String REQUEST_HEADER_TRACE_ID_NAME = "trace-id";
    public static final String TRACE_ID_FIELD_NAME = "traceId";
    public static final ThreadLocal<String> threadTraceId = new ThreadLocal<>();
    public static final ThreadLocal<String> threadTraceSource = new ThreadLocal<>();
    public static final long PID;
    private static final AtomicBoolean initialSlf4jMDC = new AtomicBoolean(false);
    private static final AtomicBoolean initialI2fLogHolder = new AtomicBoolean(false);
    private static volatile Class<?> slf4jMDCClass;
    private static volatile Class<?> i2fLogHolderClass;

    static {
        long pid = 0;
        try {
            String str = JvmUtil.getPid();
            try {
                pid = Long.parseLong(str);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        PID = pid;
        THROWABLE_LISTENER.add(SystemErrorThrowablePrintListener.INSTANCE);
        SQL_LISTENER.add(SystemOutSqlPrintListener.INSTANCE);
        FILE_LISTENER.add(SystemOutFilePrintListener.INSTANCE);
        SHUTDOWN_LISTENER.add(SystemErrorShutdownPrintListener.INSTANCE);
        PROCESS_START_LISTENER.add(SystemOutProcessStartPrintListener.INSTANCE);
        URL_OPEN_CONNECTION_LISTENER.add(SystemOutUrlOpenConnectionPrintListener.INSTANCE);
        RMI_NAMING_LOOKUP_LISTENER.add(SystemOutRmiNamingLookupPrintListener.INSTANCE);
    }

    public static Instrumentation instrumentation() {
        return instrumentation;
    }

    public static void setTraceId(Object request) {
        System.out.println("filter-holder-set-trace-id: " + request);
        String id = threadTraceId.get();
        if (id != null) {
            return;
        }
        if (request != null) {
            try {
                id = (String) ReflectResolver.invokeMethod(request, "getHeader", REQUEST_HEADER_TRACE_ID_NAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (id == null) {
                try {
                    id = (String) ReflectResolver.invokeMethod(request, "getHeader", TRACE_ID_FIELD_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (id == null) {
                try {
                    id = (String) ReflectResolver.invokeMethod(request, "getParameter", TRACE_ID_FIELD_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (id == null) {
                try {
                    id = (String) ReflectResolver.invokeMethod(request, "getParameter", TRACE_ID_FIELD_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (threadTraceSource.get() == null) {
                try {
                    Object origin = ReflectResolver.invokeMethod(request, "getHeader", "origin");
                    Object method = ReflectResolver.invokeMethod(request, "getMethod");
                    Object contentType = ReflectResolver.invokeMethod(request, "getContentType");
                    Object url = ReflectResolver.invokeMethod(request, "getRequestURL");

                    threadTraceSource.set(String.format("http [%s] [%s] [%s] [%s]", String.valueOf(origin), String.valueOf(method), String.valueOf(contentType), String.valueOf(url)));
                } catch (IllegalAccessException e) {

                }
            }
        }
        if (id == null) {
            id = "tid-" + Long.toString(SystemClock.currentTimeMillis(), 16) + "-" + Long.toString(PID, 16) + "-" + Long.toString(Thread.currentThread().getId(), 16) + "-" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        }
        threadTraceId.set(id);
        if (!initialSlf4jMDC.getAndSet(true)) {
            slf4jMDCClass = ReflectResolver.loadClass("org.slf4j.MDC");
        }
        if (slf4jMDCClass != null) {
            try {
                ReflectResolver.invokeStaticMethod(slf4jMDCClass, "put", TRACE_ID_FIELD_NAME, threadTraceId.get());
            } catch (IllegalAccessException e) {

            }
        }
        if (!initialI2fLogHolder.getAndSet(true)) {
            i2fLogHolderClass = ReflectResolver.loadClass("i2f.log.holder.LogHolder");
        }
        if (i2fLogHolderClass != null) {
            try {
                ReflectResolver.invokeStaticMethod(i2fLogHolderClass, "setTraceId", threadTraceId.get());
            } catch (IllegalAccessException e) {

            }
        }
    }

    public static void removeTraceId() {
        threadTraceId.remove();
        threadTraceSource.remove();
        if (!initialSlf4jMDC.getAndSet(true)) {
            slf4jMDCClass = ReflectResolver.loadClass("org.slf4j.MDC");
        }
        if (slf4jMDCClass != null) {
            try {
                ReflectResolver.invokeStaticMethod(slf4jMDCClass, "remove", TRACE_ID_FIELD_NAME);
            } catch (IllegalAccessException e) {

            }
        }
        if (!initialI2fLogHolder.getAndSet(true)) {
            i2fLogHolderClass = ReflectResolver.loadClass("i2f.log.holder.LogHolder");
        }
        if (i2fLogHolderClass != null) {
            try {
                ReflectResolver.invokeStaticMethod(i2fLogHolderClass, "removeTraceId");
            } catch (IllegalAccessException e) {

            }
        }
    }

    public static String getTraceId() {
        return threadTraceId.get();
    }

    public static void notifyRmiNamingLookup(String name) {
        for (Predicate<String> item : RMI_NAMING_LOOKUP_LISTENER) {
            if (item != null) {
                if (item.test(name)) {
                    break;
                }
            }
        }
    }

    public static void notifyUrlOpenConnection(URL url) {
        for (Predicate<URL> item : URL_OPEN_CONNECTION_LISTENER) {
            if (item != null) {
                if (item.test(url)) {
                    break;
                }
            }
        }
    }

    public static void notifyProcessStart(ProcessBuilder builder) {
        for (Predicate<ProcessBuilder> item : PROCESS_START_LISTENER) {
            if (item != null) {
                if (item.test(builder)) {
                    break;
                }
            }
        }
    }

    public static void notifyShutdown(int exitCode, StackTraceElement[] stack) {
        for (BiPredicate<Integer, StackTraceElement[]> item : SHUTDOWN_LISTENER) {
            if (item != null) {
                if (item.test(exitCode, stack)) {
                    break;
                }
            }
        }
    }

    public static void notifyFile(File file) {
        for (Predicate<File> item : FILE_LISTENER) {
            if (item != null) {
                if (item.test(file)) {
                    break;
                }
            }
        }
    }

    public static void notifyStatementExecute(String sql, Statement statement) {
        for (BiPredicate<String, Object> item : SQL_LISTENER) {
            if (item != null) {
                if (item.test(sql, statement)) {
                    break;
                }
            }
        }
    }

    public static void notifyThrowable(Throwable e) {
//        System.out.println("notify-throwable-invoke:" + e.getClass());
        THROWABLE_QUEUE.add(e);
        triggerThrowableDispatchThread();
    }

    public static void triggerThrowableDispatchThread() {
        if (THROWABLE_THREAD_RUNNING.getAndSet(true)) {
            return;
        }
        System.out.println("dispatch-throwable-trigger...");
        Thread thread = new Thread(() -> {
            System.out.println("dispatch-throwable-thread-running...");
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
//            System.out.println("dispatch-throwable-wait...");
            while (THROWABLE_QUEUE.size() > THROWABLE_MAX_QUEUE_SIZE.get()) {
                THROWABLE_QUEUE.poll();
            }
            Throwable e = THROWABLE_QUEUE.take();
//            System.out.println("dispatch-throwable-invoke:" + e.getClass());
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
