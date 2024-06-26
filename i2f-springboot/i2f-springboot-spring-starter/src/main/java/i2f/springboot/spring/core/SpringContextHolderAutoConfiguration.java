package i2f.springboot.spring.core;

import i2f.spring.core.SpringUtil;
import i2f.spring.enviroment.EnvironmentUtil;
import i2f.spring.event.EventManager;
import i2f.springboot.spring.core.holder.SpringContextHolder;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * @author Ice2Faith
 * @date 2024/6/12 10:06
 * @desc
 */
@Data
@AutoConfigureAfter(SpringCoreAutoConfiguration.class)
@ConditionalOnBean(SpringCoreAutoConfiguration.class)
public class SpringContextHolderAutoConfiguration implements InitializingBean {

    @Autowired
    private SpringUtil springUtil;

    @Autowired
    private EnvironmentUtil environmentUtil;

    @Autowired
    private EventManager eventManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringContextHolder.setSpringUtil(springUtil);
        SpringContextHolder.setEnvironmentUtil(environmentUtil);
        SpringContextHolder.setEventManager(eventManager);
    }
}
