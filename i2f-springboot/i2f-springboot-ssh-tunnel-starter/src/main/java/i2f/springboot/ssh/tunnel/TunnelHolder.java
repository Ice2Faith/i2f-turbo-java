package i2f.springboot.ssh.tunnel;

/**
 * @author Ice2Faith
 * @date 2026/6/17 19:51
 * @desc
 */
public class TunnelHolder {
    public static final String CONFIG_PREFIX = "i2f.springboot.ssh.tunnel";
    public static volatile TunnelProperties tunnelProperties;
    public static volatile SshTunnelManager sshTunnelManager;
}
