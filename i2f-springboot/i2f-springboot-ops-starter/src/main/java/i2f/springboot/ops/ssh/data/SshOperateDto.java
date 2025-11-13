package i2f.springboot.ops.ssh.data;

import i2f.extension.sftp.basic.data.SftpMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/13 20:58
 * @desc
 */
@Data
@NoArgsConstructor
public class SshOperateDto {
    protected SftpMeta meta;

    protected String workdir;
    protected String path;

    protected boolean inline;

    protected String md5;

    protected String cmd;
    protected boolean runAsFile;
    protected long waitForSeconds;

    protected int lineCount;
}
