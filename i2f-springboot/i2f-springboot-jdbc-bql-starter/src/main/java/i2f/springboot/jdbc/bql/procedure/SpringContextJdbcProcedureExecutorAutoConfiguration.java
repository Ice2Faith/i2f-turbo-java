package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.JdbcProcedureContextRefreshListener;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContextRefreshListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.provider.types.class4j.JdbcProcedureJavaCallerMetaProvider;
import i2f.jdbc.procedure.provider.types.xml.JdbcProcedureXmlNodeMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import i2f.springboot.jdbc.bql.procedure.impl.SpringContextJdbcProcedureExecutor;
import i2f.springboot.jdbc.bql.procedure.impl.SpringJdbcProcedureJavaCallerMetaCacheProvider;
import i2f.springboot.jdbc.bql.procedure.impl.SpringJdbcProcedureMetaProviderRegistry;
import i2f.springboot.jdbc.bql.procedure.impl.SpringJdbcProcedureXmlNodeMetaCacheProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("xproc4j config ...");
    }


    @ConditionalOnMissingBean(JdbcProcedureXmlNodeMetaProvider.class)
    @Bean
    public JdbcProcedureXmlNodeMetaProvider jdbcProcedureNodeMapSupplier() {
        log.info("xproc4j config SpringJdbcProcedureNodeMapCacheSupplier ...");
        SpringJdbcProcedureXmlNodeMetaCacheProvider ret = new SpringJdbcProcedureXmlNodeMetaCacheProvider();
        String xmlLocations = jdbcProcedureProperties.getXmlLocations();
        if (xmlLocations == null) {
            xmlLocations = SpringJdbcProcedureProperties.DEFAULT_XML_LOCATIONS;
        }
        String[] arr = xmlLocations.split("[,;\n]");
        ret.getXmlLocations().addAll(Arrays.asList(arr));
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnMissingBean(JdbcProcedureJavaCallerMetaProvider.class)
    @Bean
    public JdbcProcedureJavaCallerMetaProvider springJdbcProcedureJavaCallerMapCacheSupplier() {
        log.info("xproc4j config SpringJdbcProcedureJavaCallerMapCacheSupplier ...");
        SpringJdbcProcedureJavaCallerMetaCacheProvider ret = new SpringJdbcProcedureJavaCallerMetaCacheProvider(applicationContext);
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnMissingBean(JdbcProcedureMetaProviderRegistry.class)
    @Bean
    public JdbcProcedureMetaProviderRegistry jdbcProcedureMetaProviderRegistry(){
        log.info("xproc4j config SpringJdbcProcedureMetaProviderRegistry ...");
        return new SpringJdbcProcedureMetaProviderRegistry(applicationContext);
    }

    @ConditionalOnMissingBean(JdbcProcedureContext.class)
    @Bean
    public JdbcProcedureContext procedureContext(JdbcProcedureMetaProviderRegistry registry) {
        log.info("xproc4j config JdbcProcedureContext ...");
        DefaultJdbcProcedureContext ret = new DefaultJdbcProcedureContext(registry);
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnMissingBean(JdbcProcedureExecutor.class)
    @Bean
    public JdbcProcedureExecutor jdbcProcedureExecutor(JdbcProcedureContext context) {
        log.info("xproc4j config JdbcProcedureExecutor ...");
        return new SpringContextJdbcProcedureExecutor(context,applicationContext);
    }

    @ConditionalOnMissingBean(JdbcProcedureContextRefreshListener.class)
    @Bean
    public JdbcProcedureContextRefreshListener  jdbcProcedureContextRefreshListener(JdbcProcedureExecutor executor,
                                                                                    JdbcProcedureContext context){
        log.info("xproc4j config JdbcProcedureContextRefreshListener ...");
        DefaultJdbcProcedureContextRefreshListener listener = new DefaultJdbcProcedureContextRefreshListener(executor);
        listener.getReportOnBoot().set(jdbcProcedureProperties.isReportOnBoot());
        context.listener(listener);
        return listener;
    }

}
