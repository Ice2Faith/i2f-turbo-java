package i2f.springboot.dynamic.datasource.initializer;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.zaxxer.hikari.HikariDataSource;
import i2f.springboot.dynamic.datasource.autoconfiguration.DynamicDataSourceConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/5/20 18:10
 * @desc
 */
@ConditionalOnExpression("${" + DynamicDataSourceConfig.CONFIG_PREFIX + ".initializer.enable:true}")
public class DefaultDataSourceInitializerConfiguration {

    @ConditionalOnClass(DruidDataSourceWrapper.class)
    @Bean
    public DruidDataSourceInitializer druidDataSourceInitializer(ConfigurationPropertiesBindingPostProcessor postProcessor) {
        return new DruidDataSourceInitializer(postProcessor);
    }

    @ConditionalOnClass(HikariDataSource.class)
    @Bean
    public HikariDataSourceInitializer hikariDataSourceInitializer(ConfigurationPropertiesBindingPostProcessor postProcessor) {
        return new HikariDataSourceInitializer(postProcessor);
    }

}
