package i2f.jdbc.procedure.node.impl.tinyscript;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:56
 * @desc
 */
public interface ExecContextMethodProvider {

    Connection get_connection(String datasource);

    Object eval(String script);

    Object eval_script(String lang, String script);

    String render(String script);

    Object visit(String expression);

    void visit_set(String expression, Object value);

    void visit_delete(String expression);

    boolean test(String expression);

    boolean sql_adapt(String datasource, String databases);

    void sql_trans_begin(String datasource);

    void sql_trans_commit(String datasource);

    void sql_trans_rollback(String datasource);

    void sql_trans_none(String datasource);

    Object sql_query_object(String datasource, String sql, Object... args);

    Object sql_query_row(String datasource, String sql, Object... args);

    Object sql_query_list(String datasource, String sql, Object... args);

    Object sql_query_page(String datasource, int offset, int size, String sql, Object... args);

    Object sql_query_columns(String datasource, String sql, Object... args);

    Object sql_update(String datasource, String sql, Object... args);

    Object sql_script_query_object(String datasource, String sql);

    Object sql_script_query_row(String datasource, String sql);

    Object sql_script_query_list(String datasource, String sql);

    Object sql_script_query_page(String datasource, int offset, int size, String sql);

    Object sql_script_query_columns(String datasource, String sql);

    Object sql_script_update(String datasource, String sql);
}
