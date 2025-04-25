package i2f.jdbc.procedure.executor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/10 16:54
 */
@FunctionalInterface
public interface JdbcProcedureJavaCaller {

    Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable;
}
