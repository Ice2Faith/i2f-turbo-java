package i2f.springboot.jdbc.bql.procedure.impl;

import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.datasource.DataSourceProvider;
import i2f.spring.core.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/30 14:49
 */
public class SpringRoutingDataSourceProvider implements DataSourceProvider {
    public static final int ORDER = 100;
    protected INamingContext namingContext;

    public SpringRoutingDataSourceProvider(INamingContext namingContext) {
        this.namingContext = namingContext;
    }

    public SpringRoutingDataSourceProvider(ApplicationContext applicationContext) {
        this.namingContext = new SpringContext(applicationContext);
    }

    @Override
    public Map<String, DataSource> getDataSources() {
        Map<String, DataSource> ret = new HashMap<>();
        AbstractRoutingDataSource bean = namingContext.getBean(AbstractRoutingDataSource.class);
        if (bean != null) {
            Map<Object, DataSource> dataSources = bean.getResolvedDataSources();
            for (Map.Entry<Object, DataSource> entry : dataSources.entrySet()) {
                String name = String.valueOf(entry.getKey());
                if (name.toLowerCase().endsWith("datasource")) {
                    name = name.substring(0, name.length() - "datasource".length());
                }
                ret.put(name, entry.getValue());
            }

        }
        return ret;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
