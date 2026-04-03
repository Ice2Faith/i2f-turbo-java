package i2f.springboot.auth.permission.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/4/3 11:01
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.auth.permission")
public class CheckPermissionProperties {
    protected int aspectOrder = -999;
}
