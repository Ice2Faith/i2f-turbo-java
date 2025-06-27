package i2f.jdbc.procedure.provider.event;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/25 9:58
 */
@Data
@NoArgsConstructor
public class ProcedureMetaProviderChangeEvent implements XProc4jEvent {
    protected JdbcProcedureMetaProvider provider;
    protected Map<String, ProcedureMeta> metaMap;
}
