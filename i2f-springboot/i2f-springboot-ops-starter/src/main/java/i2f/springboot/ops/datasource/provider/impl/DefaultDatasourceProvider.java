package i2f.springboot.ops.datasource.provider.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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
    public static final String DEFAULT_DATASOURCE_NAME="primary";

    @Autowired
    protected ApplicationContext applicationContext;
    protected final ConcurrentHashMap<String,DataSource> dataSourceMap = new ConcurrentHashMap<>();
    protected final AtomicReference<String> defaultDataSourceName=new AtomicReference<>();

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
        if(defaultDataSourceName.get()!=null){
            return;
        }
        refresh();
    }

    public void refresh(){
        try {
            AbstractRoutingDataSource bean = applicationContext.getBean(AbstractRoutingDataSource.class);
            if (bean != null) {
                Map<Object, DataSource> dataSources = bean.getResolvedDataSources();
                for (Map.Entry<Object, DataSource> entry : dataSources.entrySet()) {
                    String name = String.valueOf(entry.getKey());
                    if (name.toLowerCase().endsWith("datasource")) {
                        name = name.substring(0, name.length() - "datasource".length());
                    }
                    dataSourceMap.put(name, entry.getValue());
                }
                defaultDataSourceName.set(detectPrimaryDatasource(dataSourceMap));
            }
        } catch (Exception e) {
        }
        try {
            DynamicRoutingDataSource bean = applicationContext.getBean(DynamicRoutingDataSource.class);
            Map<String, DataSource> ret = bean.getDataSources();
            dataSourceMap.putAll(ret);
            defaultDataSourceName.set(detectPrimaryDatasource(dataSourceMap));
        } catch (Exception e) {
        }
        try {
            Map<String, DataSource> ret = new HashMap<>();
            String[] beanNames= applicationContext.getBeanNamesForType(DataSource.class);
            for (String beanName : beanNames) {
                DataSource bean = applicationContext.getBean(beanName, DataSource.class);
                String name=beanName;
                if (name.toLowerCase().endsWith("datasource")) {
                    name = name.substring(0, name.length() - "datasource".length());
                }
                if(name.isEmpty()){
                    name=beanName;
                }
                dataSourceMap.put(name, bean);
            }
            defaultDataSourceName.set(detectPrimaryDatasource(dataSourceMap));
        } catch (Exception e) {

        }
    }

    public String detectPrimaryDatasource(Map<String, DataSource> ret) {
        if (ret==null || ret.isEmpty()) {
            return null;
        }
        DataSource primary = ret.get(DEFAULT_DATASOURCE_NAME);
        if (primary != null) {
            return DEFAULT_DATASOURCE_NAME;
        }
        if(ret.isEmpty()){
            return null;
        }
        String first=null;
        List<String> defaultNames = Arrays.asList("primary", "master", "main", "default", "leader");
        for (Map.Entry<String, DataSource> entry : ret.entrySet()) {
            if(first==null){
                first=entry.getKey();
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
