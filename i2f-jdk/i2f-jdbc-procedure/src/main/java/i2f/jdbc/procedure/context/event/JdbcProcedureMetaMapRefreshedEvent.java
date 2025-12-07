package i2f.jdbc.procedure.context.event;

import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.event.XProc4jEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2025/4/25 9:01
 */
@Data
@NoArgsConstructor
public class JdbcProcedureMetaMapRefreshedEvent implements XProc4jEvent {
    protected JdbcProcedureContext context;
    protected Set<String> effectKeys;
    protected Map<String, ProcedureMeta> metaMap;
}
