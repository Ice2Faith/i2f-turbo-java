package i2f.springboot.ops.datasource.provider.impl;

import i2f.springboot.ops.datasource.provider.DatasourceCollector;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/28 11:18
 */
@ConditionalOnClass(AbstractRoutingDataSource.class)
@Data
@NoArgsConstructor
@Component
public class DefaultDatasourceCollector implements DatasourceCollector {
    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public Map<String, DataSource> collect() {
        Map<String, DataSource> ret = new LinkedHashMap<>();
        try {
            AbstractRoutingDataSource bean = applicationContext.getBean(AbstractRoutingDataSource.class);
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
        } catch (Exception e) {
        }
        try {
            String[] beanNames = applicationContext.getBeanNamesForType(DataSource.class);
            for (String beanName : beanNames) {
                DataSource bean = applicationContext.getBean(beanName, DataSource.class);
                String name = beanName;
                if (name.toLowerCase().endsWith("datasource")) {
                    name = name.substring(0, name.length() - "datasource".length());
                }
                if (name.isEmpty()) {
                    name = beanName;
                }
                ret.put(name, bean);
            }
        } catch (Exception e) {

        }
        return ret;
    }
}
