package i2f.jdbc.procedure.node.impl.tinyscript;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:56
 * @desc
 */
public class ExecContextMethodProvider {

    public Connection get_connection(String datasource) {
        return null;
    }

    public Object eval(String script) {
        return null;
    }

    public Object eval_script(String lang, String script) {
        return null;
    }

    public String render(String script) {
        return null;
    }

    public Object visit(String expression) {
        return null;
    }

    public void visit_set(String expression, Object value) {

    }

    public void visit_delete(String expression) {

    }

    public boolean test(String expression) {
        return false;
    }

    public boolean sql_adapt(String datasource, String databases) {
        return false;
    }

    public void sql_trans_begin(String datasource) {

    }

    public void sql_trans_commit(String datasource) {

    }

    public void sql_trans_rollback(String datasource) {

    }

    public void sql_trans_none(String datasource) {

    }

    public Object sql_query_object(String datasource, String sql, Object... args) {
        return null;
    }

    public Object sql_query_row(String datasource, String sql, Object... args) {
        return null;
    }

    public Object sql_query_list(String datasource, String sql, Object... args) {
        return null;
    }

    public Object sql_query_page(String datasource, int offset, int size, String sql, Object... args) {
        return null;
    }

    public Object sql_query_columns(String datasource, String sql, Object... args) {
        return null;
    }

    public Object sql_update(String datasource, String sql, Object... args) {
        return null;
    }

    public Object sql_script_query_object(String datasource, String sql) {
        return null;
    }

    public Object sql_script_query_row(String datasource, String sql) {
        return null;
    }

    public Object sql_script_query_list(String datasource, String sql) {
        return null;
    }

    public Object sql_script_query_page(String datasource, int offset, int size, String sql) {
        return null;
    }

    public Object sql_script_query_columns(String datasource, String sql) {
        return null;
    }

    public Object sql_script_update(String datasource, String sql) {
        return null;
    }
}
