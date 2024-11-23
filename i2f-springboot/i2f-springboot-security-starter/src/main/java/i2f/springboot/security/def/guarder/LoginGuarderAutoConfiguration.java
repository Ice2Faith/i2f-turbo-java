package i2f.springboot.security.def.guarder;

import i2f.cache.base.ICache;
import i2f.cache.expire.IExpireCache;
import i2f.web.guarder.LoginGuarder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/8/22 21:58
 * @desc
 */
@ConditionalOnBean(ICache.class)
@Data
@Configuration
public class LoginGuarderAutoConfiguration {
    @Bean
    public LoginGuarder loginGuarder(@Autowired IExpireCache<String, Object> cache) {
        return new LoginGuarder("login", cache);
    }
}
