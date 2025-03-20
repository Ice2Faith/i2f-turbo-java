package i2f.bindsql.stringify;

import i2f.bindsql.BindSql;

/**
 * @author Ice2Faith
 * @date 2025/3/20 14:55
 */
@FunctionalInterface
public interface BindSqlStringifier {
    String stringify(BindSql bql);
}
