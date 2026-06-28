package i2f.mixin.impl;


import i2f.clock.SystemClock;
import i2f.convert.obj.ObjectConvertor;
import i2f.mixin.consts.MixinConsts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2026/5/14 8:56
 * @desc
 */
public interface DateMixins {
    default long current_time_millis() {
        return System.currentTimeMillis();
    }

    default long current_time_seconds() {
        return System.currentTimeMillis() / 1000;
    }

    default String format(Date date, String fmt) {
        if (date == null || fmt == null) {
            return String.valueOf(date);
        }
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.format(date);
    }

    default Date sysdate() {
        return new Date(SystemClock.currentTimeMillis());
    }

    default Date now() {
        return new Date(SystemClock.currentTimeMillis());
    }

    default long timestamp() {
        return SystemClock.currentTimeSeconds();
    }

    default Date timestamp_to_date(Object ts) {
        long lts = -1;
        if (ts == null) {
            return null;
        }
        if (ts instanceof Long) {
            lts = (Long) ts;
        } else {
            lts = Long.parseLong(String.valueOf(ts));
        }
        return new Date(lts * 1000);
    }

    default Long date_to_timestamp(Object date) {
        if (date == null) {
            return null;
        }
        Date dt = (Date) ObjectConvertor.tryConvertAsType(date, Date.class);
        return dt.getTime() / 1000;
    }

