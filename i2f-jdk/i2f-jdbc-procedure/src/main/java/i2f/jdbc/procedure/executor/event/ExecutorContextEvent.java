package i2f.jdbc.procedure.executor.event;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/17 14:10
 */
@Data
@NoArgsConstructor
public class ExecutorContextEvent implements XProc4jEvent {
    protected Map<String, Object> context;
    protected JdbcProcedureExecutor executor;
    protected XmlNode node= ContextHolder.TRACE_NODE.get();
}
