package i2f.springboot.ops.datasource.configuration;

import i2f.springboot.ops.datasource.data.DatasourceItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/23 22:26
 * @desc
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.ops.datasource")
public class DatasourceAutoConfiguration {
    protected Map<String, DatasourceItemDto> datasourceMap = new HashMap<>();
}
