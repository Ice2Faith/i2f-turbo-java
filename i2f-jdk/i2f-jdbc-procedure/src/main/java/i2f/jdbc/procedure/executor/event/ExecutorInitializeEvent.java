package i2f.jdbc.procedure.executor.event;

import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/1/26 8:45
 * @desc
 */
@Data
@NoArgsConstructor
public class ExecutorInitializeEvent implements XProc4jEvent {
    protected JdbcProcedureExecutor executor;

    public ExecutorInitializeEvent(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }
}
