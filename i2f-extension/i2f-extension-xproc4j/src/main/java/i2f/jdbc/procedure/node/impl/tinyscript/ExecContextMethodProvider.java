package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.page.ApiOffsetSize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:56
 * @desc
 */
@Data
@NoArgsConstructor
public class ExecContextMethodProvider {
    public JdbcProcedureExecutor executor;
    public Object context;

    public ExecContextMethodProvider(JdbcProcedureExecutor executor, Object context) {
        this.executor = executor;
        this.context = context;
    }

    public Connection get_connection(String datasource) {
        return executor.getConnection(datasource, (Map<String, Object>) context);
    }

    public Object eval(String script) {
        return executor.eval(script, context);
    }

    public Object eval_script(String lang, String script) {
        return executor.evalScript(lang, script, (Map<String, Object>) context);
    }

    public String render(String script) {
        return executor.render(script, context);
    }

    public Object visit(String expression) {
        return executor.visit(expression, context);
    }

    public void visit_set(String expression, Object value) {
        executor.visitSet((Map<String, Object>) context, expression, value);
    }

    public void visit_delete(String expression) {
        executor.visitDelete((Map<String, Object>) context, expression);
    }

    public boolean test(String expression) {
        return executor.test(expression, context);
    }

    public boolean sql_adapt(String datasource, String databases) {
        return executor.sqlAdapt(datasource, databases, (Map<String, Object>) context);
    }

    public void sql_trans_begin(String datasource) {
        executor.sqlTransBegin(datasource, Connection.TRANSACTION_READ_COMMITTED, (Map<String, Object>) context);
    }

    public void sql_trans_commit(String datasource) {
        executor.sqlTransCommit(datasource, (Map<String, Object>) context);
    }

    public void sql_trans_rollback(String datasource) {
        executor.sqlTransRollback(datasource, (Map<String, Object>) context);
    }

    public void sql_trans_none(String datasource) {
        executor.sqlTransNone(datasource, (Map<String, Object>) context);
    }

    public Object sql_query_object(String datasource, String sql, Object... args) {
        return executor.sqlQueryObject(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
    }

    public Object sql_query_row(String datasource, String sql, Object... args) {
        return executor.sqlQueryRow(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
    }

    public Object sql_query_list(String datasource, String sql, Object... args) {
        return executor.sqlQueryList(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
    }

    public Object sql_query_page(String datasource, int offset, int size, String sql, Object... args) {
        return executor.sqlQueryPage(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null, new ApiOffsetSize(offset, size));
    }

    public Object sql_query_columns(String datasource, String sql, Object... args) {
        return executor.sqlQueryColumns(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context);
    }

    public Object sql_update(String datasource, String sql, Object... args) {
        return executor.sqlUpdate(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context);
    }

    public Object sql_script_query_object(String datasource, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlQueryObject(datasource, bql, (Map<String, Object>) context, null);
    }

    public Object sql_script_query_row(String datasource, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlQueryRow(datasource, bql, (Map<String, Object>) context, null);
    }

    public Object sql_script_query_list(String datasource, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlQueryList(datasource, bql, (Map<String, Object>) context, null);
    }

    public Object sql_script_query_page(String datasource, int offset, int size, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlQueryPage(datasource, bql, (Map<String, Object>) context, null, new ApiOffsetSize(offset, size));
    }

    public Object sql_script_query_columns(String datasource, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlQueryColumns(datasource, bql, (Map<String, Object>) context);
    }

    public Object sql_script_update(String datasource, String sql) {
        BindSql bql = executor.sqlScriptString(datasource, Collections.singletonList(new AbstractMap.SimpleEntry<>(null, sql)), (Map<String, Object>) context);
        return executor.sqlUpdate(datasource, bql, (Map<String, Object>) context);
    }
}
