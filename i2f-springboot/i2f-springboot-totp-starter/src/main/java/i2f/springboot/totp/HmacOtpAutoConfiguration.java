package i2f.springboot.totp;

import i2f.springboot.totp.api.impl.DefaultTotpAuthenticatorFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Ice2Faith
 * @date 2024/6/27 10:49
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.totp.enable:true}")
@Data
@Slf4j
@NoArgsConstructor
@Import({
        HmacOtpAccountAuthenticator.class,
        DefaultTotpAuthenticatorFactory.class
})
@ConfigurationProperties(prefix = "i2f.springboot.totp")
public class HmacOtpAutoConfiguration {
}
