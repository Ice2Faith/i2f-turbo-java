package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.impl.ListableJdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
@Slf4j
public class SpringJdbcProcedureJavaCallerMapCacheSupplier implements JdbcProcedureJavaCallerMapSupplier {

    protected ApplicationContext applicationContext;

    public SpringJdbcProcedureJavaCallerMapCacheSupplier(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, JdbcProcedureJavaCaller> getJavaCallerMap() {
        Map<String, JdbcProcedureJavaCaller> ret = new HashMap<>();
        if (applicationContext == null) {
            return ret;
        }
        Map<String, JdbcProcedureJavaCaller> beanMap = applicationContext.getBeansOfType(JdbcProcedureJavaCaller.class);
        for (Map.Entry<String, JdbcProcedureJavaCaller> entry : beanMap.entrySet()) {
            ListableJdbcProcedureJavaCallerMapSupplier.addCaller(entry.getValue(), ret);
        }
        return ret;
    }
}
