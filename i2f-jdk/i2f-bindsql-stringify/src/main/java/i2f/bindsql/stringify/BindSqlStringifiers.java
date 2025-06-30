package i2f.bindsql.stringify;

import i2f.bindsql.stringify.impl.WrappedBindSqlStringifier;
import i2f.database.dialect.impl.dialect.*;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/3/20 15:31
 */
public class BindSqlStringifiers {
    public static BindSqlStringifier of(Connection conn) throws SQLException {
        DatabaseType type = DatabaseType.typeOfConnection(conn);
        return of(type);
    }

    public static BindSqlStringifier of(String jdbcUrl) {
        DatabaseType type = DatabaseType.typeOfJdbcUrl(jdbcUrl);
        return of(type);
    }

    public static final WrappedBindSqlStringifier DEFAULT = WrappedBindSqlStringifier.INSTANCE;

    public static final WrappedBindSqlStringifier MYSQL = new WrappedBindSqlStringifier(MysqlDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier ORACLE = new WrappedBindSqlStringifier(OracleDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier POSTGRE = new WrappedBindSqlStringifier(PostgreSqlDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier H2 = new WrappedBindSqlStringifier(H2DatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier PHOENIX = new WrappedBindSqlStringifier(PhoenixDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier GUASS = new WrappedBindSqlStringifier(GuassDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier DB2 = new WrappedBindSqlStringifier(Db2DatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier SQL_SERVER = new WrappedBindSqlStringifier(SqlServerDatabaseObject2SqlStringifier.INSTANCE);
    public static final WrappedBindSqlStringifier HIVE = new WrappedBindSqlStringifier(HiveDatabaseObject2SqlStringifier.INSTANCE);

    public static BindSqlStringifier of(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return MYSQL;
            case ORACLE:
                return ORACLE;
            case MARIADB:
                return MYSQL;
            case GBASE:
                return MYSQL;
            case OSCAR:
                return MYSQL;
            case XU_GU:
                return MYSQL;
            case CLICK_HOUSE:
                return MYSQL;
            case OCEAN_BASE:
                return MYSQL;
            case POSTGRE_SQL:
                return POSTGRE;
            case H2:
                return H2;
            case SQLITE:
                return DEFAULT;
            case HSQL:
                return DEFAULT;
            case KINGBASE_ES:
                return MYSQL;
            case PHOENIX:
                return PHOENIX;
            case DM:
                return ORACLE;
            case GAUSS:
                return GUASS;
            case ORACLE_12C:
                return ORACLE;
            case DB2:
                return DB2;
            case SQL_SERVER:
                return SQL_SERVER;
            case Hive:
                return HIVE;
            default:
                return DEFAULT;
        }
    }
}
