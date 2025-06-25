package i2f.jdbc;

import i2f.bindsql.BindSql;
import i2f.bindsql.BindSqlWrappers;
import i2f.bindsql.data.PageBindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.cursor.impl.JdbcCursorImpl;
import i2f.jdbc.data.*;
import i2f.jdbc.handler.ResultSetObjectConvertHandler;
import i2f.jdbc.handler.StatementParameterSetHandler;
import i2f.jdbc.meta.JdbcMeta;
import i2f.jdbc.std.func.SQLBiFunction;
import i2f.jdbc.std.func.SQLFunction;
import i2f.match.regex.RegexUtil;
import i2f.page.ApiOffsetSize;
import i2f.page.Page;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:58
 * @desc
 */
public class JdbcResolver {

    public static final CopyOnWriteArrayList<StatementParameterSetHandler> STATEMENT_PARAMETER_HANDLERS = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<ResultSetObjectConvertHandler> RESULT_SET_OBJECT_CONVERT_HANDLERS = new CopyOnWriteArrayList<>();

    static {
        ServiceLoader<StatementParameterSetHandler> parameterSetHandlers = ServiceLoader.load(StatementParameterSetHandler.class);
        if (parameterSetHandlers != null) {
            for (StatementParameterSetHandler item : parameterSetHandlers) {
                STATEMENT_PARAMETER_HANDLERS.add(item);
            }
        }
        ServiceLoader<ResultSetObjectConvertHandler> objectConvertHandlers = ServiceLoader.load(ResultSetObjectConvertHandler.class);
        if (objectConvertHandlers != null) {
            for (ResultSetObjectConvertHandler item : objectConvertHandlers) {
                RESULT_SET_OBJECT_CONVERT_HANDLERS.add(item);
            }
        }
    }

    public static Connection getConnection(String driver,
                                           String url) throws SQLException {
        loadDriver(driver);
        return DriverManager.getConnection(url);
    }

    public static void loadDriver(String driver) throws SQLException {
        if (driver == null || driver.isEmpty()) {
            return;
        }
        Exception ex = null;
        try {
            Class.forName(driver);
        } catch (Exception e) {
            ex = e;
        }
        try {
            Thread.currentThread().getContextClassLoader().loadClass(driver);
        } catch (Exception e) {
            ex = e;
        }
        if (ex != null) {
            throw new SQLException(ex.getMessage(), ex);
        }
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           String username,
                                           String password) throws SQLException {
        loadDriver(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           Properties properties) throws SQLException {
        loadDriver(driver);
        return DriverManager.getConnection(url, properties);
    }

    public static Connection getConnection(JdbcMeta meta) throws SQLException {
        loadDriver(meta.getDriver());
        if (meta.getProperties() != null && !meta.getProperties().isEmpty()) {
            return DriverManager.getConnection(meta.getUrl(), meta.getProperties());
        }
        if (meta.getUsername() != null || meta.getPassword() != null) {
            return DriverManager.getConnection(meta.getUrl(), meta.getUsername(), meta.getPassword());
        }
        return DriverManager.getConnection(meta.getUrl());
    }

    public static Connection begin(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        return conn;
    }

    public static Connection auto(Connection conn) throws SQLException {
        conn.setAutoCommit(true);
        return conn;
    }

    public static Connection commit(Connection conn) throws SQLException {
        conn.commit();
        return conn;
    }

    public static Connection rollback(Connection conn) throws SQLException {
        conn.rollback();
        return conn;
    }

    public static <E, R> R transaction(Connection conn, SQLBiFunction<Connection, E, R> operation, E arg) throws SQLException {
        boolean bak = conn.getAutoCommit();
        begin(conn);
        try {
            R ret = operation.apply(conn, arg);
            commit(conn);
            return ret;
        } catch (Exception e) {
            rollback(conn);
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException(e.getMessage(), e);
            }
        } finally {
            conn.setAutoCommit(bak);
        }
    }

    public static BindSql.Type detectType(String sql) {
        if (sql == null) {
            return BindSql.Type.UNSET;
        }
        sql = sql.trim().toLowerCase();
        if (sql.startsWith("select")) {
            return BindSql.Type.QUERY;
        }
        if (sql.startsWith("insert")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("update")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("delete")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("create")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("drop")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("alter")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("comment")) {
            return BindSql.Type.UPDATE;
        }
        if (sql.startsWith("{")) {
            return BindSql.Type.CALL;
        }
        if (sql.startsWith("call")) {
            return BindSql.Type.CALL;
        }
        return BindSql.Type.UNSET;
    }

    public static List<Object> transaction(Connection conn, List<BindSql> sqls) throws SQLException {
        return transaction(conn, (connection, sqlList) -> {
            List<Object> ret = new ArrayList<>();
            for (BindSql sql : sqlList) {
                BindSql.Type type = sql.getType();
                if (type == null || type == BindSql.Type.UNSET) {
                    type = detectType(sql.getSql());
                }
                if (type == BindSql.Type.UPDATE) {
                    int val = update(connection, sql.getSql(), sql.getArgs());
                    ret.add(val);
                } else if (type == BindSql.Type.CALL) {
                    Map<String, Object> val = callNaming(connection, sql.getSql(), sql.getArgs());
                    ret.add(val);
                } else {
                    QueryResult val = query(connection, sql.getSql(), sql.getArgs());
                    ret.add(val);
                }
            }
            return ret;
        }, sqls);
    }

