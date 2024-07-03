package i2f.log.logger;

import i2f.clock.SystemClock;
import i2f.log.ILogger;
import i2f.log.data.LogData;
import i2f.log.decide.ILogDecider;
import i2f.log.enums.LogLevel;
import i2f.log.holder.LogHolder;
import i2f.log.writer.ILogWriter;
import i2f.lru.ExpireConcurrentMap;
import i2f.trace.ThreadTrace;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:34
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultLogger implements ILogger {
    private String location;
    private ILogDecider decider;
    private ILogWriter writer;
    private long expireTs = 30 * 1000;

    private ExpireConcurrentMap<String, Boolean> expireMap = new ExpireConcurrentMap<>();

    public DefaultLogger(String location, ILogDecider decider, ILogWriter writer) {
        this.location = location;
        this.decider = decider;
        this.writer = writer;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean enableLevel(LogLevel level) {
        String key = level + "#" + location;
        Boolean data = expireMap.get(key);
        if (data != null) {
            return data;
        }
        boolean ret = decider.enableLevel(level, location);
        expireMap.set(key, ret, expireTs, TimeUnit.SECONDS);
        return ret;
    }

    public LogData newLogData(LogLevel level) {
        LogData data = new LogData();
        data.setLocation(location);
        data.setLevel(level);
        data.setDate(new Date(SystemClock.currentTimeMillis()));
        Thread thread = Thread.currentThread();
        data.setThreadName(thread.getName());
        data.setThreadId(thread.getId());
        if (level.level() >= LogLevel.DEBUG.level()) {
            StackTraceElement elem = ThreadTrace.beforeTrace(ILogger.class.getName())[0];
            data.setClassName(elem.getClassName());
            data.setMethodName(elem.getMethodName());
            data.setFileName(elem.getFileName());
            data.setLineNumber(elem.getLineNumber());
        }
        return data;
    }

    public String formatMsg(String format, Object... args) {
        return LogHolder.getMsgFormatter().format(format, args);
    }

    @Override
    public void write(Object meta, LogLevel level, String format, Object... args) {
        if (!decider.enableLevel(level, location)) {
            return;
        }
        LogData data = newLogData(level);
        data.setMsg(formatMsg(format, args));
        data.setMeta(meta);
        writer.write(data);
    }

    @Override
    public void write(Object meta, LogLevel level, Throwable ex, String format, Object... args) {
        if (!decider.enableLevel(level, location)) {
            return;
        }
        LogData data = newLogData(level);
        data.setMsg(formatMsg(format, args));
        data.setMeta(meta);
        data.setEx(ex);
        writer.write(data);
    }
}
