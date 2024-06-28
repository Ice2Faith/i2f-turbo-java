package i2f.springcloud.refresh;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

/**
 * @author Ice2Faith
 * @date 2022/2/25 8:48
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.refresh.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Import({
        RefreshScope.class,
        AutoRefreshConfiguration.class,
        RefreshController.class
})
@ConfigurationProperties(prefix = "i2f.springcloud.refresh")
public class RefreshAutoConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Autowired
    private RefreshScope refreshScope;

    @Autowired
    private ContextRefresher refresher;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void refresh() {
        refresher.refresh();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RefreshConfig config done.");
    }

    @EventListener
    public void envListener(EnvironmentChangeEvent event) {
        log.info("RefreshConfig environment refresh.");
    }
}
