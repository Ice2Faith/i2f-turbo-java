package i2f.jdbc;

import i2f.bindsql.BindSql;
import i2f.bindsql.BindSqlWrappers;
import i2f.bindsql.data.PageBindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;
import i2f.reflect.ReflectResolver;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:58
 * @desc
 */
public class JdbcResolver {
    public static Connection getConnection(String driver,
                                           String url) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url);
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           String username,
                                           String password) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           Properties properties) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url, properties);
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

    public static List<Object> transaction(Connection conn, List<BindSql> sqls) throws SQLException {
        return transaction(conn, (connection, sqlList) -> {
            List<Object> ret = new ArrayList<>();
            for (BindSql sql : sqlList) {
                if (sql.isUpdate()) {
                    int val = update(connection, sql.getSql(), sql.getArgs());
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
        PreparedStatement stat = preparedStatement(conn, sql, args);
        ResultSet rs = stat.executeQuery();
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


    public static Page<Map<String, Object>> page(Connection conn, BindSql sql, ApiPage page) throws SQLException {
        return page(conn, sql.getSql(), sql.getArgs(), page, null);
    }

    public static Page<Map<String, Object>> page(Connection conn, BindSql sql, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return page(conn, sql.getSql(), sql.getArgs(), page, columnNameMapper);
    }

    public static Page<Map<String, Object>> page(Connection conn, String sql, List<Object> args, ApiPage page) throws SQLException {
        return page(conn, sql, args, page, null);
    }

    public static Page<Map<String, Object>> page(Connection conn, String sql, List<Object> args, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
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

    public static <T> Page<T> page(Connection conn, BindSql sql, Class<T> clazz, ApiPage page) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, sql, page);
        return page(conn, pageBindSql, clazz, null);
    }

    public static <T> Page<T> page(Connection conn, BindSql sql, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, sql, page);
        return page(conn, pageBindSql, clazz, columnNameMapper);
    }

    public static <T> Page<T> page(Connection conn, String sql, List<Object> args, Class<T> clazz, ApiPage page) throws SQLException {
        PageBindSql pageBindSql = BindSqlWrappers.page(conn, new BindSql(sql, args), page);
        return page(conn, pageBindSql, clazz, null);
    }

    public static <T> Page<T> page(Connection conn, String sql, List<Object> args, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
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
        PreparedStatement stat = preparedStatement(conn, sql, args);
        int ret = stat.executeUpdate();
        stat.close();
        return ret;
    }

    public static boolean call(Connection conn, String sql) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, null, null);
        return (Boolean) map.get(-1);
    }

    public static boolean call(Connection conn, String sql, List<?> args) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, args, null);
        return (Boolean) map.get(-1);
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
        CallableStatement stat = callStatement(conn, sql, args, outParams);
        boolean ok = stat.execute();
        Map<Integer, Object> ret = new LinkedHashMap<>();
        ret.put(-1, ok);
        if (outParams != null) {
            for (Map.Entry<Integer, SQLType> entry : outParams.entrySet()) {
                Object val = stat.getObject(entry.getKey() + 1);
                ret.put(entry.getKey(), val);
            }
        }
        return ret;
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

    public static CallableStatement callStatement(Connection conn, String sql, List<?> args) throws SQLException {
        return callStatement(conn, sql, args, null);
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
                    stat.setObject(i, arg);
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
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                stat.setObject(i, arg);
                i++;
            }
        }
        return stat;
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
            int columnCount = metaData.getColumnCount();

            List<QueryColumn> columns = parseResultSetColumns(metaData, columnNameMapper);
            List<Map<String, Object>> rows = new LinkedList<>();
            ret.setColumns(columns);
            ret.setRows(rows);

            int currCount = 0;
            while (rs.next()) {
                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }

                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    Object val = rs.getObject(i + 1);
                    map.put(columns.get(i).getName(), val);
                }
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
            int columnCount = metaData.getColumnCount();

            List<String> columnNames = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                String colName = metaData.getColumnLabel(i + 1);
                if (columnNameMapper != null) {
                    colName = columnNameMapper.apply(colName);
                }
                columnNames.add(colName);
            }

            int currCount = 0;
            while (rs.next()) {
                if (maxCount >= 0 && currCount >= maxCount) {
                    break;
                }

                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    Object val = rs.getObject(i + 1);
                    map.put(columnNames.get(i), val);
                }

                try {
                    T bean = ReflectResolver.getInstance(beanClass);
                    ReflectResolver.map2bean(map, bean);

                    ret.add(bean);
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }

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

    public static List<QueryColumn> parseResultSetColumns(ResultSet rs, Function<String, String> columnNameMapper) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        return parseResultSetColumns(metaData, columnNameMapper);
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
