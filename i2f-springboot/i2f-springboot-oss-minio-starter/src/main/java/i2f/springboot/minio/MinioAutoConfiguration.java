package i2f.springboot.minio;

import i2f.extension.filesystem.minio.MinioFileSystem;
import i2f.extension.minio.MinioUtil;
import i2f.springboot.minio.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author Ice2Faith
 * @date 2022/4/12 14:05
 * @desc
 */
@ConditionalOnExpression("${i2f.minio.enable:true}")
@Slf4j
@Data
@Import({
        MinioClient.class
})
@EnableConfigurationProperties({
        MinioProperties.class
})
public class MinioAutoConfiguration {

    String dateFormat = "yyyy-MM-dd HH:mm:ss SSS";

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioUtil minioUtil() {
        MinioUtil ret = new MinioUtil(minioProperties);
        log.info("MinioUtil config done.");
        return ret;
    }

    @Bean
    public MinioFileSystem minioFileSystem(MinioUtil minioUtil) {
        MinioFileSystem ret = new MinioFileSystem(minioUtil.getClient());
        log.info("MinioUtil config done.");
        return ret;
    }

}
