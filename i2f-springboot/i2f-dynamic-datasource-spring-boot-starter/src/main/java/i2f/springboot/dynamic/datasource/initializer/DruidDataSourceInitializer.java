package i2f.springboot.dynamic.datasource.initializer;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;

import javax.sql.DataSource;
import java.util.Map;

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
    public DataSource initial(String dataSourceId, Map<String, Object> dataSourceConfig) {
        DruidDataSourceWrapper wrapper = new DruidDataSourceWrapper();
        wrapper.setUrl(String.valueOf(dataSourceConfig.get("url")));
        wrapper.setDriverClassName(String.valueOf(dataSourceConfig.get("driver")));
        wrapper.setUsername(String.valueOf(dataSourceConfig.get("username")));
        wrapper.setPassword(String.valueOf(dataSourceConfig.get("password")));
        propertiesBindingPostProcessor.postProcessBeforeInitialization(wrapper, dataSourceId);
        try {
            wrapper.init();
        } catch (Exception e) {
            throw new IllegalStateException("initialize datasource[" + dataSourceId + "] error: " + e.getMessage(), e);
        }
        return wrapper;
    }
}
