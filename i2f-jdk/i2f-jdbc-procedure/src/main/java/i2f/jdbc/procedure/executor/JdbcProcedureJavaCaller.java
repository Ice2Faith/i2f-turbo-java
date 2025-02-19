package i2f.jdbc.procedure.executor;


import i2f.jdbc.procedure.context.ExecuteContext;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/10 16:54
 */
@FunctionalInterface
public interface JdbcProcedureJavaCaller {
    default JavaCallerHelper helper(ExecuteContext context,JdbcProcedureExecutor executor){
        return new JavaCallerHelper(context,executor);
    }

    Object exec(ExecuteContext context, JdbcProcedureExecutor executor, Map<String,Object> params) throws Throwable;
}
