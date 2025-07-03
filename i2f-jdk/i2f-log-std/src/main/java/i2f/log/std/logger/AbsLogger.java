package i2f.log.std.logger;

import i2f.log.std.ILogger;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.mdc.LogMdcHolder;
import i2f.log.std.util.LogUtil;

/**
 * @author Ice2Faith
 * @date 2025/7/3 15:54
 */
public abstract class AbsLogger implements ILogger {

    @Override
    public void setTraceId(String traceId) {
        LogMdcHolder.setTraceId(traceId);
    }

    @Override
    public String getTraceId() {
        return LogMdcHolder.getTraceId();
    }

    @Override
    public void removeTraceId() {
        LogMdcHolder.removeTraceId();
    }

    @Override
    public String newTraceId() {
        return LogMdcHolder.newTraceId();
    }

    @Override
    public String getOrNewTraceId() {
        return LogMdcHolder.getOrNewTraceId();
    }

    @Override
    public void write(Object meta, LogLevel level, String format, Object... args) {
        if (!enableLevel(level)) {
            return;
        }
        LogData data = newLogData(level);
        data.setMsg(formatMsg(format, args));
        data.setMeta(meta);
        writeLogData(data);
    }

    @Override
    public void write(Object meta, LogLevel level, Throwable ex, String format, Object... args) {
        if (!enableLevel(level)) {
            return;
        }
        LogData data = newLogData(level);
        data.setMsg(formatMsg(format, args));
        data.setMeta(meta);
        data.setEx(ex);
        writeLogData(data);
    }

    public String formatMsg(String format, Object... args) {
        return LogUtil.formatMsg(format, args);
    }

    public LogData newLogData(LogLevel level) {
        return LogUtil.newLogData(level, getLocation());
    }

    public abstract void writeLogData(LogData data);

}
