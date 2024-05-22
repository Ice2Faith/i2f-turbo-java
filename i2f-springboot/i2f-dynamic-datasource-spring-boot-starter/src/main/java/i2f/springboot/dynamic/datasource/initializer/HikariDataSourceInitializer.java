package i2f.springboot.dynamic.datasource.initializer;

import com.zaxxer.hikari.HikariDataSource;
import i2f.springboot.dynamic.datasource.autoconfiguration.DataSourceMeta;
import i2f.springboot.dynamic.datasource.core.DataSourceInitializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;

import javax.sql.DataSource;

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
    public boolean accept(String dataSourceId, DataSourceMeta dataSourceMeta) {
        return "com.zaxxer.hikari.HikariDataSource".equals(dataSourceMeta.getType());
    }

    @Override
    public DataSource initial(String dataSourceId, DataSourceMeta dataSourceMeta) {
        HikariConfigWrapper wrapper = new HikariConfigWrapper();
        wrapper.setJdbcUrl(dataSourceMeta.getUrl());
        wrapper.setDriverClassName(dataSourceMeta.getDriver());
        wrapper.setUsername(dataSourceMeta.getUsername());
        wrapper.setPassword(dataSourceMeta.getPassword());
        propertiesBindingPostProcessor.postProcessBeforeInitialization(wrapper, dataSourceId);
        return wrapper;
    }
}
