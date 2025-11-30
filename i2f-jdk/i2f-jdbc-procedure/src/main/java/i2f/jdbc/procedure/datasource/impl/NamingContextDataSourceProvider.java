package i2f.jdbc.procedure.datasource.impl;

import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.datasource.DataSourceProvider;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/30 14:38
 */
public class NamingContextDataSourceProvider implements DataSourceProvider {
    protected INamingContext namingContext;

    public NamingContextDataSourceProvider(INamingContext namingContext) {
        this.namingContext = namingContext;
    }

    @Override
    public Map<String, DataSource> getDataSources() {
        Map<String, DataSource> ret = new HashMap<>();
        Map<String, DataSource> dataSources = namingContext.getBeansMap(DataSource.class);
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String name = entry.getKey();
            if (name.toLowerCase().endsWith("datasource")) {
                name = name.substring(0, name.length() - "datasource".length());
            }
            ret.put(name, entry.getValue());
        }
        return ret;
    }
}
