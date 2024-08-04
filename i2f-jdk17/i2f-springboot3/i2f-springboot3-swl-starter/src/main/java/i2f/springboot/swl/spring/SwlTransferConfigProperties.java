package i2f.springboot.swl.spring;

import i2f.swl.core.SwlTransferConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:29
 * @desc
 */
@ConfigurationProperties(prefix = "i2f.swl.transfer")
public class SwlTransferConfigProperties extends SwlTransferConfig {
}
