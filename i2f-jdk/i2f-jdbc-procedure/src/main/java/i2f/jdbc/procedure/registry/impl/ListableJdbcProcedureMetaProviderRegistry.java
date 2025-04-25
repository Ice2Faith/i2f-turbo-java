package i2f.jdbc.procedure.registry.impl;

import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/3/15 15:21
 */
@Data
@NoArgsConstructor
public class ListableJdbcProcedureMetaProviderRegistry implements JdbcProcedureMetaProviderRegistry {
    protected final CopyOnWriteArrayList<JdbcProcedureMetaProvider> providers = new CopyOnWriteArrayList<>();
    protected volatile XProc4jEventHandler eventHandler = new XProc4jEventHandler();

    public ListableJdbcProcedureMetaProviderRegistry(JdbcProcedureMetaProvider... providers) {
        addProvider(providers);
    }

    public ListableJdbcProcedureMetaProviderRegistry(Collection<JdbcProcedureMetaProvider> providers) {
        addProvider(providers);
    }

    public void addProvider(JdbcProcedureMetaProvider... providers) {
        if (providers != null) {
            this.providers.addAll(Arrays.asList(providers));
        }
    }

    public void addProvider(Collection<JdbcProcedureMetaProvider> providers) {
        if (providers != null) {
            this.providers.addAll(providers);
        }
    }

    @Override
    public Collection<JdbcProcedureMetaProvider> getProviders() {
        return providers;
    }
}
