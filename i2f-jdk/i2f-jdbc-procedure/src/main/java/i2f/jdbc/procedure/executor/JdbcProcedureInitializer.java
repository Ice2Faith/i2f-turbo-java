package i2f.jdbc.procedure.executor;

/**
 * @author Ice2Faith
 * @date 2026/1/26 8:43
 * @desc
 */
@FunctionalInterface
public interface JdbcProcedureInitializer {
    void initialize(JdbcProcedureExecutor executor);
}
