package i2f.jdbc.cursor.impl;

import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.std.func.SQLBiFunction;
import i2f.jdbc.std.func.SQLFunction;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/6/25 16:10
 */
public class JdbcCursorImpl<E, CTX> implements JdbcCursor<E>, Closeable, Iterable<E> {
    protected Statement stat;
    protected ResultSet rs;
    protected SQLFunction<ResultSet, CTX> contextInitializer;
    protected SQLBiFunction<CTX, ResultSet, E> rowConvertor;
    protected AtomicReference<CTX> context;
    protected boolean hasMore = false;

    public JdbcCursorImpl(Statement stat, ResultSet rs, SQLBiFunction<CTX, ResultSet, E> rowConvertor, SQLFunction<ResultSet, CTX> contextInitializer) {
        this.stat = stat;
        this.rs = rs;
        this.contextInitializer = contextInitializer;
        this.rowConvertor = rowConvertor;
    }


    public Statement getStatement() {
        return stat;
    }

    public ResultSet getResultSet() {
        return rs;
    }


    @Override
    protected void finalize() throws Throwable {
        dispose();
    }


    @Override
    public void dispose() throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
            rs = null;
        }
        if (stat != null && !stat.isClosed()) {
            stat.close();
            stat = null;
        }
    }

    @Override
    public boolean hasRow() throws SQLException {
        if (rs == null) {
            return false;
        }
        if (rs.isClosed()) {
            return false;
        }
        if (!hasMore) {
            hasMore = rs.next();
        }
        return hasMore;
    }

    @Override
    public E nextRow() throws SQLException {
        if (context == null) {
            CTX ctx = contextInitializer.apply(rs);
            context = new AtomicReference<>(ctx);
        }
        E ret = rowConvertor.apply(context.get(), rs);
        hasMore = false;
        return ret;
    }


}
