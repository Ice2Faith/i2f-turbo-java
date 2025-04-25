package i2f.jdbc.procedure.context.impl;

import i2f.jdbc.procedure.context.CacheObjectRefresherSupplier;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.context.event.JdbcProcedureMetaMapRefreshedEvent;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import i2f.jdbc.procedure.registry.impl.ListableJdbcProcedureMetaProviderRegistry;
import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:50
 * @desc
 */
@Data
public class DefaultJdbcProcedureContext
        extends CacheObjectRefresherSupplier<Map<String, ProcedureMeta>, ConcurrentHashMap<String, ProcedureMeta>>
        implements JdbcProcedureContext {
    protected volatile JdbcProcedureMetaProviderRegistry registry = new ListableJdbcProcedureMetaProviderRegistry();
    protected XProc4jEventHandler eventHandler = new XProc4jEventHandler();

    public DefaultJdbcProcedureContext() {
        super(new ConcurrentHashMap<>(), "procedure-context-refresher");
    }

    public DefaultJdbcProcedureContext(Map<String, ProcedureMeta> map) {
        this();
        registry(map);
    }

    public DefaultJdbcProcedureContext(JdbcProcedureMetaProviderRegistry registry) {
        this();
        this.registry = registry;
    }

    @Override
    public void registry(String name, ProcedureMeta meta) {
        if (meta == null) {
            return;
        }
        if (name == null) {
            name = meta.getName();
        }
        if (name == null || name.isEmpty()) {
            return;
        }
        cache.put(name, meta);
    }

    @Override
    public void remove(String name) {
        if (name == null) {
            return;
        }
        cache.remove(name);
    }

    @Override
    public Map<String, ProcedureMeta> getMetaMap() {
        return get();
    }


    @Override
    public boolean isMissingCache() {
        return cache == null || cache.isEmpty();
    }

    @Override
    public Map<String, ProcedureMeta> wrapGet(ConcurrentHashMap<String, ProcedureMeta> ret) {
        return new HashMap<>(getCache());
    }


    @Override
    public void refresh() {
        Map<String, ProcedureMeta> metaMap = new HashMap<>();
        if (registry == null) {
            return;
        }
        Collection<JdbcProcedureMetaProvider> metaProviders = registry.getProviders();
        if (metaProviders == null || metaProviders.isEmpty()) {
            return;
        }
        for (JdbcProcedureMetaProvider provider : metaProviders) {
            Map<String, ProcedureMeta> map = provider.getMetaMap();
            if (map != null) {
                metaMap.putAll(map);
            }
        }

        cache.putAll(metaMap);

        JdbcProcedureMetaMapRefreshedEvent event = new JdbcProcedureMetaMapRefreshedEvent();
        event.setContext(this);
        event.setMetaMap(metaMap);
        eventHandler.publish(event);

    }

}
