package i2f.jdbc.cursor.impl;

import i2f.jdbc.JdbcResolver;
import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.data.QueryColumn;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/6/25 16:10
 */
public class JdbcCursorImpl<E> implements JdbcCursor<E>, Closeable, Iterable<E> {
    protected Statement stat;
    protected ResultSet rs;
    protected ResultSetMetaData metaData;
    protected List<QueryColumn> columns;
    protected Function<Map<String, Object>, E> rowConvertor;
    protected boolean hasMore = false;

    public JdbcCursorImpl(Statement stat, ResultSet rs,
                          ResultSetMetaData metaData,
                          List<QueryColumn> columns,
                          Function<Map<String, Object>, E> rowConvertor) {
        this.stat = stat;
        this.rs = rs;
        this.metaData = metaData;
        this.columns = columns;
        this.rowConvertor = rowConvertor;
    }

    public JdbcCursorImpl(Statement stat, ResultSet rs,
                          Class<E> beanClass,
                          Function<String, String> columnNameMapper) throws SQLException {
        this.stat = stat;
        this.rs = rs;
        this.metaData = rs.getMetaData();
        this.columns = JdbcResolver.parseResultSetColumns(metaData, columnNameMapper);
        this.rowConvertor = JdbcResolver.createDefaultRowConvertor(columns, beanClass);
    }

    public JdbcCursorImpl(Statement stat, ResultSet rs,
                          Function<String, String> columnNameMapper,
                          Function<Map<String, Object>, E> rowConvertor) throws SQLException {
        this.stat = stat;
        this.rs = rs;
        this.metaData = rs.getMetaData();
        this.columns = JdbcResolver.parseResultSetColumns(metaData, columnNameMapper);
        this.rowConvertor = rowConvertor;
    }

    public JdbcCursorImpl(Statement stat, ResultSet rs,
                          Class<E> beanClass,
                          Function<String, String> columnNameMapper,
                          BiFunction<List<QueryColumn>, Class<E>, Function<Map<String, Object>, E>> rowConvertorBuilder) throws SQLException {
        this.stat = stat;
        this.rs = rs;
        this.metaData = rs.getMetaData();
        this.columns = JdbcResolver.parseResultSetColumns(metaData, columnNameMapper);
        this.rowConvertor = rowConvertorBuilder.apply(columns, beanClass);
    }

    @Override
    public Statement getStatement() {
        return stat;
    }

    @Override
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
        Map<String, Object> map = JdbcResolver.convertResultSetRowAsMap(columns, rs);
        E ret = rowConvertor.apply(map);
        hasMore = false;
        return ret;
    }


}
