package i2f.jdbc.bql;

import i2f.bql.core.Bql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.std.context.JdbcInvokeContextProvider;
import i2f.jdbc.std.func.SQLFunction;
import i2f.jdbc.template.JdbcTemplate;
import i2f.page.ApiOffsetSize;
import i2f.page.Page;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:37
 * @desc
 */
public class BqlTemplate extends JdbcTemplate {

    public BqlTemplate(JdbcInvokeContextProvider<?> contextProvider) {
        super(contextProvider);
    }

    public <T> void batch(Bql<?> bql, Iterable<T> iterator) throws SQLException {
        batch(bql, iterator.iterator(), null, -1);
    }

    public <T> void batch(Bql<?> bql, Iterable<T> iterator, int batchSize) throws SQLException {
        batch(bql, iterator.iterator(), null, batchSize);
    }

    public <T> void batch(Bql<?> bql, Iterable<T> iterator, Predicate<T> filter) throws SQLException {
        batch(bql, iterator.iterator(), filter, -1);
    }

    public <T> void batch(Bql<?> bql, Iterable<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch(bql, iterator.iterator(), filter, batchSize);
    }

    public <T> void batch(Bql<?> bql, Iterator<T> iterator) throws SQLException {
        batch(bql, iterator, null, -1);
    }

    public <T> void batch(Bql<?> bql, Iterator<T> iterator, int batchSize) throws SQLException {
        batch(bql, iterator, null, batchSize);
    }

    public <T> void batch(Bql<?> bql, Iterator<T> iterator, Predicate<T> filter) throws SQLException {
        batch(bql, iterator, filter, -1);
    }

    public <T> void batch(Bql<?> bql, Iterator<T> iterator, Predicate<T> filter, int batchSize) throws SQLException {
        batch(bql.$$(), iterator, filter, batchSize);
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

    public <T> int updateByPk(T update) throws SQLException {
        return update(Bql.$bean()
                .$beanUpdateByPk(update)
        );
    }

    public <T> int delete(T condition) throws SQLException {
        return update(Bql.$bean()
                .$beanDelete(condition)
        );
    }

    public <T, V extends Serializable> int deleteByPk(Class<T> beanClass, V... pkValues) throws SQLException {
        return update(Bql.$bean()
                .$beanDeleteByPk(beanClass, pkValues)
        );
    }

    public <T, V extends Serializable> int deleteByPk(Class<T> beanClass, Collection<V> pkValues) throws SQLException {
        return update(Bql.$bean()
                .$beanDeleteByPk(beanClass, pkValues)
        );
    }


    public <T> T get(Bql<?> bql, Class<T> clazz) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            T ret = JdbcResolver.get(conn, sql, clazz);
            return ret;
        });
    }

    public <T> T findByPk(Class<T> beanClass, Serializable pkValue) throws SQLException {
        return find(Bql.$bean().$beanQueryByPk(beanClass, pkValue), beanClass);
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

    public <T, V extends Serializable> List<T> listByPk(Class<T> beanClass, V... pkValues) throws SQLException {
        return list(Bql.$bean().$beanQueryByPk(beanClass, pkValues), beanClass);
    }

    public <T, V extends Serializable> List<T> listByPk(Class<T> beanClass, Collection<V> pkValues) throws SQLException {
        return list(Bql.$bean().$beanQueryByPk(beanClass, pkValues), beanClass);
    }

    public <T> List<T> list(T condition) throws SQLException {
        return list(Bql.$bean().$beanQuery(condition), (Class<T>) condition.getClass());
    }

    public <T> Page<T> page(T condition, ApiOffsetSize page) throws SQLException {
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


    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiOffsetSize page) throws SQLException {
        return page(bql, clazz, page, null);
    }

    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            Page<T> ret = JdbcResolver.page(conn, sql, clazz, page, columnNameMapper);
            return ret;
        });
    }

    public Map<String, Object> find(Bql<?> bql) throws SQLException {
        return find(bql, (Function<String, String>) null);
    }

    public Map<String, Object> findByPkMap(Class<?> beanClass, Serializable pkValue) throws SQLException {
        return find(Bql.$bean().$beanQueryByPk(beanClass, pkValue), (Function<String, String>) null);
    }

    public Map<String, Object> find(Bql<?> bql, Function<String, String> columnNameMapper) throws SQLException {
        return contextActionDelegate(bql.$$(), (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.find(conn, sql, columnNameMapper);
            return ret;
        });
    }

    public <V extends Serializable> List<Map<String, Object>> listByPkMap(Class<?> beanClass, V... pkValue) throws SQLException {
        return list(Bql.$bean().$beanQueryByPk(beanClass, pkValue), -1, null);
    }

    public <V extends Serializable> List<Map<String, Object>> listByPkMap(Class<?> beanClass, Collection<V> pkValue) throws SQLException {
        return list(Bql.$bean().$beanQueryByPk(beanClass, pkValue), -1, null);
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

    public Page<Map<String, Object>> page(Bql<?> bql, ApiOffsetSize page) throws SQLException {
        return page(bql, page, null);
    }

    public Page<Map<String, Object>> page(Bql<?> bql, ApiOffsetSize page, Function<String, String> columnNameMapper) throws SQLException {
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
