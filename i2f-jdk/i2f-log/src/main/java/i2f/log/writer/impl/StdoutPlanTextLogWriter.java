package i2f.log.writer.impl;

import i2f.log.holder.LogHolder;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.writer.AbsPlainTextLogWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:58
 * @desc
 */
@Data
@NoArgsConstructor
public class StdoutPlanTextLogWriter extends AbsPlainTextLogWriter {
    public static final String WRITER_NAME = "STDOUT";

    @Override
    public void write(LogData data) {
        String text = LogHolder.getDataFormatter().format(data, true);
        write(data.getLevel(), text);
    }

    @Override
    public void write(LogLevel level, String text) {
        if (level.level() >= LogLevel.INFO.level()) {
            System.out.println(text);
        } else {
            System.err.println(text);
        }
    }
}
