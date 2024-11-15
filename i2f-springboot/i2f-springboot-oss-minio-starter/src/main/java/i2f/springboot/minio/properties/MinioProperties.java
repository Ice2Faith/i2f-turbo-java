package i2f.springboot.minio.properties;

import i2f.extension.minio.MinioMeta;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/11/15 17:25
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.minio")
public class MinioProperties extends MinioMeta {
}
