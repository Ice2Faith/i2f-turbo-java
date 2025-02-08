package i2f.springboot.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author ltb
 * @date 2022/4/22 10:34
 * @desc
 */
@Slf4j
@ConditionalOnExpression("!${i2f.springboot.config.security.enable:true}")
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class
})
public class DisableSecurityConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DisableSecurityConfig disable config.");
    }
}
