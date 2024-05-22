package i2f.springboot.dynamic.datasource.autoconfiguration;

import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import i2f.springboot.dynamic.datasource.core.DynamicDataSource;
import i2f.springboot.dynamic.datasource.core.DynamicDataSourceContextHolder;
import i2f.springboot.dynamic.datasource.initializer.DefaultDataSourceInitializerConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2022/3/21 10:24
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${" + DynamicDataSourceConfig.CONFIG_PREFIX + ".enable:true}")
@AutoConfigureBefore({
        DataSourceAutoConfiguration.class
})
@AutoConfigureOrder(-1)
@AutoConfigureAfter({
        DynamicDataSourceProperty.class,
        DefaultDataSourceInitializerConfiguration.class
})
public class DynamicDataSourceConfig implements BeanFactoryAware, ApplicationContextAware {

    public static final String CONFIG_PREFIX = "i2f.springboot.dynamic.datasource";

    public static final String PACKAGE_PREFIX = "i2f.springboot.dynamic.datasource";

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private DynamicDataSourceProperty dynamicDataSourceProperty;

    @Autowired
    private Environment environment;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("datasource config....");
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        /*获取yml所有数据源配置*/
        Map<String, DataSourceMeta> datasource = dynamicDataSourceProperty.getDatasource();
        Map<Object, Object> dataSourceMap = new HashMap<>(5);
        Map<String, List<String>> groupMap = new LinkedHashMap<>();
        log.info("datasource finds:" + datasource.keySet());

        Map<String, DataSourceInitializer> initializerMap = context.getBeansOfType(DataSourceInitializer.class);


        for (Map.Entry<String, DataSourceMeta> entry : datasource.entrySet()) {
            log.info("datasource construct...");
            //创建数据源对象
            String dataSourceId = DynamicDataSourceContextHolder.realDataSourceTypeName(entry.getKey());
            DataSource dataSource = configDataSource(initializerMap, dataSourceId, entry.getValue());
            log.info("datasource registry:" + dataSourceId);
            /*bean工厂注册每个数据源bean*/
            listableBeanFactory.registerSingleton(dataSourceId, dataSource);
            dataSourceMap.put(dataSourceId, dataSource);
            String group = entry.getValue().getGroup();
            if (group == null) {
                group = "ungroup";
            }
            String[] arr = group.split("\\s*,\\s*");
            boolean isUsed = false;
            for (String item : arr) {
                String k = item.trim();
                if ("".equals(k)) {
                    continue;
                }
                if (!groupMap.containsKey(k)) {
                    groupMap.put(k, new ArrayList<>());
                }
                if (!groupMap.get(k).contains(entry.getKey())) {
                    groupMap.get(k).add(entry.getKey());
                }
                isUsed = true;
            }
            if (!isUsed) {
                String k = "ungroup";
                if (!groupMap.containsKey(k)) {
                    groupMap.put(k, new ArrayList<>());
                }
                if (!groupMap.get(k).contains(entry.getKey())) {
                    groupMap.get(k).add(entry.getKey());
                }
            }
        }

        log.info("DynamicDataSourceConfig DataSource config done.");


        //AbstractRoutingDataSource设置主从数据源
        return new DynamicDataSource(beanFactory.getBean(DynamicDataSourceContextHolder.realDataSourceTypeName(dynamicDataSourceProperty.getPrimary()), DataSource.class),
                dataSourceMap,
                dynamicDataSourceProperty,
                groupMap);
    }

    private DataSource configDataSource(Map<String, DataSourceInitializer> initializerMap,
                                        String dataSourceId,
                                        DataSourceMeta dataSourceMeta) {
        if (dataSourceMeta != null) {
            String className = dataSourceMeta.getType();
            if (className == null) {
                className = "";
            }
            if ("".equals(className)) {
                className = environment.getProperty("spring.datasource.type");
                if (className == null) {
                    className = "";
                }
            }
            if ("".equals(className)) {
                dataSourceMeta.setType(className);
            }
        }


        for (Map.Entry<String, DataSourceInitializer> entry : initializerMap.entrySet()) {
            DataSourceInitializer initializer = entry.getValue();
            if (initializer.accept(dataSourceId, dataSourceMeta)) {
                return initializer.initial(dataSourceId, dataSourceMeta);
            }
        }

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        try {
            String className = dataSourceMeta.getType();
            if (className != null && !"".equals(className)) {
                Class<? extends DataSource> clazz = (Class<? extends DataSource>) Class.forName(className);
                if (clazz != null) {
                    builder.type(clazz);
                }
            }
        } catch (Exception e) {

        }
        builder.url(dataSourceMeta.getUrl());
        builder.driverClassName(dataSourceMeta.getDriver());
        builder.username(dataSourceMeta.getUsername());
        builder.password(dataSourceMeta.getPassword());

        return builder.build();
    }
}
