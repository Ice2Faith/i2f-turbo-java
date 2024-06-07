package i2f.jdbc.proxy.basemapper;

import i2f.bindsql.BindSql;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 11:11
 * @desc
 */
public interface BaseMapper<T> {

    int insert(String table, Map<String, Object> map);

    int insert(T bean);

    int update(String table, Map<String, Object> updateMap, Map<String, Object> whereMap);

    int update(T bean, T cond);

    int delete(String table, Map<String, Object> map);

    int delete(T bean);

    List<Map<String, Object>> listMap(String table, Collection<String> cols, Map<String, Object> whereMap);

    List<Map<String, Object>> listMap(T bean);

    List<T> list(String table, Collection<String> cols, Map<String, Object> whereMap);

    List<T> list(T bean);

    Map<String, Object> findMap(String table, Collection<String> cols, Map<String, Object> whereMap);

    Map<String, Object> findMap(T bean);

    T find(String table, Collection<String> cols, Map<String, Object> whereMap);

    T find(T bean);

    long count(String table, Map<String, Object> whereMap);

    long count(T bean);

    Page<Map<String, Object>> pageMap(String table, Collection<String> cols, Map<String, Object> whereMap, ApiPage page);

    Page<Map<String, Object>> pageMap(T bean, ApiPage page);

    Page<T> page(String table, Collection<String> cols, Map<String, Object> whereMap, ApiPage page);

    Page<T> page(T bean, ApiPage page);

    int executeUpdate(BindSql sql);

    <R> R executeGet(BindSql sql);

    List<Map<String, Object>> executeListMap(BindSql sql);

    List<T> executeList(BindSql sql);

    Map<String, Object> executeFindMap(BindSql sql);

    T executeFind(BindSql sql);

    Page<Map<String, Object>> executePageMap(BindSql sql);

    Page<T> executePage(BindSql sql);

    QueryResult executeRaw(BindSql sql);
}
