package i2f.springboot.spring.core;

import i2f.spring.core.SpringContext;
import i2f.spring.core.SpringUtil;
import i2f.spring.enviroment.EnvironmentUtil;
import i2f.spring.enviroment.SpringEnvironment;
import i2f.spring.event.EventManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Import;

/**
 * @author Ice2Faith
 * @date 2024/6/12 9:44
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.core.enable:true}")
@Import({
        SpringUtil.class,
        EnvironmentUtil.class,
        EventManager.class,
        SpringEnvironment.class,
        SpringContext.class,
})
public class SpringCoreAutoConfiguration {


}
