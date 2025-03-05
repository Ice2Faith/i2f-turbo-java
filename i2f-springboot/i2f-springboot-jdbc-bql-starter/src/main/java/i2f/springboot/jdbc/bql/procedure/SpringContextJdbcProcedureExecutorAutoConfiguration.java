package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:05
 */
@Data
@ConditionalOnExpression("${jdbc.xml.procedure.enable:true}")
@Import({
        JdbcProcedureHelper.class
})
@EnableConfigurationProperties({
        SpringJdbcProcedureProperties.class
})
public class SpringContextJdbcProcedureExecutorAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private SpringJdbcProcedureProperties jdbcProcedureProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @ConditionalOnMissingBean(JdbcProcedureExecutor.class)
    @Bean
    public JdbcProcedureExecutor jdbcProcedureExecutor() {
        return new SpringContextJdbcProcedureExecutor(applicationContext);
    }

    @Bean
    public SpringJdbcProcedureNodeMapCacheSupplier springJdbcProcedureNodeMapCacheSupplier() {
        SpringJdbcProcedureNodeMapCacheSupplier ret = new SpringJdbcProcedureNodeMapCacheSupplier();
        String xmlLocations = jdbcProcedureProperties.getXmlLocations();
        if (xmlLocations == null) {
            xmlLocations = SpringJdbcProcedureProperties.DEFAULT_XML_LOCATIONS;
        }
        String[] arr = xmlLocations.split("[,;\n]");
        ret.getXmlLocations().addAll(Arrays.asList(arr));
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @Bean
    public SpringJdbcProcedureJavaCallerMapCacheSupplier springJdbcProcedureJavaCallerMapCacheSupplier() {
        return new SpringJdbcProcedureJavaCallerMapCacheSupplier(applicationContext);
    }


    @Bean
    public JdbcProcedureContext procedureContext(SpringJdbcProcedureNodeMapCacheSupplier nodeMapCacheSupplier,
                                                 SpringJdbcProcedureJavaCallerMapCacheSupplier javaCallerMapCacheSupplier) {
        return new JdbcProcedureContext(nodeMapCacheSupplier, javaCallerMapCacheSupplier);
    }

    @Bean
    public DefaultJdbcProcedureExecutorCaller defaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor,
                                                                                 JdbcProcedureContext procedureContext) {
        return new DefaultJdbcProcedureExecutorCaller(executor, procedureContext);
    }
}