    public static QueryResult query(Connection conn, BindSql sql) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs());
    }

    public static QueryResult query(Connection conn, BindSql sql, int maxCount) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs(), (rs) -> parseResultSet(rs, maxCount));
    }

    public static QueryResult query(Connection conn, BindSql sql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs(), (rs) -> parseResultSet(rs, maxCount, columnNameMapper));
    }

    public static <R> R query(Connection conn, BindSql sql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs(), resultSetHandler);
    }

    public static QueryResult query(Connection conn, String sql, List<Object> args) throws SQLException {
        return query(conn, sql, args, JdbcResolver::parseResultSet);
    }

    public static QueryResult query(Connection conn, String sql, List<Object> args, int maxCount) throws SQLException {
        return query(conn, sql, args, (rs) -> parseResultSet(rs, maxCount));
    }

    public static QueryResult query(Connection conn, String sql, List<Object> args, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql, args, (rs) -> parseResultSet(rs, maxCount, columnNameMapper));
    }

    public static <R> R query(Connection conn, String sql, List<Object> args, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        try (PreparedStatement stat = preparedStatement(conn, sql, args)) {
            try (ResultSet rs = stat.executeQuery()) {
                try {
                    R ret = resultSetHandler.apply(rs);
                    return ret;
                } finally {
                    if (!rs.isClosed()) {
                        rs.close();
                    }
                    stat.close();
                }
            }
        }
    }

    public static PreparedStatement buildCursorStatement(Connection conn, String sql) throws SQLException {
        return buildCursorStatement(conn, sql, -1);
    }

    public static PreparedStatement buildCursorStatement(Connection conn, String sql, int fetchSize) throws SQLException {
        if (fetchSize <= 0) {
            fetchSize = 500;
        }
        PreparedStatement stat = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stat.setFetchDirection(ResultSet.FETCH_FORWARD);
        stat.setFetchSize(fetchSize);
        return stat;
    }


    public static final SQLBiFunction<Connection, String, PreparedStatement> DEFAULT_CURSOR_STATEMENT_BUILDER = JdbcResolver::buildCursorStatement;

    public static final SQLFunction<ResultSet, List<QueryColumn>> DEFAULT_CURSOR_CONTEXT_INITIALIZER = JdbcResolver::parseResultSetColumns;

    public static final SQLBiFunction<List<QueryColumn>, ResultSet, Map<String, Object>> DEFAULT_CURSOR_ROW_CONVERTOR = JdbcResolver::convertResultSetRowAsMap;


    public static SQLFunction<ResultSet, List<QueryColumn>> createCursorContextInitializer(Function<String, String> columnNameMapper) {
        if (columnNameMapper == null) {
            return JdbcResolver::parseResultSetColumns;
        }
        return (rs) -> parseResultSetColumns(rs, columnNameMapper);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, BindSql bql, Class<E> beanClass) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs(), beanClass);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, BindSql bql, Class<E> beanClass,
                                           SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs(), beanClass, statementBuilder);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, String sql, List<Object> args, Class<E> beanClass) throws SQLException {
        return cursor(conn, sql, args, beanClass, null, DEFAULT_CURSOR_STATEMENT_BUILDER);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, String sql, List<Object> args, Class<E> beanClass,
                                           SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor(conn, sql, args, beanClass, null, statementBuilder);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, BindSql bql, Class<E> beanClass, Function<String, String> columnNameMapper) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs(), beanClass, columnNameMapper);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, BindSql bql, Class<E> beanClass,
                                           Function<String, String> columnNameMapper,
                                           SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs(), beanClass, columnNameMapper, statementBuilder);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, String sql, List<Object> args, Class<E> beanClass, Function<String, String> columnNameMapper) throws SQLException {
        return cursor(conn, sql, args, beanClass, columnNameMapper);
    }

    public static <E> JdbcCursor<E> cursor(Connection conn, String sql, List<Object> args, Class<E> beanClass,
                                           Function<String, String> columnNameMapper,
                                           SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor0(conn, sql, args,
                createResultSetRowBeanConvertor(beanClass),
                createCursorContextInitializer(columnNameMapper),
                statementBuilder);
    }

    public static JdbcCursor<Map<String, Object>> cursor(Connection conn, BindSql bql) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs());
    }

    public static JdbcCursor<Map<String, Object>> cursor(Connection conn, BindSql bql,
                                                         SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor(conn, bql.getSql(), bql.getArgs(), statementBuilder);
    }

    public static JdbcCursor<Map<String, Object>> cursor(Connection conn, String sql, List<Object> args) throws SQLException {
        return cursor(conn, sql, args, DEFAULT_CURSOR_STATEMENT_BUILDER);
    }

    public static JdbcCursor<Map<String, Object>> cursor(Connection conn, String sql, List<Object> args,
                                                         SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        return cursor0(conn, sql, args,
                DEFAULT_CURSOR_ROW_CONVERTOR,
                DEFAULT_CURSOR_CONTEXT_INITIALIZER,
                statementBuilder);
    }

    public static <E, CTX> JdbcCursorImpl<E, CTX> cursor0(Connection conn, String sql, List<Object> args,
                                                          SQLBiFunction<CTX, ResultSet, E> rowConvertor,
                                                          SQLFunction<ResultSet, CTX> contextInitializer,
                                                          SQLBiFunction<Connection, String, PreparedStatement> statementBuilder) throws SQLException {
        if (statementBuilder == null) {
            statementBuilder = DEFAULT_CURSOR_STATEMENT_BUILDER;
        }
        PreparedStatement stat = statementBuilder.apply(conn, sql);
        fillStatementArgs(stat, args);
        ResultSet rs = stat.executeQuery();
        return new JdbcCursorImpl<>(stat, rs, rowConvertor, contextInitializer);
    }

    public static List<Map<String, Object>> list(Connection conn, BindSql sql) throws SQLException {
        return query(conn, sql, JdbcResolver::parseResultSet).getRows();
    }

    public static List<Map<String, Object>> list(Connection conn, BindSql sql, int maxCount) throws SQLException {
        return query(conn, sql, rs -> parseResultSet(rs, maxCount)).getRows();
    }

    public static List<Map<String, Object>> list(Connection conn, BindSql sql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql, rs -> parseResultSet(rs, maxCount, columnNameMapper)).getRows();
    }

    public static List<Map<String, Object>> list(Connection conn, String sql, List<Object> args) throws SQLException {
        return query(conn, sql, args, JdbcResolver::parseResultSet).getRows();
    }

    public static List<Map<String, Object>> list(Connection conn, String sql, List<Object> args, int maxCount) throws SQLException {
        return query(conn, sql, args, rs -> parseResultSet(rs, maxCount)).getRows();
    }

    public static List<Map<String, Object>> list(Connection conn, String sql, List<Object> args, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql, args, rs -> parseResultSet(rs, maxCount, columnNameMapper)).getRows();
    }


    public static Page<Map<String, Object>> page(Connection conn, BindSql sql, ApiOffsetSize page) throws SQLException {
        return page(conn, sql.getSql(), sql.getArgs(), page, null);
    }

    public static Page<Map<String, Object>> page(Connection conn, BindSql sql, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
        return page(conn, sql.getSql(), sql.getArgs(), page, columnNameMapper);
    }

    public static Page<Map<String, Object>> page(Connection conn, String sql, List<Object> args, ApiOffsetSize page) throws SQLException {
        return page(conn, sql, args, page, null);
    }

    public static Page<Map<String, Object>> page(Connection conn, String sql, List<Object> args, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, new BindSql(sql, args), page);
        return page(conn, pageBindSql, columnNameMapper);
    }

    public static Page<Map<String, Object>> page(Connection conn, PageBindSql pageBindSql) throws SQLException {
        return page(conn, pageBindSql, (Function<String, String>) null);
    }

    public static Page<Map<String, Object>> page(Connection conn, PageBindSql pageBindSql, Function<String, String> columnNameMapper) throws SQLException {
        Long total = get(conn, pageBindSql.getCountSql(), Long.class);
        List<Map<String, Object>> rows = new ArrayList<>();
        if (total > 0) {
            rows = query(conn, pageBindSql.getPageSql(), rs -> parseResultSet(rs, -1, columnNameMapper)).getRows();
        }
        return Page.of(pageBindSql.getPage(), total, rows);
    }

    public static Map<String, Object> find(Connection conn, BindSql sql) throws SQLException {
        List<Map<String, Object>> rows = query(conn, sql, (rs) -> parseResultSet(rs, 2)).getRows();
        if (!rows.isEmpty()) {
            if (rows.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return rows.get(0);
        }
        return null;
    }

    public static Map<String, Object> find(Connection conn, BindSql sql, Function<String, String> columnNameMapper) throws SQLException {
        List<Map<String, Object>> rows = query(conn, sql, (rs) -> parseResultSet(rs, 2, columnNameMapper)).getRows();
        if (!rows.isEmpty()) {
            if (rows.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return rows.get(0);
        }
        return null;
    }

    public static Map<String, Object> find(Connection conn, String sql, List<Object> args) throws SQLException {
        List<Map<String, Object>> rows = query(conn, sql, args, (rs) -> parseResultSet(rs, 2)).getRows();
        if (!rows.isEmpty()) {
            if (rows.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return rows.get(0);
        }
        return null;
    }

    public static Map<String, Object> find(Connection conn, String sql, List<Object> args, Function<String, String> columnNameMapper) throws SQLException {
        List<Map<String, Object>> rows = query(conn, sql, args, (rs) -> parseResultSet(rs, 2, columnNameMapper)).getRows();
        if (!rows.isEmpty()) {
            if (rows.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return rows.get(0);
        }
        return null;
    }

    public static <T> List<T> list(Connection conn, BindSql sql, Class<T> clazz) throws SQLException {
        return query(conn, sql, (rs) -> parseResultSetAsBeanList(rs, clazz));
    }

    public static <T> List<T> list(Connection conn, BindSql sql, Class<T> clazz, int maxCount) throws SQLException {
        return query(conn, sql, (rs) -> parseResultSetAsBeanList(rs, clazz, maxCount, null));
    }

    public static <T> List<T> list(Connection conn, BindSql sql, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql, (rs) -> parseResultSetAsBeanList(rs, clazz, maxCount, columnNameMapper));
    }

    public static <T> List<T> list(Connection conn, String sql, List<Object> args, Class<T> clazz) throws SQLException {
        return query(conn, sql, args, (rs) -> parseResultSetAsBeanList(rs, clazz));
    }

    public static <T> List<T> list(Connection conn, String sql, List<Object> args, Class<T> clazz, int maxCount) throws SQLException {
        return query(conn, sql, args, (rs) -> parseResultSetAsBeanList(rs, clazz, maxCount, null));
    }

    public static <T> List<T> list(Connection conn, String sql, List<Object> args, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return query(conn, sql, args, (rs) -> parseResultSetAsBeanList(rs, clazz, maxCount, columnNameMapper));
    }

    public static <T> Page<T> page(Connection conn, BindSql sql, Class<T> clazz, ApiOffsetSize page) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, sql, page);
        return page(conn, pageBindSql, clazz, null);
    }

    public static <T> Page<T> page(Connection conn, BindSql sql, Class<T> clazz, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, sql, page);
        return page(conn, pageBindSql, clazz, columnNameMapper);
    }

    public static <T> Page<T> page(Connection conn, String sql, List<Object> args, Class<T> clazz, ApiOffsetSize page) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, new BindSql(sql, args), page);
        return page(conn, pageBindSql, clazz, null);
    }

    public static <T> Page<T> page(Connection conn, String sql, List<Object> args, Class<T> clazz, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, new BindSql(sql, args), page);
        return page(conn, pageBindSql, clazz, columnNameMapper);
    }

    public static <T> Page<T> page(Connection conn, PageBindSql pageBindSql, Class<T> clazz) throws SQLException {
        return page(conn, pageBindSql, clazz, null);
    }

    public static <T> Page<T> page(Connection conn, PageBindSql pageBindSql, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        Long total = get(conn, pageBindSql.getCountSql(), Long.class);
        List<T> rows = new ArrayList<>();
        if (total > 0) {
            rows = query(conn, pageBindSql.getPageSql(), (rs) -> parseResultSetAsBeanList(rs, clazz, -1, columnNameMapper));
        }
        return Page.of(pageBindSql.getPage(), total, rows);
    }


    public static <T> T find(Connection conn, BindSql sql, Class<T> clazz) throws SQLException {
        List<T> list = query(conn, sql, (rs) -> parseResultSetAsBeanList(rs, clazz, 2));
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return list.get(0);
        }
        return null;
    }

    public static <T> T find(Connection conn, BindSql sql, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        List<T> list = query(conn, sql, (rs) -> parseResultSetAsBeanList(rs, clazz, 2, columnNameMapper));
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return list.get(0);
        }
        return null;
    }

    public static <T> T find(Connection conn, String sql, List<Object> args, Class<T> clazz) throws SQLException {
        List<T> list = query(conn, sql, args, (rs) -> parseResultSetAsBeanList(rs, clazz, 2));
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return list.get(0);
        }
        return null;
    }

    public static <T> T find(Connection conn, String sql, List<Object> args, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        List<T> list = query(conn, sql, args, (rs) -> parseResultSetAsBeanList(rs, clazz, 2, columnNameMapper));
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            return list.get(0);
        }
        return null;
    }

    public static <T> T get(Connection conn, BindSql sql, Class<T> clazz) throws SQLException {
        QueryResult qr = query(conn, sql, 2);
        if (!qr.getRows().isEmpty()) {
            if (qr.getColumns().size() > 1) {
                throw new SQLDataException("expect one col return but more than one has got.");
            }
            if (qr.getRows().size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            QueryColumn col = qr.getColumns().get(0);
            Object val = qr.getRows().get(0).get(col.getName());
            return (T) ObjectConvertor.tryConvertAsType(val, clazz);
        }
        return null;
    }

    public static <T> T get(Connection conn, String sql, List<Object> args, Class<T> clazz) throws SQLException {
        QueryResult qr = query(conn, sql, args, 2);
        if (!qr.getRows().isEmpty()) {
            if (qr.getColumns().size() > 1) {
                throw new SQLDataException("expect one col return but more than one has got.");
            }
            if (qr.getRows().size() > 1) {
                throw new SQLDataException("expect one row return but more than one has got.");
            }
            QueryColumn col = qr.getColumns().get(0);
            Object val = qr.getRows().get(0).get(col.getName());
            return (T) ObjectConvertor.tryConvertAsType(val, clazz);
        }
        return null;
    }

    public static int update(Connection conn, BindSql sql) throws SQLException {
        return update(conn, sql.getSql(), sql.getArgs());
    }

    public static int update(Connection conn, String sql, List<?> args) throws SQLException {
        try (PreparedStatement stat = preparedStatement(conn, sql, args)) {
            int ret = stat.executeUpdate();
            stat.close();
            return ret;
        }
    }


    public static <T> void batch(Connection conn, BindSql bql, Iterable<T> iterator) throws SQLException {
        batch(conn, bql, iterator.iterator(), null, -1);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterable<T> iterator, int batchSize) throws SQLException {
        batch(conn, bql, iterator.iterator(), null, batchSize);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterable<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, bql, iterator.iterator(), filter, -1);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterable<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch(conn, bql, iterator.iterator(), filter, batchSize);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterator<T> iterator) throws SQLException {
        batch(conn, bql, iterator, null, -1);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterator<T> iterator, int batchSize) throws SQLException {
        batch(conn, bql, iterator, null, batchSize);
    }

    public static <T> void batch(Connection conn, BindSql bql, Iterator<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, bql, iterator, filter, -1);
    }

    /**
     * bql的绑定args为Function类型则认定为Function，否则按照expression处理
     *
     * @param conn
     * @param bql
     * @param iterator
     * @param filter
     * @param batchSize
     * @param <T>
     * @throws SQLException
     */
    public static <T> void batch(Connection conn, BindSql bql, Iterator<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        List<Function<T, ?>> getters = new ArrayList<>();
        for (Object item : bql.getArgs()) {
            if (item instanceof Function) {
                Function getter = (Function) item;
                getters.add(getter);
            } else {
                String expression = String.valueOf(item);
                getters.add((elem) -> Visitor.visit(expression, elem).get());
            }
        }
        batch0(conn, bql.getSql(), getters, iterator, filter, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, Iterable<T> iterator) throws SQLException {
        batch(conn, sql, iterator.iterator(), null, -1);
    }

    public static <T> void batch(Connection conn, String sql, Iterable<T> iterator, int batchSize) throws SQLException {
        batch(conn, sql, iterator.iterator(), null, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, Iterable<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, sql, iterator.iterator(), filter, -1);
    }

    public static <T> void batch(Connection conn, String sql, Iterable<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch(conn, sql, iterator.iterator(), filter, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, Iterator<T> iterator) throws SQLException {
        batch(conn, sql, iterator, null, -1);
    }

    public static <T> void batch(Connection conn, String sql, Iterator<T> iterator, int batchSize) throws SQLException {
        batch(conn, sql, iterator, null, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, Iterator<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, sql, iterator, filter, -1);
    }

    /**
     * sql 使用 ${} 占位符来取iterator中的元素的值
     *
     * @param conn
     * @param sql
     * @param iterator
     * @param filter
     * @param batchSize
     * @param <T>
     * @throws SQLException
     */
    public static <T> void batch(Connection conn, String sql, Iterator<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        List<String> expression = new ArrayList<>();
        RegexUtil.regexFindAndReplace(sql, "\\$\\{[^}]+\\}", (result) -> {
            expression.add(result);
            return "?";
        });
        batch(conn, sql, expression, iterator, filter, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterable<T> iterator) throws SQLException {
        batch(conn, sql, expressions, iterator.iterator(), null, -1);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterable<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, sql, expressions, iterator.iterator(), filter, -1);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterable<T> iterator, int batchSize) throws SQLException {
        batch(conn, sql, expressions, iterator.iterator(), null, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterable<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch(conn, sql, expressions, iterator.iterator(), filter, batchSize);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterator<T> iterator) throws SQLException {
        batch(conn, sql, expressions, iterator, null, -1);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterator<T> iterator, Predicate<T> filter) throws SQLException {
        batch(conn, sql, expressions, iterator, filter, -1);
    }

    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterator<T> iterator, int batchSize) throws SQLException {
        batch(conn, sql, expressions, iterator, null, batchSize);
    }


    public static <T> void batch(Connection conn, String sql, List<String> expressions, Iterator<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        List<Function<T, ?>> getters = new ArrayList<>();
        for (String expression : expressions) {
            getters.add((elem) -> Visitor.visit(expression, elem).get());
        }
        batch0(conn, sql, getters, iterator, filter, batchSize);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterable<T> iterator) throws SQLException {
        batch0(conn, sql, expressions, iterator.iterator(), null, -1);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterable<T> iterator, Predicate<T> filter) throws SQLException {
        batch0(conn, sql, expressions, iterator.iterator(), filter, -1);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterable<T> iterator, int batchSize) throws SQLException {
        batch0(conn, sql, expressions, iterator.iterator(), null, batchSize);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterable<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch0(conn, sql, expressions, iterator.iterator(), filter, batchSize);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterator<T> iterator) throws SQLException {
        batch0(conn, sql, expressions, iterator, null, -1);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterator<T> iterator, Predicate<T> filter) throws SQLException {
        batch0(conn, sql, expressions, iterator, filter, -1);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterator<T> iterator, int batchSize) throws SQLException {
        batch0(conn, sql, expressions, iterator, null, batchSize);
    }

    public static <T> void batch0(Connection conn, String sql, List<Function<T, ?>> expressions, Iterator<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        if (iterator == null) {
            return;
        }
        PreparedStatement stat = null;
        try {
            if (batchSize < 0) {
                while (iterator.hasNext()) {
                    T elem = iterator.next();
                    if (filter != null && !filter.test(elem)) {
                        continue;
                    }
                    if (stat == null) {
                        stat = conn.prepareStatement(sql);
                    }
                    int i = 1;
                    for (Function<T, ?> expression : expressions) {
                        Object val = expression.apply(elem);
                        setStatementObject(stat, i++, val);
                    }
                    stat.addBatch();
                }
                stat.executeBatch();
            } else {
                List<T> once = new ArrayList<>(batchSize);
                int count = 0;
                while (iterator.hasNext()) {
                    T elem = iterator.next();
                    if (filter != null && !filter.test(elem)) {
                        continue;
                    }

                    once.add(elem);
                    count++;

                    if (stat == null) {
                        stat = conn.prepareStatement(sql);
                    }
                    int i = 1;
                    for (Function<T, ?> expression : expressions) {
                        Object val = expression.apply(elem);
                        setStatementObject(stat, i++, val);
                    }
                    stat.addBatch();
                    if (count == batchSize) {
                        stat.executeBatch();
                        count = 0;
                        once.clear();
                        stat.close();
                        stat = null;
                    }
                }
                if (count > 0) {
                    stat.executeBatch();
                    count = 0;
                    once.clear();
                    stat.close();
                    stat = null;
                }
            }
        } finally {
            if (stat != null) {
                stat.close();
            }
        }

    }

    public static void batchByListableValues(Connection conn, String sql, Iterator<List<Object>> iterator, int batchSize) throws SQLException {
        if (iterator == null) {
            return;
        }
        PreparedStatement stat = null;
        try {
            if (batchSize < 0) {
                while (iterator.hasNext()) {
                    List<Object> elem = iterator.next();
                    if (elem == null) {
                        continue;
                    }
                    if (stat == null) {
                        stat = conn.prepareStatement(sql);
                    }
                    fillStatementArgs(stat, elem);
                    stat.addBatch();
                }
                stat.executeBatch();
            } else {
                List<List<Object>> once = new ArrayList<>(batchSize);
                int count = 0;
                while (iterator.hasNext()) {
                    List<Object> elem = iterator.next();
                    if (elem == null) {
                        continue;
                    }

                    once.add(elem);
                    count++;

                    if (stat == null) {
                        stat = conn.prepareStatement(sql);
                    }
                    fillStatementArgs(stat, elem);
                    stat.addBatch();
                    if (count == batchSize) {
                        stat.executeBatch();
                        count = 0;
                        once.clear();
                        stat.close();
                        stat = null;
                    }
                }
                if (count > 0) {
                    stat.executeBatch();
                    count = 0;
                    once.clear();
                    stat.close();
                    stat = null;
                }
            }
        } finally {
            if (stat != null) {
                stat.close();
            }
        }

    }

    public static boolean call(Connection conn, String sql) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, null, null);
        return (Boolean) map.get(-1);
    }

    public static boolean call(Connection conn, BindSql sql) throws SQLException {
        return call(conn, sql.getSql(), sql.getArgs());
    }

    public static boolean call(Connection conn, String sql, List<?> args) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, args, null);
        return (Boolean) map.get(-1);
    }

    public static Map<Integer, Object> call(Connection conn, BindSql sql, Map<Integer, SQLType> outParams) throws SQLException {
        return call(conn, sql.getSql(), sql.getArgs(), outParams);
    }

    /**
     * 返回值为outParams中指定的出参
     * 特别的，key=-1表示执行的结果boolean值
     *
     * @param conn
     * @param sql
     * @param args
     * @param outParams
     * @return
     * @throws SQLException
     */
    public static Map<Integer, Object> call(Connection conn, String sql, List<?> args, Map<Integer, SQLType> outParams) throws SQLException {
        try (CallableStatement stat = callStatement(conn, sql, args, outParams)) {
            int rsIdx = -1;
            boolean ok = stat.execute();
            Map<Integer, Object> ret = new LinkedHashMap<>();
            ret.put(rsIdx, ok);
            if (outParams != null) {
                for (Map.Entry<Integer, SQLType> entry : outParams.entrySet()) {
                    Object val = stat.getObject(entry.getKey() + 1);
                    ret.put(entry.getKey(), val);
                }
            }
            while (stat.getMoreResults()) {
                rsIdx--;
                try (ResultSet rs = stat.getResultSet()) {
                    QueryResult result = parseResultSet(rs);
                    ret.put(rsIdx, result);
                }
            }
            stat.close();
            return ret;
        }
    }

    /**
     * sql= {call producer_name(?,?,?)}
     *
     * @param sql
     * @return
     */
    public static String callSql(String sql) {
        sql = sql.trim();
        if (!sql.startsWith("{")) {
            if (!sql.toLowerCase().startsWith("call")) {
                sql = "call " + sql;
            }
            sql = "{" + sql;
        }
        if (!sql.endsWith("}")) {
            sql = sql + "}";
        }
        return sql;
    }

    public static CallableStatement callStatement(Connection conn, String sql) throws SQLException {
        return callStatement(conn, sql, null, null);
    }

    public static CallableStatement callStatement(Connection conn, BindSql sql) throws SQLException {
        return callStatement(conn, sql.getSql(), sql.getArgs());
    }

    public static CallableStatement callStatement(Connection conn, String sql, List<?> args) throws SQLException {
        return callStatement(conn, sql, args, null);
    }


    public static CallableStatement callStatement(Connection conn, BindSql sql, Map<Integer, SQLType> outParams) throws SQLException {
        return callStatement(conn, sql.getSql(), sql.getArgs(), outParams);
    }

    /**
     * args 每个参数位置都是需要的，包括出参位置，出参可以设为任意值补位，因为会被忽略
     * outParams 指定哪些是出参
     * 举例：
     * 调用存过：sp_test(in,in,out,in,out)
     * 则对应的入参为：
     * sql= {call sp_test(?,?,?,?,?)}
     * args= [1,1,null,1,null]
     * outParams= {
     * 2: JDBCType.NUMBER,
     * 4: JDBCType.VARCHAR
     * }
     *
     * @param conn
     * @param sql
     * @param args
     * @param outParams
     * @return
     * @throws SQLException
     */
    public static CallableStatement callStatement(Connection conn, String sql, List<?> args, Map<Integer, SQLType> outParams) throws SQLException {
        Set<Integer> outs = new HashSet<>();
        if (outParams != null) {
            outs.addAll(outParams.keySet());
        }
        CallableStatement stat = conn.prepareCall(sql);
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                if (outs.contains(i - 1)) {
                    stat.registerOutParameter(i, outParams.get(i - 1));
                } else {
                    setStatementObject(stat, i, arg);
                }
                i++;
            }
        }
        return stat;
    }

    public static Map<String, Object> callNaming(Connection conn, BindSql sql) throws SQLException {
        return callNaming(conn, sql.getSql(), sql.getArgs());
    }

    /**
     * 返回值为出参中指定的出参
     * 特别的，key=-1表示执行的结果boolean值
     *
     * @param conn
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> callNaming(Connection conn, String sql, List<?> args) throws SQLException {
        Map<Integer, NamingOutputParameter> namingMap = new LinkedHashMap<>();

        try (CallableStatement stat = namingCallableStatement(conn, sql, args, namingMap)) {
            int rsIdx = -1;
            boolean ok = stat.execute();
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put(String.valueOf(rsIdx), ok);
            for (Map.Entry<Integer, NamingOutputParameter> entry : namingMap.entrySet()) {
                Object val = stat.getObject(entry.getKey());
                ret.put(entry.getValue().getName(), val);
            }
            while (stat.getMoreResults()) {
                rsIdx--;
                try (ResultSet rs = stat.getResultSet()) {
                    QueryResult result = parseResultSet(rs);
                    ret.put(String.valueOf(rsIdx), result);
                }
            }
            stat.close();

            return ret;
        }
    }

    public static CallableStatement namingCallableStatement(Connection conn, BindSql sql, Map<Integer, NamingOutputParameter> context) throws SQLException {
        return namingCallableStatement(conn, sql.getSql(), sql.getArgs(), context);
    }

    public static CallableStatement namingCallableStatement(Connection conn, String sql, List<?> args, Map<Integer, NamingOutputParameter> context) throws SQLException {

        CallableStatement stat = conn.prepareCall(sql);
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                if (arg instanceof NamingOutputParameter) {
                    NamingOutputParameter parameter = (NamingOutputParameter) arg;
                    if (parameter.isInput()) {
                        setStatementObject(stat, i, parameter.getValue());
                    }
                    if (parameter.getType() != null) {
                        stat.registerOutParameter(i, parameter.getType());
                    } else {
                        stat.registerOutParameter(i, parameter.getSqlType());
                    }
                    context.put(i, parameter);
                } else {
                    setStatementObject(stat, i, arg);
                }

                i++;
            }
        }
        return stat;
    }

    public static PreparedStatement preparedStatement(Connection conn, BindSql sql) throws SQLException {
        return preparedStatement(conn, sql.getSql(), sql.getArgs());
    }

    public static PreparedStatement preparedStatement(Connection conn, String sql, List<?> args) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(sql);
        fillStatementArgs(stat, args);
        return stat;
    }

    public static PreparedStatement fillStatementArgs(PreparedStatement stat, Collection<?> args) throws SQLException {
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                setStatementObject(stat, i, arg);
                i++;
            }
        }
        return stat;
    }

    public static void setStatementObject(PreparedStatement stat, int index, Object obj) throws SQLException {
        Class<?> clazz = (obj == null ? null : obj.getClass());
        SQLType jdbcType = null;
        if (obj instanceof TypedArgument) {
            TypedArgument typedArgument = (TypedArgument) obj;
            obj = typedArgument.getValue();

            ArgumentTypeHandler handler = typedArgument.getHandler();
            if (handler != null) {
                handler.handle(stat, index, obj);
                return;
            }

            Class<?> javaType = typedArgument.getJavaType();
            if (javaType != null) {
                clazz = javaType;
                obj = ObjectConvertor.tryConvertAsType(obj, javaType);
            }

            jdbcType = typedArgument.getJdbcType();

        }
        for (StatementParameterSetHandler handler : STATEMENT_PARAMETER_HANDLERS) {
            if (handler.set(stat, index, obj, jdbcType, clazz)) {
                return;
            }
        }
        if (jdbcType != null) {
            boolean handled = setStatementObjectWithJdbcType(stat, index, obj, jdbcType);
            if (handled) {
                return;
            }
            return;
        }
        if (obj == null) {
            setStatementNullWithType(stat, index, clazz, jdbcType);
            return;
        }
        if (clazz == null) {
            clazz = obj.getClass();
        }
        if (obj instanceof Integer) {
            stat.setInt(index, (Integer) obj);
            return;
        }
        if (obj instanceof Double) {
            stat.setDouble(index, (Double) obj);
            return;
        }
        if (obj instanceof Float) {
            stat.setFloat(index, (Float) obj);
            return;
        }
        if (obj instanceof Long) {
            stat.setLong(index, (Long) obj);
            return;
        }
        if (obj instanceof Short) {
            stat.setShort(index, (Short) obj);
            return;
        }
        if (obj instanceof Byte) {
            stat.setByte(index, (Byte) obj);
            return;
        }
        if (obj instanceof java.sql.Date) {
            stat.setDate(index, (java.sql.Date) obj);
            return;
        }
        if (obj instanceof Timestamp) {
            stat.setTimestamp(index, (Timestamp) obj);
            return;
        }
        if (obj instanceof Time) {
            stat.setTime(index, (Time) obj);
            return;
        }
        if (obj instanceof java.util.Date) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof LocalDateTime) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof LocalDate) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof BigDecimal) {
            stat.setBigDecimal(index, (BigDecimal) obj);
            return;
        }
        if (obj instanceof BigInteger) {
            stat.setObject(index, obj, JDBCType.BIGINT);
            return;
        }
        if (obj instanceof Character) {
            stat.setObject(index, obj, JDBCType.VARCHAR);
            return;
        }
        if (obj instanceof String) {
            stat.setString(index, (String) obj);
            return;
        }
        if (obj instanceof CharSequence) {
            stat.setString(index, String.valueOf(obj));
            return;
        }
        if (obj instanceof Boolean) {
            stat.setBoolean(index, (Boolean) obj);
            return;
        }
        if (obj instanceof Calendar) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof LocalTime) {
            stat.setTime(index, (Time) ObjectConvertor.tryConvertAsType(obj, Time.class));
            return;
        }
        if (obj instanceof Instant) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof Clock) {
            stat.setDate(index, (java.sql.Date) ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class));
            return;
        }
        if (obj instanceof byte[]) {
            stat.setBytes(index, (byte[]) obj);
            return;
        }
        if (obj instanceof AtomicInteger) {
            stat.setInt(index, ((AtomicInteger) obj).get());
            return;
        }
        if (obj instanceof AtomicLong) {
            stat.setLong(index, ((AtomicLong) obj).get());
            return;
        }
        if (obj instanceof Number) {
            stat.setObject(index, obj, JDBCType.NUMERIC);
            return;
        }
        if (obj instanceof Appendable) {
            stat.setString(index, ((Appendable) obj).toString());
            return;
        }
        if (obj instanceof AtomicBoolean) {
            stat.setBoolean(index, ((AtomicBoolean) obj).get());
            return;
        }
        if (obj instanceof AtomicReference) {
            setStatementObject(stat, index, ((AtomicReference<?>) obj).get());
            return;
        }
        if (obj instanceof ThreadLocal) {
            setStatementObject(stat, index, ((ThreadLocal<?>) obj).get());
            return;
        }
        if (obj instanceof char[]) {
            char[] arr = (char[]) obj;
            stat.setCharacterStream(index, new StringReader(new String(arr)), arr.length);
            return;
        }
        if (obj instanceof Reader) {
            stat.setCharacterStream(index, (Reader) obj);
            return;
        }
        if (obj instanceof InputStream) {
            if (obj instanceof ByteArrayInputStream) {
                ByteArrayInputStream bis = (ByteArrayInputStream) obj;
                stat.setBlob(index, bis, bis.available());
            } else {
                stat.setBlob(index, (InputStream) obj);
            }
            return;
        }
        if (obj instanceof NClob) {
            stat.setNClob(index, (NClob) obj);
            return;
        }
        if (obj instanceof Clob) {
            stat.setClob(index, (Clob) obj);
            return;
        }
        if (obj instanceof Blob) {
            stat.setBlob(index, (Blob) obj);
            return;
        }


        stat.setObject(index, obj);
    }

    public static boolean setStatementObjectWithJdbcType(PreparedStatement stat, int index, Object obj, SQLType jdbcType) throws SQLException {
        if (jdbcType == null) {
            return false;
        }
        if (!(jdbcType instanceof JDBCType)) {
            return false;
        }
        JDBCType type = (JDBCType) jdbcType;
        if (type == JDBCType.TINYINT) {
            if (obj == null) {
                stat.setNull(index, Types.TINYINT);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Integer.class);
                if (val instanceof Integer) {
                    stat.setInt(index, (Integer) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.SMALLINT) {
            if (obj == null) {
                stat.setNull(index, Types.SMALLINT);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Integer.class);
                if (val instanceof Integer) {
                    stat.setInt(index, (Integer) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.INTEGER) {
            if (obj == null) {
                stat.setNull(index, Types.INTEGER);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Integer.class);
                if (val instanceof Integer) {
                    stat.setInt(index, (Integer) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.BIGINT) {
            if (obj == null) {
                stat.setNull(index, Types.BIGINT);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Long.class);
                if (val instanceof Long) {
                    stat.setLong(index, (Long) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.FLOAT) {
            if (obj == null) {
                stat.setNull(index, Types.FLOAT);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Float.class);
                if (val instanceof Float) {
                    stat.setFloat(index, (Float) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.REAL) {
            if (obj == null) {
                stat.setNull(index, Types.REAL);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
                if (val instanceof BigDecimal) {
                    stat.setBigDecimal(index, (BigDecimal) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.DECIMAL) {
            if (obj == null) {
                stat.setNull(index, Types.DECIMAL);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, BigDecimal.class);
                if (val instanceof BigDecimal) {
                    stat.setBigDecimal(index, (BigDecimal) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.DOUBLE) {
            if (obj == null) {
                stat.setNull(index, Types.DOUBLE);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Double.class);
                if (val instanceof Double) {
                    stat.setDouble(index, (Double) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.NUMERIC) {
            if (obj == null) {
                stat.setNull(index, Types.NUMERIC);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Double.class);
                if (val instanceof Double) {
                    stat.setDouble(index, (Double) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.VARCHAR) {
            if (obj == null) {
                stat.setNull(index, Types.VARCHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    stat.setString(index, (String) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.CHAR) {
            if (obj == null) {
                stat.setNull(index, Types.CHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    stat.setString(index, (String) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.NVARCHAR) {
            if (obj == null) {
                stat.setNull(index, Types.NVARCHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    stat.setNString(index, (String) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.NCHAR) {
            if (obj == null) {
                stat.setNull(index, Types.NCHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    stat.setNString(index, (String) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.DATE) {
            if (obj == null) {
                stat.setNull(index, Types.DATE);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, java.sql.Date.class);
                if (val instanceof java.sql.Date) {
                    stat.setDate(index, (java.sql.Date) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.TIME) {
            if (obj == null) {
                stat.setNull(index, Types.TIME);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Time.class);
                if (val instanceof Time) {
                    stat.setTime(index, (Time) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.TIME_WITH_TIMEZONE) {
            if (obj == null) {
                stat.setNull(index, Types.TIME_WITH_TIMEZONE);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Time.class);
                if (val instanceof Time) {
                    stat.setTime(index, (Time) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.TIMESTAMP) {
            if (obj == null) {
                stat.setNull(index, Types.TIMESTAMP);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Timestamp.class);
                if (val instanceof Timestamp) {
                    stat.setTimestamp(index, (Timestamp) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.TIMESTAMP_WITH_TIMEZONE) {
            if (obj == null) {
                stat.setNull(index, Types.TIMESTAMP_WITH_TIMEZONE);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, Timestamp.class);
                if (val instanceof Timestamp) {
                    stat.setTimestamp(index, (Timestamp) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.NULL) {
            stat.setNull(index, Types.NULL);
            return true;
        }
        if (type == JDBCType.BLOB) {
            if (obj == null) {
                stat.setNull(index, Types.BLOB);
                return true;
            } else {
                if (obj instanceof Blob) {
                    stat.setBlob(index, (Blob) obj);
                    return true;
                }
                Object val = ObjectConvertor.tryConvertAsType(obj, InputStream.class);
                if (val instanceof InputStream) {
                    if (val instanceof ByteArrayInputStream) {
                        ByteArrayInputStream bis = (ByteArrayInputStream) val;
                        stat.setBlob(index, bis, bis.available());
                    } else {
                        stat.setBlob(index, (InputStream) val);
                    }
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.CLOB) {
            if (obj == null) {
                stat.setNull(index, Types.CLOB);
                return true;
            } else {
                if (obj instanceof Clob) {
                    stat.setClob(index, (Clob) obj);
                    return true;
                }
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    String str = (String) val;
                    stat.setClob(index, new StringReader(str), str.length());
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.NCLOB) {
            if (obj == null) {
                stat.setNull(index, Types.NCLOB);
                return true;
            } else {
                if (obj instanceof NClob) {
                    stat.setNClob(index, (NClob) obj);
                    return true;
                }
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    String str = (String) val;
                    stat.setNClob(index, new StringReader(str), str.length());
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.LONGVARCHAR) {
            if (obj == null) {
                stat.setNull(index, Types.LONGVARCHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    String str = (String) val;
                    stat.setCharacterStream(index, new StringReader(str), str.length());
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.LONGNVARCHAR) {
            if (obj == null) {
                stat.setNull(index, Types.LONGNVARCHAR);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, String.class);
                if (val instanceof String) {
                    String str = (String) val;
                    stat.setNCharacterStream(index, new StringReader(str), str.length());
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.BINARY) {
            if (obj == null) {
                stat.setNull(index, Types.BINARY);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, byte[].class);
                if (val instanceof byte[]) {
                    stat.setBytes(index, (byte[]) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.VARBINARY) {
            if (obj == null) {
                stat.setNull(index, Types.VARBINARY);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, byte[].class);
                if (val instanceof byte[]) {
                    stat.setBytes(index, (byte[]) val);
                    return true;
                }
            }
            return false;
        }
        if (type == JDBCType.LONGVARBINARY) {
            if (obj == null) {
                stat.setNull(index, Types.LONGVARBINARY);
                return true;
            } else {
                Object val = ObjectConvertor.tryConvertAsType(obj, InputStream.class);
                if (val instanceof InputStream) {
                    if (val instanceof ByteArrayInputStream) {
                        ByteArrayInputStream bis = (ByteArrayInputStream) val;
                        stat.setBinaryStream(index, bis, bis.available());
                    } else {
                        stat.setBinaryStream(index, (InputStream) val);
                    }
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    public static void setStatementNullWithType(PreparedStatement stat, int index, Class<?> clazz, SQLType jdbcType) throws SQLException {
        if (jdbcType != null) {
            boolean handled = setStatementObjectWithJdbcType(stat, index, null, jdbcType);
            if (handled) {
                return;
            }
        }
        if (clazz == null) {
            if (jdbcType != null) {
                stat.setObject(index, null, jdbcType);
            } else {
                stat.setObject(index, null);
            }
            return;
        }
        if (TypeOf.typeOfAny(clazz, short.class, Short.class, int.class, Integer.class, AtomicInteger.class)) {
            stat.setNull(index, Types.INTEGER);
            return;
        }
        if (TypeOf.typeOfAny(clazz, long.class, Long.class, AtomicLong.class, BigInteger.class)) {
            stat.setNull(index, Types.BIGINT);
            return;
        }
        if (TypeOf.typeOfAny(clazz, String.class, CharSequence.class, Appendable.class, char[].class)) {
            stat.setNull(index, Types.VARCHAR);
            return;
        }
        if (ObjectConvertor.isNumericType(clazz)) {
            stat.setNull(index, Types.NUMERIC);
            return;
        }
        if (ObjectConvertor.isDateType(clazz)) {
            stat.setNull(index, Types.DATE);
            return;
        }
        if (ObjectConvertor.isBooleanType(clazz)) {
            stat.setNull(index, Types.BOOLEAN);
            return;
        }
        stat.setNull(index, Types.VARCHAR);
    }

    public static QueryResult parseResultSet(ResultSet rs) throws SQLException {
        return parseResultSet(rs, -1, null);
    }

    public static QueryResult parseResultSet(ResultSet rs, int maxCount) throws SQLException {
        return parseResultSet(rs, maxCount, null);
    }

    public static QueryResult parseResultSet(ResultSet rs, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        try {
            QueryResult ret = new QueryResult();

            ResultSetMetaData metaData = rs.getMetaData();

            List<QueryColumn> columns = parseResultSetColumns(metaData, columnNameMapper);
            List<Map<String, Object>> rows = new LinkedList<>();
            ret.setColumns(columns);
            ret.setRows(rows);

            int currCount = 0;
            while (rs.next()) {
                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }

                Map<String, Object> map = convertResultSetRowAsMap(columns, rs);
                rows.add(map);

                currCount++;

                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }
            }

            return ret;
        } finally {
            rs.close();

        }
    }

    public static Object getResultObject(ResultSet rs, int columnIndex, int sqlType) throws SQLException {
        Object val = null;
        if (Arrays.asList(Types.VARCHAR, Types.LONGVARCHAR, Types.CHAR).contains(sqlType)) {
            val = rs.getString(columnIndex);
        } else if (Arrays.asList(Types.NVARCHAR, Types.NCHAR, Types.LONGNVARCHAR).contains(sqlType)) {
            val = rs.getString(columnIndex);
        } else if (Types.INTEGER == sqlType) {
            val = rs.getInt(columnIndex);
        } else if (Types.BIGINT == sqlType) {
            val = rs.getLong(columnIndex);
        } else if (Types.FLOAT == sqlType) {
            val = rs.getFloat(columnIndex);
        } else if (Types.REAL == sqlType) {
            val = rs.getBigDecimal(columnIndex);
        } else if (Types.DOUBLE == sqlType) {
            val = rs.getDouble(columnIndex);
        } else if (Types.NUMERIC == sqlType) {
            val = rs.getBigDecimal(columnIndex);
        } else if (Types.DATE == sqlType) {
            val = rs.getDate(columnIndex);
        } else if (Types.TIME == sqlType) {
            val = rs.getTime(columnIndex);
        } else if (Types.TIMESTAMP == sqlType) {
            val = rs.getTimestamp(columnIndex);
        } else if (Arrays.asList(Types.TINYINT, Types.SMALLINT).contains(sqlType)) {
            val = rs.getShort(columnIndex);
        } else if (Types.NULL == sqlType) {
            val = null;
        } else if (Types.BLOB == sqlType) {
            val = rs.getBlob(columnIndex);
        } else if (Types.CLOB == sqlType) {
            val = rs.getClob(columnIndex);
        } else if (Types.NCLOB == sqlType) {
            val = rs.getNClob(columnIndex);
        } else if (Types.BOOLEAN == sqlType) {
            val = rs.getBoolean(columnIndex);
        } else if (Types.BIT == sqlType) {
            val = rs.getInt(columnIndex);
        } else if (Arrays.asList(Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY).contains(sqlType)) {
            val = rs.getBytes(columnIndex);
        } else if (Types.ARRAY == sqlType) {
            val = rs.getArray(columnIndex);
        } else if (Types.SQLXML == sqlType) {
            val = rs.getSQLXML(columnIndex);
        } else if (Types.TIME_WITH_TIMEZONE == sqlType) {
            val = rs.getTime(columnIndex);
        } else if (Types.TIMESTAMP_WITH_TIMEZONE == sqlType) {
            val = rs.getTimestamp(columnIndex);
        } else {
            val = rs.getObject(columnIndex);
        }
        boolean hasHandle = false;
        for (ResultSetObjectConvertHandler handler : RESULT_SET_OBJECT_CONVERT_HANDLERS) {
            if (handler.support(val)) {
                val = handler.convert(val);
                hasHandle = true;
                break;
            }
        }
        if (!hasHandle) {
            if (val instanceof Clob) {
                val = ObjectConvertor.tryConvertAsType(val, String.class);
            }
        }

        return val;
    }


    public static <T> List<T> parseResultSetAsBeanList(ResultSet rs, Class<T> beanClass) throws SQLException {
        return parseResultSetAsBeanList(rs, beanClass, -1, null);
    }

    public static <T> List<T> parseResultSetAsBeanList(ResultSet rs, Class<T> beanClass, int maxCount) throws SQLException {
        return parseResultSetAsBeanList(rs, beanClass, maxCount, null);
    }

    public static <T> List<T> parseResultSetAsBeanList(ResultSet rs, Class<T> beanClass, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        try {
            List<T> ret = new LinkedList<>();

            ResultSetMetaData metaData = rs.getMetaData();

            List<QueryColumn> columns = parseResultSetColumns(metaData, columnNameMapper);

            SQLBiFunction<List<QueryColumn>, ResultSet, T> beanConvertor = createResultSetRowBeanConvertor(beanClass);

            boolean isDefaultMapType = (beanClass == null || TypeOf.typeOf(beanClass, Map.class));
            int currCount = 0;
            while (rs.next()) {
                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }

                Object item = beanConvertor.apply(columns, rs);

                ret.add((T) item);

                currCount++;

                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }
            }

            return ret;
        } finally {
            rs.close();
        }
    }


    public static Map<String, Object> convertResultSetRowAsMap(List<QueryColumn> columns, ResultSet rs) throws SQLException {
        if (columns == null || columns.isEmpty()) {
            columns = parseResultSetColumns(rs);
        }
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            QueryColumn col = columns.get(i);
            Object val = getResultObject(rs, i + 1, col.getType());
            map.put(col.getName(), val);
        }
        return map;
    }


    public static <E> SQLBiFunction<List<QueryColumn>, ResultSet, E> createResultSetRowBeanConvertor(Class<E> beanClass) {
        boolean isDefaultMapType = (beanClass == null || TypeOf.typeOf(beanClass, Map.class));
        if (isDefaultMapType) {
            return (columns, rs) -> {
                Map<String, Object> map = convertResultSetRowAsMap(columns, rs);
                return (E) map;
            };
        }
        return (columns, rs) -> {
            try {
                Map<String, Object> map = convertResultSetRowAsMap(columns, rs);
                E bean = ReflectResolver.getInstance(beanClass);
                ReflectResolver.map2bean(map, bean);
                return bean;
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        };
    }

    public static List<QueryColumn> parseResultSetColumns(ResultSet rs) throws SQLException {
        return parseResultSetColumns(rs, null);
    }

    public static List<QueryColumn> parseResultSetColumns(ResultSet rs, Function<String, String> columnNameMapper) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        return parseResultSetColumns(metaData, columnNameMapper);
    }

    public static List<QueryColumn> parseResultSetColumns(ResultSetMetaData metaData) throws SQLException {
        return parseResultSetColumns(metaData, null);
    }

    public static List<QueryColumn> parseResultSetColumns(ResultSetMetaData metaData, Function<String, String> columnNameMapper) throws SQLException {
        List<QueryColumn> columns = new ArrayList<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            QueryColumn col = new QueryColumn();
            col.setIndex(i);
            String colName = metaData.getColumnLabel(i + 1);
            if (columnNameMapper != null) {
                colName = columnNameMapper.apply(colName);
            }
            col.setName(colName);
            col.setOriginName(metaData.getColumnName(i + 1));
            col.setCatalog(metaData.getCatalogName(i + 1));
            col.setClazzName(metaData.getColumnClassName(i + 1));
            col.setDisplaySize(metaData.getColumnDisplaySize(i + 1));
            col.setLabel(metaData.getColumnLabel(i + 1));
            col.setType(metaData.getColumnType(i + 1));
            col.setTypeName(metaData.getColumnTypeName(i + 1));
            col.setPrecision(metaData.getPrecision(i + 1));
            col.setScale(metaData.getScale(i + 1));
            col.setSchema(metaData.getSchemaName(i + 1));
            col.setTable(metaData.getTableName(i + 1));
            col.setNullable(metaData.isNullable(i + 1) != ResultSetMetaData.columnNoNulls);
            col.setAutoIncrement(metaData.isAutoIncrement(i + 1));
            col.setReadonly(metaData.isReadOnly(i + 1));
            col.setWritable(metaData.isWritable(i + 1));
            col.setCaseSensitive(metaData.isCaseSensitive(i + 1));
            col.setCurrency(metaData.isCurrency(i + 1));
            col.setDefinitelyWritable(metaData.isDefinitelyWritable(i + 1));
            col.setSearchable(metaData.isSearchable(i + 1));
            col.setSigned(metaData.isSigned(i + 1));

            columns.add(col);
        }
        return columns;
    }

}
