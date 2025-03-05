package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.impl.ListableJdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.context.CacheObjectRefresherSupplier;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
@Slf4j
public class SpringJdbcProcedureJavaCallerMapCacheSupplier
        extends CacheObjectRefresherSupplier<Map<String, JdbcProcedureJavaCaller>, ConcurrentHashMap<String, JdbcProcedureJavaCaller>>
        implements JdbcProcedureJavaCallerMapSupplier {
    protected ApplicationContext applicationContext;

    public SpringJdbcProcedureJavaCallerMapCacheSupplier(ApplicationContext applicationContext) {
        super(new ConcurrentHashMap<>(), "xproc4j-java-caller-refresher");
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
        Map<String, JdbcProcedureJavaCaller> beanMap = applicationContext.getBeansOfType(JdbcProcedureJavaCaller.class);
        for (Map.Entry<String, JdbcProcedureJavaCaller> entry : beanMap.entrySet()) {
            ListableJdbcProcedureJavaCallerMapSupplier.addCaller(entry.getValue(), ret);
        }
        cache.putAll(ret);
    }
}
