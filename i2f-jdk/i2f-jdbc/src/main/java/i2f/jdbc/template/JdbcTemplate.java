package i2f.jdbc.template;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.SQLBiFunction;
import i2f.jdbc.SQLFunction;
import i2f.jdbc.context.JdbcInvokeContextProvider;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;
import lombok.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:37
 * @desc
 */
@Data
public class JdbcTemplate {

    protected JdbcInvokeContextProvider<?> contextProvider;

    public JdbcTemplate(JdbcInvokeContextProvider<?> contextProvider) {
        this.contextProvider = contextProvider;
    }

    public int update(String sql, List<?> args) throws SQLException {
        return update(new BindSql(sql, new ArrayList<>(args)));
    }

    public int update(BindSql bql) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            int ret = JdbcResolver.update(conn, sql);
            return ret;
        });
    }

    public <T> T get(String sql, List<?> args, Class<T> clazz) throws SQLException {
        return get(new BindSql(sql, new ArrayList<>(args)), clazz);
    }

    public <T> T get(BindSql bql, Class<T> clazz) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            T ret = JdbcResolver.get(conn, sql, clazz);
            return ret;
        });
    }

    public <T> T find(String sql, List<?> args, Class<T> clazz) throws SQLException {
        return find(new BindSql(sql, new ArrayList<>(args)), clazz, null);
    }

    public <T> T find(String sql, List<?> args, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        return find(new BindSql(sql, new ArrayList<>(args)), clazz, columnNameMapper);
    }

    public <T> T find(BindSql bql, Class<T> clazz) throws SQLException {
        return find(bql, clazz, null);
    }

    public <T> T find(BindSql bql, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            T ret = JdbcResolver.find(conn, sql, clazz, columnNameMapper);
            return ret;
        });
    }

    public <T> List<T> list(String sql, List<?> args, Class<T> clazz) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), clazz, -1, null);
    }

    public <T> List<T> list(String sql, List<?> args, Class<T> clazz, int maxCount) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), clazz, maxCount, null);
    }

    public <T> List<T> list(String sql, List<?> args, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), clazz, maxCount, columnNameMapper);
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz) throws SQLException {
        return list(bql, clazz, -1, null);
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz, int maxCount) throws SQLException {
        return list(bql, clazz, maxCount, null);
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            List<T> ret = JdbcResolver.list(conn, sql, clazz, maxCount, columnNameMapper);
            return ret;
        });
    }

    public <T> Page<T> page(String sql, List<?> args, Class<T> clazz, ApiPage page) throws SQLException {
        return page(new BindSql(sql, new ArrayList<>(args)), clazz, page, null);
    }

    public <T> Page<T> page(String sql, List<?> args, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return page(new BindSql(sql, new ArrayList<>(args)), clazz, page, columnNameMapper);
    }

    public <T> Page<T> page(BindSql bql, Class<T> clazz, ApiPage page) throws SQLException {
        return page(bql, clazz, page, null);
    }

    public <T> Page<T> page(BindSql bql, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            Page<T> ret = JdbcResolver.page(conn, sql, clazz, page, columnNameMapper);
            return ret;
        });
    }

    public Map<String, Object> find(String sql, List<?> args) throws SQLException {
        return find(new BindSql(sql, new ArrayList<>(args)), (Function<String, String>) null);
    }

    public Map<String, Object> find(String sql, List<?> args, Function<String, String> columnNameMapper) throws SQLException {
        return find(new BindSql(sql, new ArrayList<>(args)), columnNameMapper);
    }

    public Map<String, Object> find(BindSql bql) throws SQLException {
        return find(bql, (Function<String, String>) null);
    }

    public Map<String, Object> find(BindSql bql, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.find(conn, sql, columnNameMapper);
            return ret;
        });
    }

    public List<Map<String, Object>> list(String sql, List<?> args) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), -1, null);
    }

    public List<Map<String, Object>> list(String sql, List<?> args, int maxCount) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), maxCount, null);
    }

    public List<Map<String, Object>> list(String sql, List<?> args, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return list(new BindSql(sql, new ArrayList<>(args)), maxCount, columnNameMapper);
    }

    public List<Map<String, Object>> list(BindSql bql) throws SQLException {
        return list(bql, -1, null);
    }

    public List<Map<String, Object>> list(BindSql bql, int maxCount) throws SQLException {
        return list(bql, maxCount, null);
    }

    public List<Map<String, Object>> list(BindSql bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            List<Map<String, Object>> ret = JdbcResolver.list(conn, sql, maxCount, columnNameMapper);
            return ret;
        });
    }

    public Page<Map<String, Object>> page(String sql, List<?> args, ApiPage page) throws SQLException {
        return page(new BindSql(sql, new ArrayList<>(args)), page, null);
    }

    public Page<Map<String, Object>> page(String sql, List<?> args, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return page(new BindSql(sql, new ArrayList<>(args)), page, columnNameMapper);
    }

    public Page<Map<String, Object>> page(BindSql bql, ApiPage page) throws SQLException {
        return page(bql, page, null);
    }

    public Page<Map<String, Object>> page(BindSql bql, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            Page<Map<String, Object>> ret = JdbcResolver.page(conn, sql, page, columnNameMapper);
            return ret;
        });
    }

    public boolean call(String sql, List<?> args) throws SQLException {
        return call(new BindSql(sql, new ArrayList<>(args)));
    }

    public boolean call(BindSql bql) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            boolean ret = JdbcResolver.call(conn, sql);
            return ret;
        });
    }

    public Map<Integer, Object> call(String sql, List<?> args, Map<Integer, SQLType> outParams) throws SQLException {
        return call(new BindSql(sql, new ArrayList<>(args)), outParams);
    }

    public Map<Integer, Object> call(BindSql bql, Map<Integer, SQLType> outParams) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            Map<Integer, Object> ret = JdbcResolver.call(conn, sql, outParams);
            return ret;
        });
    }

    public Map<String, Object> callNaming(String sql, List<?> args) throws SQLException {
        return callNaming(new BindSql(sql, new ArrayList<>(args)));
    }

    public Map<String, Object> callNaming(BindSql bql) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.callNaming(conn, sql);
            return ret;
        });
    }

    public QueryResult queryRaw(String sql, List<?> args) throws SQLException {
        return queryRaw(new BindSql(sql, new ArrayList<>(args)), -1, null);
    }

    public QueryResult queryRaw(String sql, List<?> args, int maxCount) throws SQLException {
        return queryRaw(new BindSql(sql, new ArrayList<>(args)), maxCount, null);
    }

    public QueryResult queryRaw(String sql, List<?> args, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return queryRaw(new BindSql(sql, new ArrayList<>(args)), maxCount, columnNameMapper);
    }

    public QueryResult queryRaw(BindSql bql) throws SQLException {
        return queryRaw(bql, -1, null);
    }

    public QueryResult queryRaw(BindSql bql, int maxCount) throws SQLException {
        return queryRaw(bql, maxCount, null);
    }


    public QueryResult queryRaw(BindSql bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            QueryResult ret = JdbcResolver.query(conn, sql, maxCount, columnNameMapper);
            return ret;
        });
    }


    public <R> R queryHandler(String sql, List<?> args, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return queryHandler(new BindSql(sql, new ArrayList<>(args)), resultSetHandler);
    }

    public <R> R queryHandler(BindSql bql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return contextActionDelegate(bql, (conn, sql) -> {
            R ret = JdbcResolver.query(conn, sql, resultSetHandler);
            return ret;
        });
    }


    protected <T, R> R contextActionDelegate(T bql, SQLBiFunction<Connection, T, R> action) throws SQLException {
        Object context = contextProvider.beginContext();

        Connection conn = contextProvider.getConnectionInner(context);
        try {
            R ret = action.apply(conn, bql);
            return ret;
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            if (e instanceof SQLException) {
                throw e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            contextProvider.endContextInner(context, conn);
        }
    }
}
