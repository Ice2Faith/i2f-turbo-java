package i2f.log.holder;

import i2f.log.decide.ILogDecider;
import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.enums.LogLevel;
import i2f.log.format.ILogDataFormatter;
import i2f.log.format.ILogMsgFormatter;
import i2f.log.format.impl.DefaultLogDataFormatter;
import i2f.log.format.impl.IndexedPattenLogMsgFormatter;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:40
 * @desc
 */
public class LogHolder {
    public static final ILogDecider DEFAULT_DECIDER = new DefaultClassNamePattenLogDecider();
    public static final ILogWriter DEFAULT_WRITER = new DefaultBroadcastLogWriter();
    public static final ILogMsgFormatter DEFAULT_MSG_FORMATTER = new IndexedPattenLogMsgFormatter();
    public static final ILogDataFormatter DEFAULT_DATA_FORMATTER = new DefaultLogDataFormatter();

    public static final ThreadLocal<String> TRACE_ID_HOLDER = new ThreadLocal<>();

    public static volatile ILogDecider GLOBAL_DECIDER = DEFAULT_DECIDER;
    public static ThreadLocal<ILogDecider> THREAD_DECIDER = new ThreadLocal<>();

    public static volatile ILogWriter GLOBAL_WRITER = DEFAULT_WRITER;
    public static ThreadLocal<ILogWriter> THREAD_WRITER = new ThreadLocal<>();

    public static volatile ILogMsgFormatter GLOBAL_MSG_FORMATTER = DEFAULT_MSG_FORMATTER;
    public static ThreadLocal<ILogMsgFormatter> THREAD_MSG_FORMATTER = new ThreadLocal<>();

    public static volatile ILogDataFormatter GLOBAL_DATA_FORMATTER = DEFAULT_DATA_FORMATTER;
    public static ThreadLocal<ILogDataFormatter> THREAD_DATA_FORMATTER = new ThreadLocal<>();

    public static void setTraceId(String traceId) {
        TRACE_ID_HOLDER.set(traceId);
    }

    public static String getTraceId() {
        return TRACE_ID_HOLDER.get();
    }

    public static void removeTraceId() {
        TRACE_ID_HOLDER.remove();
    }

    public static String newTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public synchronized static String getOrNewTraceId() {
        String traceId = getTraceId();
        if (traceId == null) {
            traceId = newTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    public static void replaceWriter(ILogWriter writer) {
        if (writer != null) {
            GLOBAL_WRITER = writer;
        }
    }

    public static void replaceDecider(ILogDecider decider) {
        if (decider != null) {
            GLOBAL_DECIDER = decider;
        }
    }

    public static boolean registryWriter(String name, ILogWriter writer) {
        if (GLOBAL_WRITER instanceof DefaultBroadcastLogWriter) {
            DefaultBroadcastLogWriter broadcastLogWriter = (DefaultBroadcastLogWriter) GLOBAL_WRITER;
            broadcastLogWriter.registry(name, writer);
            return true;
        }
        return false;
    }

    public static boolean removeWriter(String name) {
        if (GLOBAL_WRITER instanceof DefaultBroadcastLogWriter) {
            DefaultBroadcastLogWriter broadcastLogWriter = (DefaultBroadcastLogWriter) GLOBAL_WRITER;
            broadcastLogWriter.remove(name);
            return true;
        }
        return false;
    }

    public static boolean registryDecideLevel(String patten, LogLevel level) {
        if (GLOBAL_DECIDER instanceof DefaultClassNamePattenLogDecider) {
            DefaultClassNamePattenLogDecider pattenLogDecider = (DefaultClassNamePattenLogDecider) GLOBAL_DECIDER;
            pattenLogDecider.registry(patten, level);
            return true;
        }
        return false;
    }

    public static boolean removeDecideLevel(String patten) {
        if (GLOBAL_DECIDER instanceof DefaultClassNamePattenLogDecider) {
            DefaultClassNamePattenLogDecider pattenLogDecider = (DefaultClassNamePattenLogDecider) GLOBAL_DECIDER;
            pattenLogDecider.remove(patten);
            return true;
        }
        return false;
    }

    public static ILogDecider getDecider() {
        ILogDecider decider = THREAD_DECIDER.get();
        if (decider == null) {
            decider = GLOBAL_DECIDER;
        }
        return decider;
    }

    public static ILogWriter getWriter() {
        ILogWriter writer = THREAD_WRITER.get();
        if (writer == null) {
            writer = GLOBAL_WRITER;
        }
        return writer;
    }

    public static ILogMsgFormatter getMsgFormatter() {
        ILogMsgFormatter writer = THREAD_MSG_FORMATTER.get();
        if (writer == null) {
            writer = GLOBAL_MSG_FORMATTER;
        }
        return writer;
    }

    public static ILogDataFormatter getDataFormatter() {
        ILogDataFormatter writer = THREAD_DATA_FORMATTER.get();
        if (writer == null) {
            writer = GLOBAL_DATA_FORMATTER;
        }
        return writer;
    }

}
