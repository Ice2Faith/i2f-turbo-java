package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.reportor.GrammarReporter;
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
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private final ExecutorService reportPool= new ThreadPoolExecutor(1, 3,
                                      0L,TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<>(),
                                        new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.info("xproc4j config ...");
    }

    @ConditionalOnMissingBean(JdbcProcedureExecutor.class)
    @Bean
    public JdbcProcedureExecutor jdbcProcedureExecutor() {
        log.info("xproc4j config JdbcProcedureExecutor ...");
        return new SpringContextJdbcProcedureExecutor(applicationContext);
    }

    @Bean
    public SpringJdbcProcedureNodeMapCacheSupplier springJdbcProcedureNodeMapCacheSupplier() {
        log.info("xproc4j config SpringJdbcProcedureNodeMapCacheSupplier ...");
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
        log.info("xproc4j config SpringJdbcProcedureJavaCallerMapCacheSupplier ...");
        SpringJdbcProcedureJavaCallerMapCacheSupplier ret = new SpringJdbcProcedureJavaCallerMapCacheSupplier(applicationContext);
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }


    @Bean
    public JdbcProcedureContext procedureContext(SpringJdbcProcedureNodeMapCacheSupplier nodeMapCacheSupplier,
                                                 SpringJdbcProcedureJavaCallerMapCacheSupplier javaCallerMapCacheSupplier,
                                                 JdbcProcedureExecutor executor) {
        log.info("xproc4j config JdbcProcedureContext ...");
        JdbcProcedureContext ret = new JdbcProcedureContext(nodeMapCacheSupplier, javaCallerMapCacheSupplier);
        AtomicBoolean isInit=new AtomicBoolean(true);
        ret.listener((ctx)->{
            if(isInit.getAndSet(false)){
                GrammarReporter.reportGrammar(executor, new HashMap<>(ctx.getCache()), (msg) -> log.warn(msg));
            }else {
                reportPool.submit(() -> {
                    GrammarReporter.reportGrammar(executor, new HashMap<>(ctx.getCache()), (msg) -> log.warn(msg));
                });
            }
        });
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @Bean
    public DefaultJdbcProcedureExecutorCaller defaultJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor,
                                                                                 JdbcProcedureContext procedureContext) {
        log.info("xproc4j config DefaultJdbcProcedureExecutorCaller ...");
        return new DefaultJdbcProcedureExecutorCaller(executor, procedureContext);
    }
}
