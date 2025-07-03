package i2f.log.std.logger;

import i2f.log.std.ILogger;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.util.LogUtil;

/**
 * @author Ice2Faith
 * @date 2025/7/3 15:54
 */
public abstract class AbsLogger implements ILogger {

    @Override
    public void write(Object meta, LogLevel level, String format, Object... args) {
        try {
            if (!enableLevel(level)) {
                return;
            }
            LogData data = newLogData(level);
            data.setMsg(formatMsg(format, args));
            data.setMeta(meta);
            writeLogData(data);
        } catch (Throwable e) {

        }
    }

    @Override
    public void write(Object meta, LogLevel level, Throwable ex, String format, Object... args) {
        try {
            if (!enableLevel(level)) {
                return;
            }
            LogData data = newLogData(level);
            data.setMsg(formatMsg(format, args));
            data.setMeta(meta);
            data.setEx(ex);
            writeLogData(data);
        } catch (Throwable e) {

        }
    }

    public String formatMsg(String format, Object... args) {
        if (format == null || format.isEmpty()) {
            return LogUtil.formatArgs(new StringBuilder(), -1, -1, args).toString();
        }
        return LogUtil.formatMsg(format, args);
    }

    public LogData newLogData(LogLevel level) {
        return LogUtil.newLogData(level, getLocation());
    }

    public abstract void writeLogData(LogData data);

}
