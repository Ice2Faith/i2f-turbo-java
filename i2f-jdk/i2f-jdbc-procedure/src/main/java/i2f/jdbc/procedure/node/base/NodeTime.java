package i2f.jdbc.procedure.node.base;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/1/23 11:43
 */
public class NodeTime {
    public static TimeUnit getTimeUnit(String timeUnit, TimeUnit defVal) {
        TimeUnit unit = defVal;
        if ("SECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MILLISECONDS;
        } else if ("MILLISECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MILLISECONDS;
        } else if ("NANOSECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.NANOSECONDS;
        } else if ("MICROSECONDS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MICROSECONDS;
        } else if ("MINUTES".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.MINUTES;
        } else if ("HOURS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.HOURS;
        } else if ("DAYS".equalsIgnoreCase(timeUnit)) {
            unit = TimeUnit.DAYS;
        }
        return unit;
    }
}
