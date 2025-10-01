package i2f.jdbc.procedure.executor.caller;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/10/1 20:40
 * @desc
 */
public abstract class AbstractContextJdbcProcedureJavaCaller implements DefaultContextJdbcProcedureJavaCaller {
    protected JdbcProcedureExecutor executor;
    protected Map<String, Object> params;

    @Override
    public void init(JdbcProcedureExecutor executor, Map<String, Object> params) {
        this.executor = executor;
        this.params = params;
    }

    @Override
    public JdbcProcedureExecutor executor() {
        return executor;
    }

    @Override
    public Map<String, Object> params() {
        return params;
    }
}
