package i2f.jdbc.context.impl;

import i2f.jdbc.std.context.JdbcInvokeContextProvider;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:52
 * @desc
 */
public class DirectJdbcInvokeContextProvider implements JdbcInvokeContextProvider<Connection> {
    private Connection conn;

    public DirectJdbcInvokeContextProvider(Connection conn) {
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
