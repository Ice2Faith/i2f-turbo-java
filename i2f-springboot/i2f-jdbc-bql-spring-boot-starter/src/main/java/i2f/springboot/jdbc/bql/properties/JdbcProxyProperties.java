package i2f.springboot.jdbc.bql.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:34
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "i2f.jdbc.proxy")
public class JdbcProxyProperties {
    private boolean enable;

    private List<String> mapperPackages;

    private List<String> scriptLocations;
}
