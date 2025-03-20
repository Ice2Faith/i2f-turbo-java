package i2f.bindsql.stringify;

import i2f.bindsql.stringify.impl.BasicBindSqlStringifier;
import i2f.bindsql.stringify.impl.dialect.*;
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

    public static BindSqlStringifier of(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return MySqlBindSqlStringifier.INSTANCE;
            case ORACLE:
                return OracleBindSqlStringifier.INSTANCE;
            case MARIADB:
                return MySqlBindSqlStringifier.INSTANCE;
            case GBASE:
                return MySqlBindSqlStringifier.INSTANCE;
            case OSCAR:
                return MySqlBindSqlStringifier.INSTANCE;
            case XU_GU:
                return MySqlBindSqlStringifier.INSTANCE;
            case CLICK_HOUSE:
                return MySqlBindSqlStringifier.INSTANCE;
            case OCEAN_BASE:
                return MySqlBindSqlStringifier.INSTANCE;
            case POSTGRE_SQL:
                return PostgreSqlBindSqlStringifier.INSTANCE;
            case H2:
                return H2BindSqlStringifier.INSTANCE;
            case SQLITE:
                return BasicBindSqlStringifier.INSTANCE;
            case HSQL:
                return BasicBindSqlStringifier.INSTANCE;
            case KINGBASE_ES:
                return MySqlBindSqlStringifier.INSTANCE;
            case PHOENIX:
                return PhoenixBindSqlStringifier.INSTANCE;
            case DM:
                return OracleBindSqlStringifier.INSTANCE;
            case GAUSS:
                return GuassBindSqlStringifier.INSTANCE;
            case ORACLE_12C:
                return OracleBindSqlStringifier.INSTANCE;
            case DB2:
                return Db2BindSqlStringifier.INSTANCE;
            case SQL_SERVER:
                return SqlServerBindSqlStringifier.INSTANCE;
            default:
                return BasicBindSqlStringifier.INSTANCE;
        }
    }
}
