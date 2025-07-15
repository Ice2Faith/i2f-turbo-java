package i2f.extension.oss.aws.s3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/7/14 14:27
 */
@Data
@NoArgsConstructor
public class AwsS3OssMeta {

    protected String url;

    protected String accessKeyId;

    protected String secretAccessKey;

    protected String region;
}
