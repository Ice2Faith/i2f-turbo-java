package i2f.springcloud.zipkin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2022/5/28 19:45
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.zipkin.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.zipkin")
@Configuration
public class ZipkinAutoConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ZipkinConfig config done.");
    }
}
