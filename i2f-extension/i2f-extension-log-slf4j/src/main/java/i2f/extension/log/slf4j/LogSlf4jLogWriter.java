package i2f.extension.log.slf4j;

import i2f.log.data.LogData;
import i2f.log.enums.LogLevel;
import i2f.log.writer.ILogWriter;
import i2f.lru.LruMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ice2Faith
 * @date 2024/7/1 13:41
 * @desc
 */
public class LogSlf4jLogWriter implements ILogWriter {
    public static final String WRITER_NAME = "SLF4j";
    private LruMap<String, Logger> CACHE = new LruMap<>(1024);

    @Override
    public void write(LogData data) {
        String location = data.getLocation();
        Logger logger = CACHE.get(location);
        if (logger == null) {
            logger = LoggerFactory.getLogger(location);
            CACHE.put(location, logger);
        }
        LogLevel level = data.getLevel();
        if (level == LogLevel.FATAL) {
            logger.error(data.getMsg(), data.getEx());
        } else if (level == LogLevel.ERROR) {
            logger.error(data.getMsg(), data.getEx());
        } else if (level == LogLevel.WARN) {
            logger.warn(data.getMsg(), data.getEx());
        } else if (level == LogLevel.INFO) {
            logger.info(data.getMsg(), data.getEx());
        } else if (level == LogLevel.DEBUG) {
            logger.debug(data.getMsg(), data.getEx());
        } else if (level == LogLevel.TRACE) {
            logger.trace(data.getMsg(), data.getEx());
        } else {
            logger.info(data.getMsg(), data.getEx());
        }

    }
}
