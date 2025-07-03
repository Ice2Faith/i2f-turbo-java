package i2f.log.std.data;

import i2f.log.std.enums.LogLevel;
import lombok.Data;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:26
 * @desc
 */
@Data
public class LogData {
    private String location;
    private LogLevel level;
    private Date date;

    private String msg;
    private Throwable ex;

    private Object meta;

    private String threadName;
    private long threadId;

    private String className;
    private String methodName;
    private String fileName;
    private int lineNumber;

    private String traceId;
}
