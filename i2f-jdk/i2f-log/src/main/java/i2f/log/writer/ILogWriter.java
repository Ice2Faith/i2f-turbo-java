package i2f.log.writer;

import i2f.log.data.LogData;

/**
 * @author Ice2Faith
 * @date 2024/7/1 9:58
 * @desc
 */
public interface ILogWriter {
    void write(LogData data);
}
