package i2f.springboot.dynamic.datasource.wrapper;

import i2f.springboot.dynamic.datasource.autoconfiguration.DynamicDataSourceConfig;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2026/10/12 10:19
 * @desc
 */
@ConditionalOnExpression("${" + DynamicDataSourceConfig.CONFIG_PREFIX + ".post-wrapper.enable:true}")
public class DefaultDataSourcePostWrapperAutoConfiguration {
    @ConditionalOnClass(DataSourceProxy.class)
    @Bean
    public SeataDataSourcePostWrapper seataDataSourcePostWrapper() {
        return new SeataDataSourcePostWrapper();
    }
}
