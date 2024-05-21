package i2f.springboot.dynamic.datasource.initializer;

import com.zaxxer.hikari.HikariDataSource;
import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/20 17:36
 * @desc
 */
@Data
public class HikariDataSourceInitializer implements DataSourceInitializer {

    @Autowired
    private ConfigurationPropertiesBindingPostProcessor propertiesBindingPostProcessor;

    public HikariDataSourceInitializer(ConfigurationPropertiesBindingPostProcessor propertiesBindingPostProcessor) {
        this.propertiesBindingPostProcessor = propertiesBindingPostProcessor;
    }

    @ConfigurationProperties("spring.datasource.hikari")
    public static class HikariConfigWrapper extends HikariDataSource {

    }

    @Override
    public DataSource initial(String dataSourceId, Map<String, Object> dataSourceConfig) {
        HikariConfigWrapper wrapper = new HikariConfigWrapper();
        wrapper.setJdbcUrl(String.valueOf(dataSourceConfig.get("url")));
        wrapper.setDriverClassName(String.valueOf(dataSourceConfig.get("driver")));
        wrapper.setUsername(String.valueOf(dataSourceConfig.get("username")));
        wrapper.setPassword(String.valueOf(dataSourceConfig.get("password")));
        propertiesBindingPostProcessor.postProcessBeforeInitialization(wrapper, dataSourceId);
        return wrapper;
    }
}
