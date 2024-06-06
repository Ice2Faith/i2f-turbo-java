package i2f.jdbc.proxy.provider.impl;

import i2f.jdbc.proxy.provider.JdbcInvokeContextProvider;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:52
 * @desc
 */
public class SimpleJdbcInvokeContextProvider implements JdbcInvokeContextProvider<Connection> {
    private Connection conn;

    public SimpleJdbcInvokeContextProvider(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Connection beginContext() {
        return conn;
    }

    @Override
    public Connection getConnection(Connection context) {
        return context;
    }

    @Override
    public void endContext(Connection context, Connection conn) {

    }
}
