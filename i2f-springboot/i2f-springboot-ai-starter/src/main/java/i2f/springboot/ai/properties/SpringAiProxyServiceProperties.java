package i2f.springboot.ai.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/30 14:31
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.proxy")
public class SpringAiProxyServiceProperties {
    protected List<String> servicePackages = new ArrayList<>();
}
