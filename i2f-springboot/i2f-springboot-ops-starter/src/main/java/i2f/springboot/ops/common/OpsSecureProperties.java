package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2025/11/2 12:33
 * @desc
 */
@ConfigurationProperties(prefix = "i2f.springboot.ops.secure")
@Data
@NoArgsConstructor
public class OpsSecureProperties {
    protected String cert;
}
