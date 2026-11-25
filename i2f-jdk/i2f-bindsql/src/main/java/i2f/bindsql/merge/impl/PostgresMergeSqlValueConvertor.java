package i2f.bindsql.merge.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.merge.BindSqlMergeSqlValueConvertor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:43
 * @desc
 */

public class PostgresMergeSqlValueConvertor extends BasicMergeSqlValueConvertor implements BindSqlMergeSqlValueConvertor {
    public static final PostgresMergeSqlValueConvertor INSTANCE = new PostgresMergeSqlValueConvertor();

    @Override
    public boolean support(String dialectType) {
        if (dialectType == null) {
            return false;
        }
        dialectType = dialectType.trim().toLowerCase();
        if (dialectType.isEmpty()) {
            return false;
        }
        // postgres / postgresql / postgre
        return dialectType.contains("postgre");
    }

    @Override
    public String onDate(Date value) {
        return BindSql.escapeSqlString(FMT_S_DATE.get().format(value)) + "::timestamp";
    }

    @Override
    public String onLocalDateTime(LocalDateTime value) {
        return BindSql.escapeSqlString(FMT_DATE_TIME.format(value)) + "::timestamp";
    }

    @Override
    public String onLocalDate(LocalDate value) {
        return BindSql.escapeSqlString(FMT_DATE.format(value)) + "::date";
    }

    @Override
    public String onLocalTime(LocalTime value) {
        return BindSql.escapeSqlString(FMT_TIME.format(value)) + "::time";
    }

    @Override
    public String onObject(Object value) {
        // postgres 的 boolean 类型不接受 'true'/'1' 这类字符串隐式转换边界的歧义，直接输出字面量
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "true" : "false";
        }
        return super.onObject(value);
    }
}
