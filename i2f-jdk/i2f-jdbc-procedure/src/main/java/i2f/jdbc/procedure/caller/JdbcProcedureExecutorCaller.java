package i2f.jdbc.procedure.caller;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 11:02
 */
public interface JdbcProcedureExecutorCaller {

    <T> T invoke(String procedureId, Map<String, Object> params);

    void call(String procedureId, Map<String, Object> params);
}
