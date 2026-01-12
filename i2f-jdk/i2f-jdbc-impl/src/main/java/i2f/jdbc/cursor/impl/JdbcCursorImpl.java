package i2f.jdbc.cursor.impl;

import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.std.func.SQLBiFunction;
import i2f.jdbc.std.func.SQLFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2025/6/25 16:10
 */
public class JdbcCursorImpl<E, CTX> implements JdbcCursor<E> {
    protected Statement stat;
    protected ResultSet rs;
    protected SQLFunction<ResultSet, CTX> contextInitializer;
    protected SQLBiFunction<CTX, ResultSet, E> rowConvertor;
    protected final AtomicReference<AtomicReference<CTX>> contextHolder = new AtomicReference<>();
    protected final AtomicReference<AtomicReference<E>> rowHolder = new AtomicReference<>();
    protected ReentrantLock lock = new ReentrantLock();

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
        lock.lock();
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
                rs = null;
            }
            if (stat != null && !stat.isClosed()) {
                stat.close();
                stat = null;
            }
            rowHolder.set(null);
            contextHolder.set(null);
        } finally {
            lock.unlock();
        }
    }

    protected CTX getRequiredContext() throws SQLException {
        AtomicReference<CTX> ref = contextHolder.get();
        if (ref != null) {
            return ref.get();
        }
        CTX ctx = contextInitializer.apply(rs);
        contextHolder.set(new AtomicReference<>(ctx));
        return ctx;

    }

    protected void fetchNextRow() throws SQLException {
        AtomicReference<E> ref = rowHolder.get();
        if (ref == null) {
            if (rs.next()) {
                E ret = rowConvertor.apply(getRequiredContext(), rs);
                rowHolder.set(new AtomicReference<>(ret));
            }
        }
        if (rowHolder.get() == null) {
            dispose();
        }
    }

    @Override
    public boolean hasRow() throws SQLException {
        lock.lock();
        try {
            if (rs == null) {
                return false;
            }
            if (rs.isClosed()) {
                return false;
            }
            fetchNextRow();
            if (rowHolder.get() == null) {
                return false;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E nextRow() throws SQLException {
        lock.lock();
        try {
            AtomicReference<E> ref = rowHolder.get();
            if (ref != null) {
                rowHolder.set(null);
                return ref.get();
            }
            if (rs == null) {
                throw new SQLException("ResultSet has been consumed!");
            }
            if (rs.isClosed()) {
                throw new SQLException("ResultSet has been closed!");
            }
            fetchNextRow();
            ref = rowHolder.get();
            if (ref != null) {
                rowHolder.set(null);
                return ref.get();
            }
            throw new SQLException("Cursor internal error!");
        } finally {
            lock.unlock();
        }
    }

}
