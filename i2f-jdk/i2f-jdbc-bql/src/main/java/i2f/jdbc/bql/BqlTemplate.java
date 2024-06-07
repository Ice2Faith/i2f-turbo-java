package i2f.jdbc.bql;

import i2f.bql.core.Bql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.SQLFunction;
import i2f.jdbc.context.JdbcInvokeContextProvider;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.template.JdbcTemplate;
import i2f.page.ApiPage;
import i2f.page.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:37
 * @desc
 */
public class BqlTemplate extends JdbcTemplate {

    public BqlTemplate(JdbcInvokeContextProvider<?> contextProvider) {
        super(contextProvider);
    }

    public <T> int insert(T bean) throws SQLException {
        return update(Bql.$bean()
                .$beanInsert(bean));
    }

    public <T> int insertBatchValues(List<T> list) throws SQLException {
        return update(Bql.$bean().$beanInsertBatchValues(list));
    }

    public <T> int insertBatchUnionAll(List<T> list) throws SQLException {
        return update(Bql.$bean().$beanInsertBatchUnionAll(list));
    }

    public <T> int update(T update, T condition) throws SQLException {
        return update(Bql.$bean()
                .$beanUpdate(update, condition)
        );
    }

    public <T> int delete(T condition) throws SQLException {
        return update(Bql.$bean()
                .$beanDelete(condition)
        );
    }


    public <T> T get(Bql<?> bql, Class<T> clazz) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            T ret = JdbcResolver.get(conn, sql, clazz);
            return ret;
        });
    }


    public <T> T find(T condition) throws SQLException {
        return find(Bql.$bean().$beanQuery(condition), (Class<T>) condition.getClass());
    }

    public <T> T find(Bql<?> bql, Class<T> clazz) throws SQLException {
        return find(bql, clazz, null);
    }

    public <T> T find(Bql<?> bql, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            T ret = JdbcResolver.find(conn, sql, clazz, columnNameMapper);
            return ret;
        });
    }


    public <T> List<T> list(T condition) throws SQLException {
        return list(Bql.$bean().$beanQuery(condition), (Class<T>) condition.getClass());
    }

    public <T> Page<T> page(T condition, ApiPage page) throws SQLException {
        return page(Bql.$bean().$beanQuery(condition), (Class<T>) condition.getClass(), page);
    }

    public <T> List<T> list(Bql<?> bql, Class<T> clazz) throws SQLException {
        return list(bql, clazz, -1, null);
    }

    public <T> List<T> list(Bql<?> bql, Class<T> clazz, int maxCount) throws SQLException {
        return list(bql, clazz, maxCount, null);
    }

    public <T> List<T> list(Bql<?> bql, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            List<T> ret = JdbcResolver.list(conn, sql, clazz, maxCount, columnNameMapper);
            return ret;
        });
    }


    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiPage page) throws SQLException {
        return page(bql, clazz, page, null);
    }

    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            Page<T> ret = JdbcResolver.page(conn, sql, clazz, page, columnNameMapper);
            return ret;
        });
    }

    public Map<String, Object> find(Bql<?> bql) throws SQLException {
        return find(bql, (Function<String, String>) null);
    }

    public Map<String, Object> find(Bql<?> bql, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.find(conn, sql, columnNameMapper);
            return ret;
        });
    }

    public List<Map<String, Object>> list(Bql<?> bql) throws SQLException {
        return list(bql, -1, null);
    }

    public List<Map<String, Object>> list(Bql<?> bql, int maxCount) throws SQLException {
        return list(bql, maxCount, null);
    }

    public List<Map<String, Object>> list(Bql<?> bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            List<Map<String, Object>> ret = JdbcResolver.list(conn, sql, maxCount, columnNameMapper);
            return ret;
        });
    }

    public Page<Map<String, Object>> page(Bql<?> bql, ApiPage page) throws SQLException {
        return page(bql, page, null);
    }

    public Page<Map<String, Object>> page(Bql<?> bql, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            Page<Map<String, Object>> ret = JdbcResolver.page(conn, sql, page, columnNameMapper);
            return ret;
        });
    }

    public int update(Bql<?> bql) throws SQLException {
        return update(bql.$$());
    }


    public QueryResult queryRaw(Bql<?> bql) throws SQLException {
        return queryRaw(bql.$$(), -1, null);
    }

    public QueryResult queryRaw(Bql<?> bql, int maxCount) throws SQLException {
        return queryRaw(bql.$$(), maxCount, null);
    }

    public QueryResult queryRaw(Bql<?> bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return queryRaw(bql.$$(), maxCount, columnNameMapper);
    }

    public <R> R queryHandler(Bql<?> bql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return queryHandler(bql.$$(), resultSetHandler);
    }

}
