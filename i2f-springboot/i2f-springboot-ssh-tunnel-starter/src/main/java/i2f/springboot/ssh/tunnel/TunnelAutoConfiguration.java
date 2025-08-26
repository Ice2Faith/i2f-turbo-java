package i2f.springboot.ssh.tunnel;

import i2f.extension.sftp.SshTunnelUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/8/26 16:36
 */
@ConditionalOnExpression("${i2f.springboot.ssh.tunnel.enable:true}")
@Slf4j
@Data
@AutoConfigureBefore({WebMvcAutoConfiguration.class, DataSourceAutoConfiguration.class})
@Configuration
@EnableConfigurationProperties(TunnelProperties.class)
public class TunnelAutoConfiguration {
    @Autowired
    private TunnelProperties tunnelProperties;

    @Data
    @NoArgsConstructor
    public static class SshTunnelManager{
        public static List<SshTunnelUtil> servers=new ArrayList<>();
    }

    @Bean
    public SshTunnelManager sshTunnelManager() throws Exception {
        SshTunnelManager manager=new SshTunnelManager();
        List<TunnelProperties.Server> servers = tunnelProperties.getServers();
        if(servers!=null) {
            for (TunnelProperties.Server server : servers) {
                log.info("jump server "+server.getName()+" tunnels:");

                TunnelProperties.SshProperties ssh = server.getSsh();
                SshTunnelUtil ret = new SshTunnelUtil(ssh.getHost(), ssh.getPort(), ssh.getUsername(), ssh.getPassword());

                List<TunnelProperties.TunnelItemProperties> tunnels = server.getTunnels();
                if (tunnels != null && !tunnels.isEmpty()) {
                    for (TunnelProperties.TunnelItemProperties item : tunnels) {
                        ret.createTunnel(item.getLocalPort(), item.getRemoteHost(), item.getRemotePort());
                        log.info("create tunnel " + item.getName() + ": " + "localhost" + ":" + item.getLocalPort() + " --> " + item.getRemoteHost() + ":" + item.getRemotePort());
                    }
                }
                ret.setup();
            }
        }

        return manager;
    }
}
