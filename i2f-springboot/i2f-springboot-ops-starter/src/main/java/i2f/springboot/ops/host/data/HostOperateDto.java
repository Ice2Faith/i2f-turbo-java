package i2f.springboot.ops.host.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/4 21:53
 * @desc
 */
@Data
@NoArgsConstructor
public class HostOperateDto {
    protected String hostId;

    protected String workdir;
    protected String path;

    protected boolean inline;

    protected String md5;

    protected String cmd;
    protected boolean runAsFile;
    protected long waitForSeconds;

    protected int lineCount;
}
