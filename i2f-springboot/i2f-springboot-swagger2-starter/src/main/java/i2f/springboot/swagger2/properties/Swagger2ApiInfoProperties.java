package i2f.springboot.swagger2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/6/26 20:25
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "i2f.swagger2.api-info")
public class Swagger2ApiInfoProperties {
    private String title = "Micro-Service Project Api";
    private String description = "Micro-Service project.";
    private String license = "Apache 2.0";
    private String licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html";
    private String version = "1.0.0";
}
