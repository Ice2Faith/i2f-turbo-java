package i2f.bindsql.stringify;

import i2f.bindsql.BindSql;
import i2f.database.type.DatabaseType;

/**
 * @author Ice2Faith
 * @date 2025/3/20 14:55
 */
public interface BindSqlStringifier {
    boolean support(DatabaseType databaseType);

    String stringify(BindSql bql);
}
