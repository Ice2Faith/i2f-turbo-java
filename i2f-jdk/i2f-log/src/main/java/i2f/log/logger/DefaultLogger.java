package i2f.log.logger;

import i2f.log.decide.ILogDecider;
import i2f.log.holder.LogHolder;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.logger.AbsLogger;
import i2f.log.writer.ILogWriter;
import i2f.lru.ExpireConcurrentMap;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:34
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultLogger extends AbsLogger {
    protected String location;
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

    @Override
    public String formatMsg(String format, Object... args) {
        return LogHolder.getMsgFormatter().format(format, args);
    }

    @Override
    public void writeLogData(LogData data) {
        writer.write(data);
    }
}
