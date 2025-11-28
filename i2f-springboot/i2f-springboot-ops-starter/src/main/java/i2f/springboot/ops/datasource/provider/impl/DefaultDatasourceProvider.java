package i2f.springboot.ops.datasource.provider.impl;

import i2f.springboot.ops.datasource.provider.DatasourceCollector;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/1 21:48
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class DefaultDatasourceProvider implements DatasourceProvider {
    public static final String DEFAULT_DATASOURCE_NAME = "primary";

    @Autowired
    protected ApplicationContext applicationContext;
    protected final ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    protected final AtomicReference<String> defaultDataSourceName = new AtomicReference<>();

    @Override
    public Map<String, DataSource> getDatasourceMap() {
        initialize();
        return dataSourceMap;
    }

    @Override
    public String getDefaultDataSourceName() {
        initialize();
        return defaultDataSourceName.get();
    }

    public void initialize() {
        if (defaultDataSourceName.get() != null) {
            return;
        }
        refresh();
    }

    public void refresh() {
        try {
            String[] names = applicationContext.getBeanNamesForType(DatasourceCollector.class);
            for (String name : names) {
                try {
                    DatasourceCollector collector = applicationContext.getBean(name, DatasourceCollector.class);
                    Map<String, DataSource> map = collector.collect();
                    dataSourceMap.putAll(map);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }

        defaultDataSourceName.set(detectPrimaryDatasource(dataSourceMap));
        try {
            dataSourceMap.put(DEFAULT_DATASOURCE_NAME, dataSourceMap.get(defaultDataSourceName.get()));
        } catch (Exception e) {

        }
    }

    public String detectPrimaryDatasource(Map<String, DataSource> ret) {
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        DataSource primary = ret.get(DEFAULT_DATASOURCE_NAME);
        if (primary != null) {
            return DEFAULT_DATASOURCE_NAME;
        }
        if (ret.isEmpty()) {
            return null;
        }
        String first = null;
        List<String> defaultNames = Arrays.asList("primary", "master", "main", "default", "leader");
        for (Map.Entry<String, DataSource> entry : ret.entrySet()) {
            if (first == null) {
                first = entry.getKey();
            }
            String name = entry.getKey();
            if (defaultNames.contains(name)) {
                return name;
            }
            name = name.toLowerCase();
            if (defaultNames.contains(name)) {
                return name;
            }
        }

        return first;
    }
}
