package i2f.springboot.ops.minio.data;

import i2f.extension.minio.MinioMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/25 9:47
 */
@Data
@NoArgsConstructor
public class MinioOperateDto {
    protected MinioMeta meta;

    protected String workdir;
    protected String path;

    protected boolean inline;

    protected String md5;

    protected int lineCount;
}
