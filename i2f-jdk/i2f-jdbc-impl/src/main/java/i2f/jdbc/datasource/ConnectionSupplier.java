package i2f.jdbc.datasource;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/1/23 15:36
 */
public interface ConnectionSupplier {
    Connection get() throws SQLException;
}
