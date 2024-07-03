package i2f.log.format;

import i2f.log.data.LogData;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:34
 * @desc
 */
public interface ILogDataFormatter {
    String format(LogData data);
}
