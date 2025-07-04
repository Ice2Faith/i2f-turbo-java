package i2f.bindsql.page;

import i2f.bindsql.page.impl.*;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class PageWrappers {

    public static IPageWrapper wrapper(Connection conn) throws SQLException {
        DatabaseType type = DatabaseType.typeOfConnection(conn);
        return wrapper(type);
    }

    public static IPageWrapper wrapper(String jdbcUrl) {
        DatabaseType type = DatabaseType.typeOfJdbcUrl(jdbcUrl);
        return wrapper(type);
    }

    public static IPageWrapper wrapper(DatabaseType type) {
        ServiceLoader<IPageWrapperProvider> list = ServiceLoader.load(IPageWrapperProvider.class);
        for (IPageWrapperProvider item : list) {
            if (list == null) {
                continue;
            }
            if (item.support(type)) {
                IPageWrapper pageWrapper = item.getPageWrapper();
                if (pageWrapper != null) {
                    return pageWrapper;
                }
            }
        }
        switch (type) {
            case MYSQL:
                return MysqlPageWrapper.INSTANCE;
            case ORACLE:
                return OraclePageWrapper.INSTANCE;
            case MARIADB:
                return MysqlPageWrapper.INSTANCE;
            case GBASE:
                return MysqlPageWrapper.INSTANCE;
            case OSCAR:
                return PostgreSqlPageWrapper.INSTANCE;
            case XU_GU:
                return MysqlPageWrapper.INSTANCE;
            case CLICK_HOUSE:
                return MysqlPageWrapper.INSTANCE;
            case OCEAN_BASE:
                return MysqlPageWrapper.INSTANCE;
            case POSTGRE_SQL:
                return PostgreSqlPageWrapper.INSTANCE;
            case H2:
                return PostgreSqlPageWrapper.INSTANCE;
            case SQLITE:
                return PostgreSqlPageWrapper.INSTANCE;
            case HSQL:
                return PostgreSqlPageWrapper.INSTANCE;
            case KINGBASE_ES:
                return PostgreSqlPageWrapper.INSTANCE;
            case PHOENIX:
                return PostgreSqlPageWrapper.INSTANCE;
            case DM:
                return OraclePageWrapper.INSTANCE;
            case GAUSS:
                return OraclePageWrapper.INSTANCE;
            case ORACLE_12C:
                return SqlServerPageWrapper.INSTANCE;
            case DB2:
                return Db2PageWrapper.INSTANCE;
            case SQL_SERVER:
                return SqlServerPageWrapper.INSTANCE;
            case FIREBIRD:
                return SqlServerPageWrapper.INSTANCE;
            case HighGo:
                return PostgreSqlPageWrapper.INSTANCE;
            case Hive:
                return MysqlPageWrapper.INSTANCE;
            case YaShanDB:
                return PostgreSqlPageWrapper.INSTANCE;
            case TDengine:
                return MysqlPageWrapper.INSTANCE;
            case HerdDB:
                return MysqlPageWrapper.INSTANCE;
            case CirroData:
                return CirroDataPageWrapper.INSTANCE;
            case IbmAs400:
                return IbmAs400PageWrapper.INSTANCE;
            case Informix:
                return InformixPageWrapper.INSTANCE;
            case Snowflake:
                return PostgreSqlPageWrapper.INSTANCE;
            case Databricks:
                return PostgreSqlPageWrapper.INSTANCE;
            case RedShift:
                return PostgreSqlPageWrapper.INSTANCE;
            case Trino:
                return PostgreSqlPageWrapper.INSTANCE;
            default:
                throw new UnsupportedOperationException("un-support auto page route db type");
        }
    }
}
