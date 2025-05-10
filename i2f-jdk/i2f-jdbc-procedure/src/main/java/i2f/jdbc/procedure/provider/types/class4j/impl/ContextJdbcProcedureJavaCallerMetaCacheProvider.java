package i2f.jdbc.procedure.provider.types.class4j.impl;

import i2f.context.std.IContext;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.context.CacheObjectRefresherSupplier;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.provider.types.class4j.JdbcProcedureJavaCallerMetaProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
public class ContextJdbcProcedureJavaCallerMetaCacheProvider
        extends CacheObjectRefresherSupplier<Map<String, JdbcProcedureJavaCaller>, ConcurrentHashMap<String, JdbcProcedureJavaCaller>>
        implements JdbcProcedureJavaCallerMetaProvider {
    protected volatile IContext applicationContext;
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();

    public ContextJdbcProcedureJavaCallerMetaCacheProvider(IContext applicationContext) {
        super(new ConcurrentHashMap<>(), XProc4jConsts.NAME + "-java-caller-refresher");
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, JdbcProcedureJavaCaller> getJavaCallerMap() {
        return get();
    }

    @Override
    public Map<String, JdbcProcedureJavaCaller> wrapGet(ConcurrentHashMap<String, JdbcProcedureJavaCaller> ret) {
        return new HashMap<>(ret);
    }

    @Override
    public boolean isMissingCache() {
        return cache == null || cache.isEmpty();
    }

    @Override
    public void refresh() {
        Map<String, JdbcProcedureJavaCaller> ret = new HashMap<>();
        if (applicationContext == null) {
            return;
        }
        List<JdbcProcedureJavaCaller> list = applicationContext.getBeans(JdbcProcedureJavaCaller.class);
        for (JdbcProcedureJavaCaller item : list) {
            ListableJdbcProcedureJavaCallerMetaProvider.addCaller(item, ret);
        }
        cache.putAll(ret);
    }
}
