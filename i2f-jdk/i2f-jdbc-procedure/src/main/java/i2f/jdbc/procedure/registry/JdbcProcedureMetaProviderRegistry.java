package i2f.jdbc.procedure.registry;


import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2025/3/15 14:53
 */
@FunctionalInterface
public interface JdbcProcedureMetaProviderRegistry {
   Collection<JdbcProcedureMetaProvider> getProviders();
}
