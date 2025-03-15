package i2f.jdbc.procedure.context;

import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/3/14 21:05
 */
@FunctionalInterface
public interface JdbcProcedureContextRefreshListener extends Consumer<JdbcProcedureContext> {

}
