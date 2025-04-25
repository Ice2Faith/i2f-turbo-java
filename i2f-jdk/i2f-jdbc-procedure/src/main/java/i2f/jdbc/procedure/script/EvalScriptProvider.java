package i2f.jdbc.procedure.script;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/7 17:13
 */
public interface EvalScriptProvider {
    boolean support(String lang);

    Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor);
}
