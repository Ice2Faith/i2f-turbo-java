package i2f.database.metadata;

import i2f.database.metadata.data.TableMeta;
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
