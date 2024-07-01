package i2f.log.holder;

import i2f.log.decide.ILogDecider;
import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.enums.LogLevel;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:40
 * @desc
 */
public class LogHolder {
    public static final ILogDecider DEFAULT_DECIDER = new DefaultClassNamePattenLogDecider();
    public static final ILogWriter DEFAULT_WRITER = new DefaultBroadcastLogWriter();

    public static ILogDecider GLOBAL_DECIDER = DEFAULT_DECIDER;
    public static ThreadLocal<ILogDecider> THREAD_DECIDER = new ThreadLocal<>();

    public static ILogWriter GLOBAL_WRITER = DEFAULT_WRITER;
    public static ThreadLocal<ILogWriter> THREAD_WRITER = new ThreadLocal<>();

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

}
