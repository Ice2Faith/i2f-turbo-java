package i2f.springboot.ssh.tunnel;

import i2f.extension.sftp.SshTunnelUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/17 19:47
 * @desc
 */
@Data
@NoArgsConstructor
public class SshTunnelManager {
    protected List<SshTunnelUtil> servers = new ArrayList<>();
}
