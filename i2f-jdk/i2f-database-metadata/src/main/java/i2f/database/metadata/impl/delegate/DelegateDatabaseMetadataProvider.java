package i2f.database.metadata.impl.delegate;

import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.data.TableMeta;
import i2f.jdbc.data.QueryResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/12/26 9:49
 */
@Data
@NoArgsConstructor
public class DelegateDatabaseMetadataProvider implements DatabaseMetadataProvider {
    protected final CopyOnWriteArrayList<DatabaseMetadataProvider> providers = new CopyOnWriteArrayList<>();

    public DelegateDatabaseMetadataProvider(List<DatabaseMetadataProvider> providers) {
        this.providers.addAll(providers);
    }

    public DelegateDatabaseMetadataProvider(DatabaseMetadataProvider... providers) {
        this.providers.addAll(Arrays.asList(providers));
    }

    @Override
    public String detectDefaultDatabase(Connection conn) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.detectDefaultDatabase(conn);
            } catch (Throwable e) {
                ex = e;
            }
        }
        return null;
    }

    @Override
    public String detectDefaultDatabase(String jdbcUrl) {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.detectDefaultDatabase(jdbcUrl);
            } catch (Throwable e) {
                ex = e;
            }
        }
        return null;
    }

    @Override
    public List<String> getDataTypes(Connection conn) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getDataTypes(conn);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public List<String> getCatalogs(Connection conn) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getCatalogs(conn);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public List<String> getSchemas(Connection conn) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getSchemas(conn);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getDatabases(conn);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public List<TableMeta> getTables(Connection conn, String database) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getTables(conn, database);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public TableMeta getTableInfo(Connection conn, String database, String table) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getTableInfo(conn, database, table);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return getTableInfoByQuery(conn, table);
    }

    @Override
    public TableMeta getTableInfoByQuery(QueryResult result) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getTableInfoByQuery(result);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public TableMeta getTableInfoByQuery(ResultSet rs) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getTableInfoByQuery(rs);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public TableMeta getTableInfoByQuery(Connection conn, String table) throws SQLException {
        Throwable ex = null;
        for (DatabaseMetadataProvider provider : providers) {
            try {
                return provider.getTableInfoByQuery(conn, table);
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (ex != null) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new SQLException(ex.getMessage(), ex);
            }
        }
        return null;
    }


}
