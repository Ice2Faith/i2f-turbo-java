package i2f.springboot.jdbc.bql.procedure;

import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;
import i2f.jdbc.procedure.context.impl.ProcedureMetaMapGrammarReporterListener;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.event.XmlNodeExecInvokeLogListener;
import i2f.jdbc.procedure.provider.types.class4j.JdbcProcedureJavaCallerMetaProvider;
import i2f.jdbc.procedure.provider.types.class4j.impl.ContextJdbcProcedureJavaCallerMetaCacheProvider;
import i2f.jdbc.procedure.provider.types.xml.JdbcProcedureXmlNodeMetaProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import i2f.jdbc.procedure.registry.impl.ContextJdbcProcedureMetaProviderRegistry;
import i2f.spring.core.SpringContext;
import i2f.springboot.jdbc.bql.procedure.impl.SpringContextJdbcProcedureExecutor;
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
@ConditionalOnExpression("${xproc4j.enable:true}")
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
        log.info(XProc4jConsts.NAME + " config ...");
    }

    @ConditionalOnExpression("${xproc4j.naming-context.enable:true}")
    @ConditionalOnMissingBean(INamingContext.class)
    @Bean
    public INamingContext namingContext() {
        log.info(XProc4jConsts.NAME + " config " + INamingContext.class.getSimpleName() + " ...");
        INamingContext ret = new SpringContext(applicationContext);
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.event-handler.enable:true}")
    @ConditionalOnMissingBean(XProc4jEventHandler.class)
    @Bean
    public XProc4jEventHandler xProc4jEventHandler(INamingContext namingContext) {
        log.info(XProc4jConsts.NAME + " config " + DefaultXProc4jEventHandler.class.getSimpleName() + " ...");
        DefaultXProc4jEventHandler ret = new DefaultXProc4jEventHandler(() -> namingContext);
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.listeners.xml-node-exec-invoke-log-listener.enable:true}")
    @Bean
    public XmlNodeExecInvokeLogListener xmlNodeExecInvokeLogListener(){
        return new XmlNodeExecInvokeLogListener();
    }

    @ConditionalOnExpression("${xproc4j.provider.xml-node.scan.enable:true}")
    @ConditionalOnMissingBean(JdbcProcedureXmlNodeMetaProvider.class)
    @Bean
    public JdbcProcedureXmlNodeMetaProvider jdbcProcedureNodeMapSupplier(
            @Autowired(required = false) XProc4jEventHandler eventHandler
    ) {
        log.info(XProc4jConsts.NAME + " config " + SpringJdbcProcedureXmlNodeMetaCacheProvider.class.getSimpleName() + " ...");
        SpringJdbcProcedureXmlNodeMetaCacheProvider ret = new SpringJdbcProcedureXmlNodeMetaCacheProvider();
        if (eventHandler != null) {
            ret.setEventHandler(eventHandler);
        }
        String xmlLocations = jdbcProcedureProperties.getXmlLocations();
        if (xmlLocations == null) {
            xmlLocations = SpringJdbcProcedureProperties.DEFAULT_XML_LOCATIONS;
        }
        String[] arr = xmlLocations.split("[,;\n]");
        ret.getXmlLocations().addAll(Arrays.asList(arr));
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.provider.java-caller.enable:true}")
    @ConditionalOnMissingBean(JdbcProcedureJavaCallerMetaProvider.class)
    @Bean
    public JdbcProcedureJavaCallerMetaProvider jdbcProcedureJavaCallerMetaProvider(
            INamingContext namingContext,
            @Autowired(required = false) XProc4jEventHandler eventHandler
    ) {
        log.info(XProc4jConsts.NAME + " config " + ContextJdbcProcedureJavaCallerMetaCacheProvider.class.getSimpleName() + " ...");
        ContextJdbcProcedureJavaCallerMetaCacheProvider ret = new ContextJdbcProcedureJavaCallerMetaCacheProvider(namingContext);
        if (eventHandler != null) {
            ret.setEventHandler(eventHandler);
        }
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.provider.registry.enable:true}")
    @ConditionalOnMissingBean(JdbcProcedureMetaProviderRegistry.class)
    @Bean
    public JdbcProcedureMetaProviderRegistry jdbcProcedureMetaProviderRegistry(
            INamingContext namingContext,
            @Autowired(required = false) XProc4jEventHandler eventHandler
    ) {
        log.info(XProc4jConsts.NAME + " config " + ContextJdbcProcedureMetaProviderRegistry.class.getSimpleName() + " ...");
        ContextJdbcProcedureMetaProviderRegistry ret = new ContextJdbcProcedureMetaProviderRegistry(namingContext);
        if (eventHandler != null) {
            ret.setEventHandler(eventHandler);
        }
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.context.enable:true}")
    @ConditionalOnMissingBean(JdbcProcedureContext.class)
    @Bean
    public JdbcProcedureContext procedureContext(JdbcProcedureMetaProviderRegistry registry,
                                                 @Autowired(required = false) XProc4jEventHandler eventHandler
    ) {
        log.info(XProc4jConsts.NAME + " config " + DefaultJdbcProcedureContext.class.getSimpleName() + " ...");
        DefaultJdbcProcedureContext ret = new DefaultJdbcProcedureContext(registry);
        if (eventHandler != null) {
            ret.setEventHandler(eventHandler);
        }
        ret.startRefreshThread(jdbcProcedureProperties.getRefreshXmlIntervalSeconds());
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.executor.enable:true}")
    @ConditionalOnMissingBean(JdbcProcedureExecutor.class)
    @Bean
    public JdbcProcedureExecutor jdbcProcedureExecutor(JdbcProcedureContext context,
                                                       @Autowired(required = false) XProc4jEventHandler eventHandler
    ) {
        log.info(XProc4jConsts.NAME + " config " + SpringContextJdbcProcedureExecutor.class.getSimpleName() + " ...");
        SpringContextJdbcProcedureExecutor ret = new SpringContextJdbcProcedureExecutor(context, applicationContext);
        if (eventHandler != null) {
            ret.setEventHandler(eventHandler);
        }
        ret.setSlowSqlMinMillsSeconds(jdbcProcedureProperties.getSlowSqlMinMillsSeconds());
        ret.debug(jdbcProcedureProperties.isDebug());
        return ret;
    }

    @ConditionalOnExpression("${xproc4j.procedure-meta.grammar-reporter.enable:true}")
    @ConditionalOnMissingBean(ProcedureMetaMapGrammarReporterListener.class)
    @Bean
    public ProcedureMetaMapGrammarReporterListener procedureMetaMapGrammarReporterListener(JdbcProcedureExecutor executor) {
        log.info(XProc4jConsts.NAME + " config " + ProcedureMetaMapGrammarReporterListener.class.getSimpleName() + " ...");
        ProcedureMetaMapGrammarReporterListener ret = new ProcedureMetaMapGrammarReporterListener(executor);
        ret.getReportOnBoot().set(jdbcProcedureProperties.isReportOnBoot());
        return ret;
    }

}
