package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public SpringJdbcProcedureNodeMapRefresher springJdbcProcedureNodeMapRefresher() {
        SpringJdbcProcedureNodeMapRefresher ret = new SpringJdbcProcedureNodeMapRefresher();
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
    public SpringContextJdbcProcedureExecutorCaller springContextJdbcProcedureExecutorCaller(JdbcProcedureExecutor executor,
                                                                                             SpringJdbcProcedureNodeMapRefresher refresher) {
        return new SpringContextJdbcProcedureExecutorCaller(executor, refresher,()->{
            List<JdbcProcedureJavaCaller> ret=new ArrayList<>();
            Map<String, JdbcProcedureJavaCaller> beanMap = applicationContext.getBeansOfType(JdbcProcedureJavaCaller.class);
            for (Map.Entry<String, JdbcProcedureJavaCaller> entry : beanMap.entrySet()) {
                ret.add(entry.getValue());
            }
            return ret;
        });
    }
}
