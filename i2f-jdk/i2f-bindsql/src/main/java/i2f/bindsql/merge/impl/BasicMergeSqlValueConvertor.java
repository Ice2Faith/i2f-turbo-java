package i2f.bindsql.merge.impl;

import i2f.bindsql.BindSql;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:41
 * @desc
 */
public class BasicMergeSqlValueConvertor implements Function<Object, String> {
    public static final DateTimeFormatter FMT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final ThreadLocal<SimpleDateFormat> FMT_S_DATE = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public String apply(Object value) {
        if (value == null) {
            return onNull();
        }
        if (value instanceof Number) {
            return onNumber((Number) value);
        }
        if (value instanceof CharSequence) {
            return onCharSequence((CharSequence) value);
        }
        if (value instanceof Date) {
            return onDate((Date) value);
        }
        if (value instanceof LocalDateTime) {
            return onLocalDateTime((LocalDateTime) value);
        }
        if (value instanceof LocalDate) {
            return onLocalDate((LocalDate) value);
        }
        if (value instanceof LocalTime) {
            return onLocalTime((LocalTime) value);
        }
        return onObject(value);
    }

    public String onNull() {
        return "null";
    }

    public String onNumber(Number value) {
        return String.valueOf(value);
    }

    public String onCharSequence(CharSequence value) {
        return BindSql.escapeSqlString(String.valueOf(value));
    }

    public String onDate(Date value) {
        return BindSql.escapeSqlString(FMT_S_DATE.get().format(value));
    }

    public String onLocalDateTime(LocalDateTime value) {
        return BindSql.escapeSqlString(FMT_DATE_TIME.format(value));
    }

    public String onLocalDate(LocalDate value) {
        return BindSql.escapeSqlString(FMT_DATE.format(value));
    }

    public String onLocalTime(LocalTime value) {
        return BindSql.escapeSqlString(FMT_TIME.format(value));
    }

    public String onObject(Object value) {
        return BindSql.escapeSqlString(String.valueOf(value));
    }
}
