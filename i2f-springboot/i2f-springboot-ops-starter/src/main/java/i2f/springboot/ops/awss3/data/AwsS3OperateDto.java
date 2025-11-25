package i2f.springboot.ops.awss3.data;

import i2f.extension.oss.aws.s3.AwsS3OssMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/25 9:47
 */
@Data
@NoArgsConstructor
public class AwsS3OperateDto {
    protected AwsS3OssMeta meta;

    protected String workdir;
    protected String path;

    protected boolean inline;

    protected String md5;

    protected int lineCount;
}
