package i2f.springboot.spring.core;

import i2f.spring.tx.TransactionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Ice2Faith
 * @date 2026/3/22 19:49
 * @desc
 */
@ConditionalOnClass(PlatformTransactionManager.class)
@Data
@NoArgsConstructor
@Configuration
public class SpringTransactionUtilConfigurer {
    @ConditionalOnClass(PlatformTransactionManager.class)
    @ConditionalOnBean(PlatformTransactionManager.class)
    @Bean
    public TransactionUtil transactionUtil(PlatformTransactionManager platformTransactionManager) {
        return new TransactionUtil(platformTransactionManager);
    }
}
