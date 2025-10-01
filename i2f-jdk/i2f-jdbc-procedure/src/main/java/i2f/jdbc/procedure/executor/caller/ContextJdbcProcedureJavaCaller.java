package i2f.jdbc.procedure.executor.caller;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/10/1 20:13
 * @desc
 */
public interface ContextJdbcProcedureJavaCaller {

    void init(JdbcProcedureExecutor executor, Map<String, Object> params);

    Object execute() throws Throwable;

    JdbcProcedureExecutor executor();

    Map<String, Object> params();

}
