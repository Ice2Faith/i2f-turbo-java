package i2f.bindsql.page;

import i2f.bindsql.page.impl.*;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class PageWrappers {
    public static final IPageWrapper MYSQL = new MysqlPageWrapper();
    public static final IPageWrapper ORACLE = new OraclePageWrapper();
    public static final IPageWrapper POSTGRE_SQL = new PostgreSqlPageWrapper();
    public static final IPageWrapper ORACLE_12C = new Oracle12cPageWrapper();
    public static final IPageWrapper DB2 = new Db2PageWrapper();
    public static final IPageWrapper SQL_SERVER = new SqlServerPageWrapper();

    public static IPageWrapper wrapper(Connection conn) throws SQLException {
        DatabaseType type = DatabaseType.typeOfConnection(conn);
        return wrapper(type);
    }

    public static IPageWrapper wrapper(String jdbcUrl) {
        DatabaseType type = DatabaseType.typeOfJdbcUrl(jdbcUrl);
        return wrapper(type);
    }


    public static IPageWrapper wrapper(DatabaseType type) {
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
                return POSTGRE_SQL;
            case H2:
                return POSTGRE_SQL;
            case SQLITE:
                return POSTGRE_SQL;
            case HSQL:
                return POSTGRE_SQL;
            case KINGBASE_ES:
                return POSTGRE_SQL;
            case PHOENIX:
                return POSTGRE_SQL;
            case DM:
                return ORACLE;
            case GAUSS:
                return ORACLE;
            case ORACLE_12C:
                return ORACLE_12C;
            case DB2:
                return DB2;
            case SQL_SERVER:
                return SQL_SERVER;
            default:
                throw new UnsupportedOperationException("un-support auto page route db type");
        }
    }
}
