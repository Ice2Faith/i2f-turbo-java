package i2f.springcloud.refresh;

import i2f.codec.bytes.basex.Base32;
import i2f.otpauth.impl.TotpAuthenticator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2022/4/11 8:56
 * @desc
 */
@ConditionalOnClass(RestController.class)
@ConditionalOnBean(RefreshAutoConfiguration.class)
@ConditionalOnExpression("${i2f.springcloud.refresh.api-refresh.enable:false}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springcloud.refresh.api-refresh")
@RestController
@RequestMapping("refresh")
public class RefreshController implements InitializingBean {

    @Autowired
    private ContextRefresher refresher;

    private String totpKey;

    @PostMapping("trigger")
    public String doRefresh(String code) {
        if (totpKey != null) {
            try {
                byte[] keys = Base32.decode(Base32.encode(totpKey.getBytes()));
                TotpAuthenticator authenticator = new TotpAuthenticator(keys);
                boolean ok = authenticator.verify(code);
                if (!ok) {
                    return "failure!";
                }
            } catch (Exception e) {
                return "failure!";
            }
        }
        log.info("RefreshController trigger refresh ...");
        refresher.refresh();
        log.info("RefreshController trigger refresh done.");
        return "ok";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RefreshController trigger config done.");
    }
}
