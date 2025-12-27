package i2f.springboot.ssh.tunnel;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/8/26 16:39
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ssh.tunnel")
public class TunnelProperties {

    protected List<Server> servers = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class Server {
        protected String name;

        protected SshProperties ssh;

        protected List<TunnelItemProperties> tunnels = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class SshProperties {
        protected String host;
        protected int port = 22;
        protected String username;
        protected String password;
    }

    @Data
    @NoArgsConstructor
    public static class TunnelItemProperties {
        protected String name;
        protected int localPort;
        protected String remoteHost;
        protected int remotePort;
    }
}
