package i2f.jdbc.procedure.executor.event;

import i2f.bindsql.BindSql;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/4/22 9:01
 */
@Data
@NoArgsConstructor
public class SqlExecUseTimeEvent extends ExecutorContextEvent {
    protected long useMillsSeconds;
    protected BindSql bql;
    protected String location;
    protected String datasource;
    protected long effectCount=-1;
    protected Throwable ex;
}
