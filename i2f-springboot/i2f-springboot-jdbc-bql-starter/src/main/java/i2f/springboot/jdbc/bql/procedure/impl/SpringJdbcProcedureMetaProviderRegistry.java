package i2f.springboot.jdbc.bql.procedure.impl;

import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/3/15 15:23
 */
@Data
@NoArgsConstructor
public class SpringJdbcProcedureMetaProviderRegistry implements JdbcProcedureMetaProviderRegistry {
    private volatile ApplicationContext applicationContext;

    public SpringJdbcProcedureMetaProviderRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Collection<JdbcProcedureMetaProvider> getProviders() {
        List<JdbcProcedureMetaProvider> ret=new ArrayList<>();
        if(applicationContext==null){
            return ret;
        }
        Map<String, JdbcProcedureMetaProvider> map = applicationContext.getBeansOfType(JdbcProcedureMetaProvider.class);
        for (Map.Entry<String, JdbcProcedureMetaProvider> entry : map.entrySet()) {
            ret.add(entry.getValue());
        }
        return ret;
    }
}
