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

public class SqliteMergeSqlValueConvertor extends BasicMergeSqlValueConvertor implements BindSqlMergeSqlValueConvertor {
    public static final SqliteMergeSqlValueConvertor INSTANCE = new SqliteMergeSqlValueConvertor();

    @Override
    public boolean support(String dialectType) {
        if (dialectType == null) {
            return false;
        }
        dialectType = dialectType.trim().toLowerCase();
        if (dialectType.isEmpty()) {
            return false;
        }
        return dialectType.contains("sqlite");
    }

    @Override
    public String onDate(Date value) {
        return "datetime(" + BindSql.escapeSqlString(FMT_S_DATE.get().format(value)) + ")";
    }

    @Override
    public String onLocalDateTime(LocalDateTime value) {
        return "datetime(" + BindSql.escapeSqlString(FMT_DATE_TIME.format(value)) + ")";
    }

    @Override
    public String onLocalDate(LocalDate value) {
        return "date(" + BindSql.escapeSqlString(FMT_DATE.format(value)) + ")";
    }

    @Override
    public String onLocalTime(LocalTime value) {
        return "time(" + BindSql.escapeSqlString(FMT_TIME.format(value)) + ")";
    }

    @Override
    public String onObject(Object value) {
        // sqlite 没有 boolean 类型，统一以 1/0 存储，避免出现 'true'/'false' 与整数比较失配
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        }
        return super.onObject(value);
    }
}
