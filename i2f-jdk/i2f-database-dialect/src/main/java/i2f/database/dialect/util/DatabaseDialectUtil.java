package i2f.database.dialect.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2025/6/26 13:59
 */
public class DatabaseDialectUtil {
    public static final ThreadLocal<SimpleDateFormat> sfmt = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String decorateAsSqlString(Object value) {
        String str = String.valueOf(value);
        str = str.replace("'", "''");
        return "'" + str + "'";
    }

    public static String typeStringifier(Object value) {
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        if (value instanceof java.util.Date) {
            String str = sfmt.get().format((java.util.Date) value);
            return decorateAsSqlString(str);
        }
        if (value instanceof LocalDate) {
            String str = dateFormatter.format((LocalDate) value);
            return decorateAsSqlString(str);
        }
        if (value instanceof LocalTime) {
            String str = timeFormatter.format((LocalTime) value);
            return decorateAsSqlString(str);
        }
        if (value instanceof LocalDateTime) {
            String str = dateTimeFormatter.format((LocalDateTime) value);
            return decorateAsSqlString(str);
        }
        return decorateAsSqlString(value);
    }

}
