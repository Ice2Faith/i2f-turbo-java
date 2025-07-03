package i2f.log.writer;

import i2f.log.holder.LogHolder;
import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:28
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class AbsPlainTextLogWriter implements ILogWriter {

    @Override
    public void write(LogData data) {
        String text = LogHolder.getDataFormatter().format(data);
        write(data.getLevel(), text);
    }


    public abstract void write(LogLevel level, String text);
}
