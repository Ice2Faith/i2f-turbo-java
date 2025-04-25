package i2f.jdbc.procedure.provider.event;

import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/4/25 9:58
 */
@Data
@NoArgsConstructor
public class ProcedureMetaProviderNotifyEvent implements XProc4jEvent {
    protected JdbcProcedureMetaProvider provider;
}
