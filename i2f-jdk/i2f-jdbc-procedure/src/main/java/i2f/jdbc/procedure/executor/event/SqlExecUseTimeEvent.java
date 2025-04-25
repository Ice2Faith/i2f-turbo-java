package i2f.jdbc.procedure.executor.event;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/4/22 9:01
 */
@Data
@NoArgsConstructor
public class SqlExecUseTimeEvent implements XProc4jEvent {
    protected JdbcProcedureExecutor executor;
    protected long useMillsSeconds;
    protected BindSql bql;
    protected String location;
}
