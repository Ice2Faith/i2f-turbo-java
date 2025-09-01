package i2f.bindsql.page;

import i2f.bindsql.page.impl.*;
import i2f.database.type.DatabaseDialectMapping;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class PageWrappers {
    public static final ThreadLocal<IPageWrapper> WRAPPER_HOLDER = new ThreadLocal<>();
    public static final CopyOnWriteArraySet<IPageWrapperProvider> PROVIDERS = new CopyOnWriteArraySet<>();
    public static DatabaseDialectMapping DIALECT_MAPPING = new DatabaseDialectMapping() {
        @Override
        public void init() {
            redirect(DatabaseType.MARIADB, DatabaseType.MYSQL);
            redirect(DatabaseType.GBASE, DatabaseType.MYSQL);
            redirect(DatabaseType.OSCAR, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.XU_GU, DatabaseType.MYSQL);
            redirect(DatabaseType.CLICK_HOUSE, DatabaseType.MYSQL);
            redirect(DatabaseType.OCEAN_BASE, DatabaseType.MYSQL);
            redirect(DatabaseType.H2, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.SQLITE, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.HSQL, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.KINGBASE_ES, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.PHOENIX, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.DM, DatabaseType.ORACLE);
            redirect(DatabaseType.GAUSS, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.ORACLE_12C, DatabaseType.SQL_SERVER);
            redirect(DatabaseType.FIREBIRD, DatabaseType.SQL_SERVER);
            redirect(DatabaseType.HighGo, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.Hive, DatabaseType.MYSQL);
            redirect(DatabaseType.YaShanDB, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.TDengine, DatabaseType.MYSQL);
            redirect(DatabaseType.HerdDB, DatabaseType.MYSQL);
            redirect(DatabaseType.Snowflake, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.Databricks, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.RedShift, DatabaseType.POSTGRE_SQL);
            redirect(DatabaseType.Trino, DatabaseType.POSTGRE_SQL);
        }
    };

    static {
        ServiceLoader<IPageWrapperProvider> list = ServiceLoader.load(IPageWrapperProvider.class);
        for (IPageWrapperProvider item : list) {
            PROVIDERS.add(item);
        }
    }


    public static IPageWrapper wrapper(Connection conn) throws SQLException {
        IPageWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DIALECT_MAPPING.dialectOf(conn);
        return wrapper(type);
    }

    public static IPageWrapper wrapper(String jdbcUrl) {
        IPageWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        DatabaseType type = DIALECT_MAPPING.dialectOf(jdbcUrl);
        return wrapper(type);
    }

    public static IPageWrapper wrapper(DatabaseType type) {
        IPageWrapper ret = WRAPPER_HOLDER.get();
        if (ret != null) {
            return ret;
        }
        for (IPageWrapperProvider item : PROVIDERS) {
            if (item == null) {
                continue;
            }
            if (item.support(type)) {
                IPageWrapper pageWrapper = item.getPageWrapper();
                if (pageWrapper != null) {
                    return pageWrapper;
                }
            }
        }

        type = DIALECT_MAPPING.dialectOf(type);

        switch (type) {
            case MYSQL:
                return MysqlPageWrapper.INSTANCE;
            case ORACLE:
                return OraclePageWrapper.INSTANCE;
            case POSTGRE_SQL:
                return PostgreSqlPageWrapper.INSTANCE;
            case DB2:
                return Db2PageWrapper.INSTANCE;
            case SQL_SERVER:
                return SqlServerPageWrapper.INSTANCE;
            case CirroData:
                return CirroDataPageWrapper.INSTANCE;
            case IbmAs400:
                return IbmAs400PageWrapper.INSTANCE;
            case Informix:
                return InformixPageWrapper.INSTANCE;
            default:
                throw new UnsupportedOperationException("un-support auto page route db type");
        }
    }
}
