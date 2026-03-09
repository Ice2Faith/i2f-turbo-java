package i2f.jdbc.procedure.executor.event;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2026/2/6 15:57
 * @desc
 */
@Data
@NoArgsConstructor
public class SqlActionHookEvent implements XProc4jEvent {
    protected JdbcProcedureExecutor executor;
    protected Connection connection;
    protected String datasource;
    protected SqlActionType type;
    protected BindSql bql;
}
