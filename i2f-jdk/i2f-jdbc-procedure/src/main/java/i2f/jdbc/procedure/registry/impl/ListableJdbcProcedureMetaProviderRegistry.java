package i2f.jdbc.procedure.registry.impl;

import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/3/15 15:21
 */
public class ListableJdbcProcedureMetaProviderRegistry implements JdbcProcedureMetaProviderRegistry {
    protected final CopyOnWriteArrayList<JdbcProcedureMetaProvider> providers=new CopyOnWriteArrayList<>();

    public ListableJdbcProcedureMetaProviderRegistry(JdbcProcedureMetaProvider ... providers) {
        addProvider(providers);
    }

    public ListableJdbcProcedureMetaProviderRegistry(Collection<JdbcProcedureMetaProvider> providers) {
        addProvider(providers);
    }

    public void addProvider(JdbcProcedureMetaProvider ... providers){
        if(providers!=null){
            this.providers.addAll(Arrays.asList(providers));
        }
    }

    public void addProvider(Collection<JdbcProcedureMetaProvider>  providers){
        if(providers!=null){
            this.providers.addAll(providers);
        }
    }

    @Override
    public Collection<JdbcProcedureMetaProvider> getProviders() {
        return providers;
    }
}
