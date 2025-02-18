package i2f.jdbc.procedure.caller;

import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:06
 * @desc
 */
@FunctionalInterface
public interface JdbcProcedureJavaCallerMapSupplier {
    Map<String, JdbcProcedureJavaCaller> getJavaCallerMap();

    default JdbcProcedureJavaCaller getJavaCaller(String procedureId) {
        return getJavaCallerMap().get(procedureId);
    }

}
