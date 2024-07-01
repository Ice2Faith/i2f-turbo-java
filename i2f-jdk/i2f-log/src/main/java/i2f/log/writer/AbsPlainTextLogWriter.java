package i2f.log.writer;

import i2f.log.data.LogData;
import i2f.log.enums.LogLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:28
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class AbsPlainTextLogWriter implements ILogWriter {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    @Override
    public void write(LogData data) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(data.getDate()));
        builder.append(String.format(" [%5s]", String.valueOf(data.getLevel())));
        builder.append(String.format(" %32s", data.getLocation()));
        builder.append(String.format(" [%8s-%3d]", data.getThreadName(), data.getThreadId()));
        builder.append(" : ").append(data.getMsg());
        if (data.getMethodName() != null) {
            builder.append(String.format(" --@%s(%s:%d)", data.getMethodName(), data.getFileName(), data.getLineNumber()));
        }
        if (data.getEx() != null) {
            Throwable ex = data.getEx();
            builder.append("\nexception : ").append(ex.getMessage());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            ex.printStackTrace(ps);
            ps.close();
            builder.append("\n")
                    .append(new String(bos.toByteArray()));
        }
        write(data.getLevel(), builder.toString());
    }

    public abstract void write(LogLevel level, String text);
}
