package i2f.springcloud.refresh;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/11 8:31
 * @desc
 */
@ConditionalOnBean(RefreshAutoConfiguration.class)
@ConditionalOnExpression("${i2f.springcloud.refresh.auto-refresh.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springcloud.refresh.auto-refresh")
public class AutoRefreshConfiguration implements InitializingBean {
    private ScheduledExecutorService pool;

    @Autowired
    private ContextRefresher refresher;

    private int delayTime = 5;

    private TimeUnit delayTimeUnit = TimeUnit.MINUTES;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (pool != null) {
            log.warn("AutoRefreshConfig refresh schedule pool has exists,stop before and rebuild.");
            pool = null;
        }
        pool = Executors.newSingleThreadScheduledExecutor();
        if (delayTime <= 0) {
            log.warn("AutoRefreshConfig delay-time could not lower than zero.");
            return;
        }

        pool.scheduleWithFixedDelay(() -> {
            log.info("AutoRefreshConfig schedule refresh configs in delay:" + delayTime + " of " + delayTimeUnit + " ...");
            refresher.refresh();
            log.info("AutoRefreshConfig schedule refresh configs done.");
        }, delayTime, delayTime, delayTimeUnit);

        log.info("AutoRefreshConfig schedule refresh configs done is:" + delayTime + " of " + delayTimeUnit);
    }
}
