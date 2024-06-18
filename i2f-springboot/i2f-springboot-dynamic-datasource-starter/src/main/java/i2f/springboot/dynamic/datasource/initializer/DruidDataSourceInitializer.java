package i2f.springboot.dynamic.datasource.initializer;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import i2f.springboot.dynamic.datasource.autoconfiguration.DataSourceMeta;
import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;

import javax.sql.DataSource;

/**
 * @author Ice2Faith
 * @date 2024/5/20 17:36
 * @desc
 */
@Data
public class DruidDataSourceInitializer implements DataSourceInitializer {

    private ConfigurationPropertiesBindingPostProcessor propertiesBindingPostProcessor;

    public DruidDataSourceInitializer(ConfigurationPropertiesBindingPostProcessor propertiesBindingPostProcessor) {
        this.propertiesBindingPostProcessor = propertiesBindingPostProcessor;
    }

    @Override
    public boolean accept(String dataSourceId, DataSourceMeta dataSourceMeta) {
        return "com.alibaba.druid.pool.DruidDataSource".equals(dataSourceMeta.getType());
    }

    @Override
    public DataSource initial(String dataSourceId, DataSourceMeta dataSourceMeta) {
        DruidDataSourceWrapper wrapper = new DruidDataSourceWrapper();
        wrapper.setUrl(dataSourceMeta.getUrl());
        wrapper.setDriverClassName(dataSourceMeta.getDriver());
        wrapper.setUsername(dataSourceMeta.getUsername());
        wrapper.setPassword(dataSourceMeta.getPassword());
        propertiesBindingPostProcessor.postProcessBeforeInitialization(wrapper, dataSourceId);
        try {
            wrapper.init();
        } catch (Exception e) {
            throw new IllegalStateException("initialize datasource[" + dataSourceId + "] error: " + e.getMessage(), e);
        }
        return wrapper;
    }
}
