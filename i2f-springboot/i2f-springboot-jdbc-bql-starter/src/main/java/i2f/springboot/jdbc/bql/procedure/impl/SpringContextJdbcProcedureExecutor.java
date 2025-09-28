package i2f.springboot.jdbc.bql.procedure.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import i2f.context.std.INamingContext;
import i2f.environment.std.IEnvironment;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.spring.core.SpringContext;
import i2f.spring.enviroment.SpringEnvironment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/8 9:50
 */
@Data
@NoArgsConstructor
public class SpringContextJdbcProcedureExecutor extends DefaultJdbcProcedureExecutor {
    protected static final Logger log = LoggerFactory.getLogger(SpringContextJdbcProcedureExecutor.class);


    public SpringContextJdbcProcedureExecutor(JdbcProcedureContext context, ApplicationContext applicationContext) {
        super(context, new SpringEnvironment(applicationContext.getEnvironment()), new SpringContext(applicationContext));
    }

    public SpringContextJdbcProcedureExecutor(JdbcProcedureContext context, ApplicationContext applicationContext, Environment environment) {
        super(context, new SpringEnvironment(environment), new SpringContext(applicationContext));
    }

    public SpringContextJdbcProcedureExecutor(JdbcProcedureContext context, IEnvironment environment, INamingContext namingContext) {
        super(context, environment, namingContext);
    }

    @Override
    public Map<String, DataSource> getDatasourceMap() {
        try {
            AbstractRoutingDataSource bean = getNamingContext().getBean(AbstractRoutingDataSource.class);
            if (bean != null) {
                Map<String, DataSource> ret = new HashMap<>();
                Map<Object, DataSource> dataSources = bean.getResolvedDataSources();
                for (Map.Entry<Object, DataSource> entry : dataSources.entrySet()) {
                    String name = String.valueOf(entry.getKey());
                    if (name.toLowerCase().endsWith("datasource")) {
                        name = name.substring(0, name.length() - "datasource".length());
                    }
                    ret.put(name, entry.getValue());
                }
                detectPrimaryDatasource(ret);
                return ret;
            }
        } catch (Exception e) {
        }
        try {
            DynamicRoutingDataSource bean = getNamingContext().getBean(DynamicRoutingDataSource.class);
            Map<String, DataSource> ret = bean.getDataSources();
            detectPrimaryDatasource(ret);
            return ret;
        } catch (Exception e) {
        }
        return super.getDatasourceMap();
    }


}
