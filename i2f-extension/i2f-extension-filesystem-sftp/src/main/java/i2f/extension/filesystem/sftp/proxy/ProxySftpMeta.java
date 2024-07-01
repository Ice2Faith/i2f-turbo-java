package i2f.extension.filesystem.sftp.proxy;

import i2f.extension.filesystem.sftp.SftpMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2024/7/1 9:25
 * @desc
 */
@Data
@NoArgsConstructor
public class ProxySftpMeta extends SftpMeta {
    private int localPort;
    private String remoteHost;
    private int remotePort;
    private String remoteUsername;
    private String remotePassword;
    private String remotePrivateKey;
    private Properties remoteConfig = new Properties();
}