    default Date parse_date(String dt, String fmt) throws ParseException {
        if (dt == null || fmt == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.parse(dt);
    }

    default ChronoUnit chrono_unit(String unit) {
        if (unit == null || unit.isEmpty()) {
            return ChronoUnit.DAYS;
        }
        unit = unit.trim();
        try {
            ChronoUnit ret = ChronoUnit.valueOf(unit.toUpperCase());
            if (ret != null) {
                return ret;
            }
        } catch (IllegalArgumentException e) {

        }

        for (String[] arr : MixinConsts.CHRONO_UNIT_MAPPING) {
            if (arr[0].equalsIgnoreCase(unit)) {
                try {
                    ChronoUnit ret = ChronoUnit.valueOf(arr[1].toUpperCase());
                    if (ret != null) {
                        return ret;
                    }
                } catch (IllegalArgumentException e) {

                }
            }
        }
        return ChronoUnit.DAYS;
    }

    default Object date_sub(Object date, String unit, long interval) {
        return date_add(date, unit, -interval);
    }

    default Object date_add(Object date, String unit, long interval) {
        if (date == null) {
            return null;
        }
        ChronoUnit chronoUnit = chrono_unit(unit);
        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.plus(interval, chronoUnit);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    default Date to_date(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return ObjectConvertor.tryParseDate(String.valueOf(obj));
    }

    default Date to_date(Object obj, String pattern) {
        String str = null;
        if (obj == null) {
            str = null;
        } else if (obj instanceof CharSequence
                || obj instanceof Appendable
                || obj instanceof String) {
            str = String.valueOf(obj);
        } else {
            str = String.valueOf(obj);
        }
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            Object ret = ObjectConvertor.tryConvertAsType(str, Date.class);
            if (ret instanceof Date) {
                return (Date) ret;
            }
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default Date str_to_date(Object obj) {
        return to_date(obj);
    }

    default Date str_to_date(Object obj, String pattern) {
        return to_date(obj, pattern);
    }

    default String date_format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date instanceof Date) {
            return new SimpleDateFormat(pattern).format(date);
        } else if (date instanceof Temporal) {
            return DateTimeFormatter.ofPattern(pattern).format((Temporal) date);
        }
        throw new IllegalArgumentException("un-support date type[" + date.getClass() + "] format!");
    }

    default Object last_day(Object date) {
        if (date == null) {
            return null;
        }

        LocalDateTime v = (LocalDateTime) ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        LocalDateTime rv = v.withDayOfMonth(1).plusMonths(1).plusDays(-1);
        Class<?> rawType = date.getClass();
        return ObjectConvertor.tryConvertAsType(rv, rawType);
    }

    default Object add_months(Object date, long interval) {
        return date_add(date, ChronoUnit.MONTHS.name(), interval);
    }

    default Object add_days(Object date, long interval) {
        return date_add(date, ChronoUnit.DAYS.name(), interval);
    }

    default Object first_day(Object date) {
        if (date == null) {
            return null;
        }
        Object obj = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(obj instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime time = (LocalDateTime) obj;
        time = time
                .withDayOfMonth(1);
        return ObjectConvertor.tryConvertAsType(time, date.getClass());
    }

    default int day(Object date) {
        return extract(ChronoUnit.DAYS.name(), date);
    }

    default int month(Object date) {
        return extract(ChronoUnit.MONTHS.name(), date);
    }

    default int year(Object date) {
        return extract(ChronoUnit.YEARS.name(), date);
    }

    default int hour(Object date) {
        return extract(ChronoUnit.HOURS.name(), date);
    }

    default int minute(Object date) {
        return extract(ChronoUnit.MINUTES.name(), date);
    }

    default int second(Object date) {
        return extract(ChronoUnit.SECONDS.name(), date);
    }

    default int week(Object date) {
        return extract(ChronoUnit.WEEKS.name(), date);
    }

    default int extract(String fmt, Object date) {
        if (date == null) {
            return -1;
        }
        Object obj = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(obj instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime time = (LocalDateTime) obj;
        ChronoUnit unit = chrono_unit(fmt);
        if (unit == ChronoUnit.YEARS) {
            return time.getYear();
        } else if (unit == ChronoUnit.MONTHS) {
            return time.getMonth().getValue();
        } else if (unit == ChronoUnit.DAYS) {
            return time.getDayOfMonth();
        } else if (unit == ChronoUnit.HOURS) {
            return time.getHour();
        } else if (unit == ChronoUnit.MINUTES) {
            return time.getMinute();
        } else if (unit == ChronoUnit.SECONDS) {
            return time.getSecond();
        } else if (unit == ChronoUnit.MILLIS) {
            return time.getNano() / 1000 / 1000;
        } else if (unit == ChronoUnit.MICROS) {
            return time.getNano() / 1000;
        } else if (unit == ChronoUnit.NANOS) {
            return time.getNano();
        } else if (unit == ChronoUnit.WEEKS) {
            return time.getDayOfWeek().getValue();
        }
        return -1;
    }

    default Object trunc(Object date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "dd";
        }
        Object time = ObjectConvertor.tryConvertAsType(date, LocalDateTime.class);
        if ((!(time instanceof LocalDateTime))) {
            throw new IllegalArgumentException("date cannot cast as date type, of type :" + date.getClass());
        }
        LocalDateTime obj = (LocalDateTime) time;
        if ("dd".equalsIgnoreCase(format)
                || "d".equalsIgnoreCase(format)
                || "day".equalsIgnoreCase(format)
                || "days".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0);
        } else if ("MM".equals(format)
                || "mon".equalsIgnoreCase(format)
                || "month".equalsIgnoreCase(format)
                || "months".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .withDayOfMonth(1);
        } else if ("yyyy".equalsIgnoreCase(format)
                || "yy".equalsIgnoreCase(format)
                || "year".equalsIgnoreCase(format)
                || "years".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .withDayOfMonth(1)
                    .withMonth(1);
        } else if ("HH".equalsIgnoreCase(format)
                || "hh24".equalsIgnoreCase(format)
                || "hh12".equalsIgnoreCase(format)
                || "hour".equalsIgnoreCase(format)
                || "hours".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0);
        } else if ("mm".equals(format) || "mi".equalsIgnoreCase(format)
                || "min".equalsIgnoreCase(format)
                || "minute".equalsIgnoreCase(format)
                || "minutes".equalsIgnoreCase(format)) {
            obj = obj.withNano(0)
                    .withSecond(0);
        } else if ("ss".equalsIgnoreCase(format)
                || "sec".equalsIgnoreCase(format)
                || "second".equalsIgnoreCase(format)
                || "seconds".equalsIgnoreCase(format)) {
            obj = obj.withNano(0);
        } else if ("SSS".equalsIgnoreCase(format)
                || "mill".equalsIgnoreCase(format)
                || "mills".equalsIgnoreCase(format)
                | "ms".equalsIgnoreCase(format)) {
            obj = obj.withNano(obj.getNano() / 1000_000 * 1000_000);
        } else if ("week".equalsIgnoreCase(format)
                || "iw".equalsIgnoreCase(format)) {
            DayOfWeek week = obj.getDayOfWeek();
            int day = week.getValue();
            obj = obj.plusDays(day - 1);
        } else if ("quarter".equalsIgnoreCase(format)
                || "q".equalsIgnoreCase(format)) {
            Month month = obj.getMonth();
            Month quarterFirstMonth = Month.of(((month.getValue() - 1) / 3) * 3 + 1);
            obj = obj.withNano(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withHour(0)
                    .withDayOfMonth(1)
                    .withMonth(quarterFirstMonth.getValue());
        } else {
            throw new IllegalArgumentException("un-support date trunc format :" + format);
        }

        return ObjectConvertor.tryConvertAsType(obj, date.getClass());
    }

}
