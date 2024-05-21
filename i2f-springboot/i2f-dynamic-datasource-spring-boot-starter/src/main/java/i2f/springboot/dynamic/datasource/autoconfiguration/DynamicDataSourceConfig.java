package i2f.springboot.dynamic.datasource.autoconfiguration;

import i2f.springboot.dynamic.datasource.aop.DataSourceType;
import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import i2f.springboot.dynamic.datasource.core.DynamicDataSource;
import i2f.springboot.dynamic.datasource.core.DynamicDataSourceContextHolder;
import i2f.springboot.dynamic.datasource.initializer.DefaultDataSourceInitializerConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
public class DynamicDataSourceConfig implements BeanFactoryAware {

    public static final String CONFIG_PREFIX = "i2f.springboot.dynamic.datasource";

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private DynamicDataSourceProperty dynamicDataSourceProperty;

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private DataSourceInitializer dataSourceInitializer;


    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("datasource config....");
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        /*获取yml所有数据源配置*/
        Map<String, Map<String, Object>> datasource = dynamicDataSourceProperty.getDatasource();
        Map<Object, Object> dataSourceMap = new HashMap<>(5);
        log.info("datasource finds:" + datasource);
        Optional.ofNullable(datasource).ifPresent(map -> {
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                log.info("datasource construct...");
                //创建数据源对象
                String dataSourceId = DynamicDataSourceContextHolder.realDataSourceTypeName(entry.getKey());
                DataSource dataSource = configDataSource(dataSourceId, entry.getValue());
                log.info("datasource registry:" + dataSourceId);
                /*bean工厂注册每个数据源bean*/
                listableBeanFactory.registerSingleton(dataSourceId, dataSource);
                dataSourceMap.put(dataSourceId, dataSource);
            }
        });
        log.info("DynamicDataSourceConfig DataSource config done.");
        //AbstractRoutingDataSource设置主从数据源
        return new DynamicDataSource(beanFactory.getBean(DataSourceType.MASTER + "DataSource", DataSource.class), dataSourceMap);
    }

    private DataSource configDataSource(String dataSourceId, Map<String, Object> dataSourceConfig) {
        if (dataSourceInitializer != null) {
            return dataSourceInitializer.initial(dataSourceId, dataSourceConfig);
        }

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        try {
            String className = environment.getProperty("spring.datasource.type");
            Class<? extends DataSource> clazz = (Class<? extends DataSource>) Class.forName(className);
            if (clazz != null) {
                builder.type(clazz);
            }
        } catch (Exception e) {

        }
        builder.url(String.valueOf(dataSourceConfig.get("url")));
        builder.driverClassName(String.valueOf(dataSourceConfig.get("driver")));
        builder.username(String.valueOf(dataSourceConfig.get("username")));
        builder.password(String.valueOf(dataSourceConfig.get("password")));

        return builder.build();
    }
}
