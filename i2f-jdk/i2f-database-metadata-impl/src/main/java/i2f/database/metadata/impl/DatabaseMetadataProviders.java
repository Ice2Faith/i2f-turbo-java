package i2f.database.metadata.impl;

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
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/12/27 21:33
 * @desc
 */
public class DatabaseMetadataProviders {

    public static DatabaseMetadataProvider getProvider(Connection conn) throws SQLException {
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

    public static DatabaseMetadataProvider findProvider(Connection conn) throws SQLException {
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
}
