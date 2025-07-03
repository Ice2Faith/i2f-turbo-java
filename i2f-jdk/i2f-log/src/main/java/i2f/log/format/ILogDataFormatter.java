package i2f.log.format;

import i2f.log.std.data.LogData;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:34
 * @desc
 */
public interface ILogDataFormatter {
    default String format(LogData data) {
        return format(data, false);
    }

    String format(LogData data, boolean stdout);
}
