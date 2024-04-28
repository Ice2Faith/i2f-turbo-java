package i2f.springboot.jdbc.bql.components;

import i2f.bindsql.BindSql;
import i2f.bql.core.Bql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.SQLBiFunction;
import i2f.jdbc.SQLFunction;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
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
public class BqlTemplate {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;

    public BqlTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            T ret = JdbcResolver.get(conn, sql, clazz);
            log.debug("query get type : {}", ret == null ? "null" : ret.getClass().getSimpleName());
            return ret;
        });
    }

    public <T> T get(BindSql bql, Class<T> clazz) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            T ret = JdbcResolver.get(conn, sql, clazz);
            log.debug("query get type : {}", ret == null ? "null" : ret.getClass().getSimpleName());
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
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            T ret = JdbcResolver.find(conn, sql, clazz, columnNameMapper);
            log.debug("query find type : {}", ret == null ? "null" : ret.getClass().getSimpleName());
            return ret;
        });
    }

    public <T> T find(BindSql bql, Class<T> clazz) throws SQLException {
        return find(bql, clazz, null);
    }

    public <T> T find(BindSql bql, Class<T> clazz, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            T ret = JdbcResolver.find(conn, sql, clazz, columnNameMapper);
            log.debug("query find type : {}", ret == null ? "null" : ret.getClass().getSimpleName());
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
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            List<T> ret = JdbcResolver.list(conn, sql, clazz, maxCount, columnNameMapper);
            log.debug("query list rows is empty: {}", ret.isEmpty());
            return ret;
        });
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz) throws SQLException {
        return list(bql, clazz, -1, null);
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz, int maxCount) throws SQLException {
        return list(bql, clazz, maxCount, null);
    }

    public <T> List<T> list(BindSql bql, Class<T> clazz, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            List<T> ret = JdbcResolver.list(conn, sql, clazz, maxCount, columnNameMapper);
            log.debug("query list rows is empty : {}", ret.isEmpty());
            return ret;
        });
    }

    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiPage page) throws SQLException {
        return page(bql, clazz, page, null);
    }

    public <T> Page<T> page(Bql<?> bql, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            Page<T> ret = JdbcResolver.page(conn, sql, clazz, page, columnNameMapper);
            log.debug("query page, total : {}, rows is empty : {}", ret.getTotal(), ret.getList().isEmpty());
            return ret;
        });
    }

    public <T> Page<T> page(BindSql bql, Class<T> clazz, ApiPage page) throws SQLException {
        return page(bql, clazz, page, null);
    }

    public <T> Page<T> page(BindSql bql, Class<T> clazz, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            Page<T> ret = JdbcResolver.page(conn, sql, clazz, page, columnNameMapper);
            log.debug("query page, total : {}, rows is empty : {}", ret.getTotal(), ret.getList().isEmpty());
            return ret;
        });
    }

    public Map<String, Object> find(Bql<?> bql) throws SQLException {
        return find(bql, (Function<String, String>) null);
    }

    public Map<String, Object> find(Bql<?> bql, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.find(conn, sql, columnNameMapper);
            log.debug("query find one is null : {}", ret == null);
            return ret;
        });
    }

    public Map<String, Object> find(BindSql bql) throws SQLException {
        return find(bql, (Function<String, String>) null);
    }

    public Map<String, Object> find(BindSql bql, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            Map<String, Object> ret = JdbcResolver.find(conn, sql, columnNameMapper);
            log.debug("query find one is null : {}", ret == null);
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
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            List<Map<String, Object>> ret = JdbcResolver.list(conn, sql, maxCount, columnNameMapper);
            log.debug("query return rows is empty : {}", ret.isEmpty());
            return ret;
        });
    }

    public List<Map<String, Object>> list(BindSql bql) throws SQLException {
        return list(bql, -1, null);
    }

    public List<Map<String, Object>> list(BindSql bql, int maxCount) throws SQLException {
        return list(bql, maxCount, null);
    }

    public List<Map<String, Object>> list(BindSql bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            List<Map<String, Object>> ret = JdbcResolver.list(conn, sql, maxCount, columnNameMapper);
            log.debug("query return rows is empty : {}", ret.isEmpty());
            return ret;
        });
    }

    public Page<Map<String, Object>> page(Bql<?> bql, ApiPage page) throws SQLException {
        return page(bql, page, null);
    }

    public Page<Map<String, Object>> page(Bql<?> bql, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql.$$(), (conn, sql) -> {
            Page<Map<String, Object>> ret = JdbcResolver.page(conn, sql, page, columnNameMapper);
            log.debug("query return , total : {}, rows is empty : {}", ret.getTotal(), ret.getList().isEmpty());
            return ret;
        });
    }

    public Page<Map<String, Object>> page(BindSql bql, ApiPage page) throws SQLException {
        return page(bql, page, null);
    }

    public Page<Map<String, Object>> page(BindSql bql, ApiPage page, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            Page<Map<String, Object>> ret = JdbcResolver.page(conn, sql, page, columnNameMapper);
            log.debug("query return , total : {}, rows is empty : {}", ret.getTotal(), ret.getList().isEmpty());
            return ret;
        });
    }

    protected <R> R bqlDelegate(BindSql bql, SQLBiFunction<Connection, BindSql, R> action) throws SQLException {
        DataSource dataSource = getDataSource();

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            log.debug("bql execute : {}", bql);
            R ret = action.apply(conn, bql);
            return ret;
        } catch (Throwable e) {
            DataSourceUtils.releaseConnection(conn, dataSource);
            conn = null;
            log.error("bql error : " + e.getMessage() + " bql :" + bql, e);
            if (e instanceof RuntimeException) {
                throw e;
            }
            if (e instanceof SQLException) {
                throw e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }


    public int update(Bql<?> bql) throws SQLException {
        return update(bql.$$());
    }

    public int update(BindSql bql) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            int ret = JdbcResolver.update(conn, sql);
            log.debug("update return : {}", ret);
            return ret;
        });
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

    public QueryResult queryRaw(BindSql bql) throws SQLException {
        return queryRaw(bql, -1, null);
    }

    public QueryResult queryRaw(BindSql bql, int maxCount) throws SQLException {
        return queryRaw(bql, maxCount, null);
    }

    public QueryResult queryRaw(BindSql bql, int maxCount, Function<String, String> columnNameMapper) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            QueryResult ret = JdbcResolver.query(conn, sql, maxCount, columnNameMapper);
            log.debug("query return rows is empty : {}", ret.getRows().isEmpty());
            return ret;
        });
    }

    public <R> R queryHandler(Bql<?> bql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return queryHandler(bql.$$(), resultSetHandler);
    }

    public <R> R queryHandler(BindSql bql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return bqlDelegate(bql, (conn, sql) -> {
            R ret = JdbcResolver.query(conn, sql, resultSetHandler);
            log.debug("query handler return type : {}", ret != null ? ret.getClass().getSimpleName() : "null");
            return ret;
        });
    }
}
