package i2f.springcloud.config.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/4/1 15:38
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springcloud.config-client.refresh.pull")
public class EnvironmentPullBaseWithVersionProperties {
    protected long initDelaySeconds = 30;
    protected long intervalDelaySeconds = 30;
}
