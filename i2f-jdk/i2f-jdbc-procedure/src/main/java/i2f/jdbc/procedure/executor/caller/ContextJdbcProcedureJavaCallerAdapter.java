package i2f.jdbc.procedure.executor.caller;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/10/1 21:35
 * @desc
 */
public class ContextJdbcProcedureJavaCallerAdapter implements JdbcProcedureJavaCaller {
    protected Supplier<ContextJdbcProcedureJavaCaller> supplier;

    public ContextJdbcProcedureJavaCallerAdapter(Supplier<ContextJdbcProcedureJavaCaller> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        ContextJdbcProcedureJavaCaller caller = supplier.get();
        caller.init(executor, params);
        return caller.execute();
    }
}
