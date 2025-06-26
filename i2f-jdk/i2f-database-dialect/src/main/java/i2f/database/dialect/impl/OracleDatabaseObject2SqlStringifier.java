package i2f.database.dialect.impl;


import i2f.database.dialect.util.DatabaseDialectUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class OracleDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final OracleDatabaseObject2SqlStringifier INSTANCE = new OracleDatabaseObject2SqlStringifier();

    @Override
    public boolean support(String databaseType) {
        if (databaseType == null) {
            return false;
        }
        String name = databaseType.toLowerCase().trim();
        if (name.startsWith("oracle")) {
            return true;
        }
        return Arrays.asList(
                "oracle",
                "ora",
                "oracle_12c",
                "dm",
                "dameng"
        ).contains(name);
    }

    @Override
    public String toSql(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.util.Date) {
            String str = DatabaseDialectUtil.sfmt.get().format((java.util.Date) value);
            return toDate(str, "yyyy-MM-dd HH24:mi:ss");
        }
        if (value instanceof LocalDate) {
            String str = DatabaseDialectUtil.dateFormatter.format((LocalDate) value);
            return toDate(str, "yyyy-MM-dd");
        }
        if (value instanceof LocalTime) {
            String str = DatabaseDialectUtil.timeFormatter.format((LocalTime) value);
            return toDate(str, "HH24:mi:ss");
        }
        if (value instanceof LocalDateTime) {
            String str = DatabaseDialectUtil.dateTimeFormatter.format((LocalDateTime) value);
            return toDate(str, "yyyy-MM-dd HH24:mi:ss");
        }
        // 不处理
        return null;
    }

    public static String toDate(String date, String oraPattern) {
        return "TO_DATE(" + DatabaseDialectUtil.decorateAsSqlString(date) + "," + DatabaseDialectUtil.decorateAsSqlString(oraPattern) + ")";
    }
}
