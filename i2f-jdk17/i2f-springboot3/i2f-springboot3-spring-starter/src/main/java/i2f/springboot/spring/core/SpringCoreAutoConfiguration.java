package i2f.springboot.spring.core;

import i2f.spring.core.SpringContext;
import i2f.spring.core.SpringUtil;
import i2f.spring.enviroment.EnvironmentUtil;
import i2f.spring.enviroment.SpringEnvironment;
import i2f.spring.event.EventManager;
import i2f.spring.tx.TransactionUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

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

    @ConditionalOnClass(PlatformTransactionManager.class)
    @ConditionalOnBean(PlatformTransactionManager.class)
    @Bean
    public TransactionUtil transactionUtil(PlatformTransactionManager platformTransactionManager) {
        return new TransactionUtil(platformTransactionManager);
    }
}
