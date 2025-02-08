package i2f.jdbc.datasource.impl;

import i2f.jdbc.datasource.ConnectionSupplier;
import i2f.jdbc.meta.JdbcMeta;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author Ice2Faith
 * @date 2025/1/23 15:21
 */
@NoArgsConstructor
public class DirectConnectionDatasource implements DataSource {
    protected ConnectionSupplier connectionSupplier;
    protected PrintWriter printWriter = new PrintWriter(System.out);
    protected int loginTimeout = 0;

    public DirectConnectionDatasource(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public DirectConnectionDatasource(JdbcMeta meta) {
        this.connectionSupplier = new JdbcMetaConnectionSupplier(meta);
    }

    public ConnectionSupplier getConnectionSupplier() {
        return this.connectionSupplier;
    }

    public void setConnectionSupplier(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionSupplier.get();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connectionSupplier.get();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.printWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.printWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.loginTimeout = seconds;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getGlobal();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("un-support datasource unwrap call!");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
