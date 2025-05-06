package i2f.jdbc.procedure.registry.impl;

import i2f.context.std.IContext;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/3/15 15:23
 */
@Data
@NoArgsConstructor
public class ContextJdbcProcedureMetaProviderRegistry implements JdbcProcedureMetaProviderRegistry {
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();
    private volatile IContext applicationContext;

    public ContextJdbcProcedureMetaProviderRegistry(IContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Collection<JdbcProcedureMetaProvider> getProviders() {
        List<JdbcProcedureMetaProvider> ret = new ArrayList<>();
        if (applicationContext == null) {
            return ret;
        }
        List<JdbcProcedureMetaProvider> list = applicationContext.getBeans(JdbcProcedureMetaProvider.class);
        ret.addAll(list);
        return ret;
    }
}
