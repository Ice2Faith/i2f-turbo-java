package i2f.database.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2026/6/5 21:18
 * @desc
 */
public interface ProxyDialectConnectionFeature {
    String METHOD_REAL_CONNECTION = "getProxyRealConnection";

    Connection getProxyRealConnection() throws SQLException;

    String METHOD_DIALECT = "getProxyDialect";

    String getProxyDialect() throws SQLException;

    String METHOD_REAL_URL = "getProxyRealUrl";

    String getProxyRealUrl() throws SQLException;

    String METHOD_REAL_DRIVER = "getProxyRealDriver";

    Driver getProxyRealDriver() throws SQLException;
}
