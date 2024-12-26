package i2f.database.metadata;

import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.delegate.DelegateDatabaseMetadataProvider;
import i2f.database.metadata.impl.dm.DmDatabaseMetadataProvider;
import i2f.database.metadata.impl.gbase.GbaseDatabaseMetadataProvider;
import i2f.database.metadata.impl.h2.H2DatabaseMetadataProvider;
import i2f.database.metadata.impl.jdbc.JdbcDatabaseMetadataProvider;
import i2f.database.metadata.impl.mysql.MysqlDatabaseMetadataProvider;
import i2f.database.metadata.impl.oracle.OracleDatabaseMetadataProvider;
import i2f.database.metadata.impl.postgresql.PostgreSqlDatabaseMetadataProvider;
import i2f.database.metadata.impl.sqlite3.Sqlite3DatabaseMetadataProvider;
import i2f.database.metadata.impl.sqlserver.SqlServerDatabaseMetadataProvider;
import i2f.database.type.DatabaseType;
import i2f.jdbc.data.QueryResult;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:48
 * @desc
 */
public interface DatabaseMetadataProvider {
    static List<Driver> getSpiDrivers() {
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        List<Driver> ret = new ArrayList<>();
        for (Driver driver : loader) {
            ret.add(driver);
        }
        return ret;
    }

    static String getProductName(Connection conn) throws SQLException {
        return conn.getMetaData().getDatabaseProductName();
    }

    static String getProductVersion(Connection conn) throws SQLException {
        return conn.getMetaData().getDatabaseProductVersion();
    }

    static String getDriverName(Connection conn) throws SQLException {
        return conn.getMetaData().getDriverName();
    }

    static String getDriverVersion(Connection conn) throws SQLException {
        return conn.getMetaData().getDriverVersion();
    }

    static String getConnectionUrl(Connection conn) throws SQLException {
        return conn.getMetaData().getURL();
    }

    static String getUsername(Connection conn) throws SQLException {
        return conn.getMetaData().getUserName();
    }

    static DatabaseMetadataProvider getProvider(Connection conn) throws SQLException {
        DatabaseMetadataProvider provider = findProvider(conn);
        List<DatabaseMetadataProvider> providers = new ArrayList<>();
        if (provider != null) {
            providers.add(provider);
        }
        if (!(provider instanceof JdbcDatabaseMetadataProvider)) {
            providers.add(JdbcDatabaseMetadataProvider.INSTANCE);
        }
        return new DelegateDatabaseMetadataProvider(providers);
    }

    static DatabaseMetadataProvider findProvider(Connection conn) throws SQLException {
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        String productName = conn.getMetaData().getDatabaseProductName();
        if (DatabaseType.MYSQL == databaseType
                || DatabaseType.MARIADB == databaseType) {
            return MysqlDatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.ORACLE == databaseType
                || DatabaseType.ORACLE_12C == databaseType) {
            return OracleDatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.GBASE == databaseType) {
            return GbaseDatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.SQLITE == databaseType) {
            return Sqlite3DatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.POSTGRE_SQL == databaseType) {
            return PostgreSqlDatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.DM == databaseType) {
            return DmDatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.H2 == databaseType) {
            return H2DatabaseMetadataProvider.INSTANCE;
        } else if (DatabaseType.SQL_SERVER == databaseType
                || DatabaseType.SQL_SERVER2005 == databaseType) {
            return SqlServerDatabaseMetadataProvider.INSTANCE;
        }
        System.err.println("un-support metadata provider for product: " + productName);
        return JdbcDatabaseMetadataProvider.INSTANCE;
    }

    default String detectDefaultDatabase(Connection conn) throws SQLException {
        String jdbcUrl = getConnectionUrl(conn);
        return detectDefaultDatabase(jdbcUrl);
    }

    default String detectDefaultDatabase(String jdbcUrl) {
        return null;
    }

    List<String> getDataTypes(Connection conn) throws SQLException;

    List<String> getCatalogs(Connection conn) throws SQLException;

    List<String> getSchemas(Connection conn) throws SQLException;

    List<String> getDatabases(Connection conn) throws SQLException;

    List<TableMeta> getTables(Connection conn, String database) throws SQLException;

    TableMeta getTableInfo(Connection conn, String database, String table) throws SQLException;

    TableMeta getTableInfoByQuery(QueryResult result) throws SQLException;

    TableMeta getTableInfoByQuery(ResultSet rs) throws SQLException;

    TableMeta getTableInfoByQuery(Connection conn, String table) throws SQLException;
}
