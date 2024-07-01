package i2f.log.decide;

import i2f.log.data.LogData;
import i2f.log.enums.LogLevel;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:00
 * @desc
 */
public interface ILogDecider {
    boolean enableLevel(LogLevel level, String location);

    default boolean enableLevel(LogData data) {
        return enableLevel(data.getLevel(), data.getLocation());
    }
}
