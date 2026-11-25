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

public class OracleMergeSqlValueConvertor extends BasicMergeSqlValueConvertor implements BindSqlMergeSqlValueConvertor {
    public static final OracleMergeSqlValueConvertor INSTANCE = new OracleMergeSqlValueConvertor();

    @Override
    public boolean support(String dialectType) {
        if (dialectType == null) {
            return false;
        }
        dialectType = dialectType.trim().toLowerCase();
        if (dialectType.isEmpty()) {
            return false;
        }
        return dialectType.contains("oracle");
    }

    @Override
    public String onDate(Date value) {
        return "to_date(" + BindSql.escapeSqlString(FMT_S_DATE.get().format(value)) + ",'yyyy-mm-dd hh24:mi:ss')";
    }

    @Override
    public String onLocalDateTime(LocalDateTime value) {
        return "to_date(" + BindSql.escapeSqlString(FMT_DATE_TIME.format(value)) + ",'yyyy-mm-dd hh24:mi:ss')";
    }

    @Override
    public String onLocalDate(LocalDate value) {
        return "to_date(" + BindSql.escapeSqlString(FMT_DATE.format(value)) + ",'yyyy-mm-dd')";
    }

    @Override
    public String onLocalTime(LocalTime value) {
        return "to_date(" + BindSql.escapeSqlString(FMT_TIME.format(value)) + ",'hh24:mi:ss')";
    }
}
