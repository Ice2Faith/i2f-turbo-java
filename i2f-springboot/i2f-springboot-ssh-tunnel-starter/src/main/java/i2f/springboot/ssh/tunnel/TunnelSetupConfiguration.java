package i2f.springboot.ssh.tunnel;

import i2f.extension.sftp.SshTunnelUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/17 19:46
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ssh.tunnel.enable:true}")
@Component
@Slf4j
@Data
@NoArgsConstructor
public class TunnelSetupConfiguration implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Binder binder = Binder.get(event.getEnvironment());

        TunnelProperties properties = binder.bind(TunnelHolder.CONFIG_PREFIX, TunnelProperties.class)
                .orElse(new TunnelProperties());
        TunnelHolder.tunnelProperties = properties;
        SshTunnelManager sshTunnelManager = sshTunnelManager(properties);
        TunnelHolder.sshTunnelManager = sshTunnelManager;
    }

    public static SshTunnelManager sshTunnelManager(TunnelProperties tunnelProperties) {
        SshTunnelManager manager = new SshTunnelManager();
        List<TunnelProperties.Server> servers = tunnelProperties.getServers();
        if (servers != null) {
            for (TunnelProperties.Server server : servers) {
                log.info("jump server " + server.getName() + " tunnels:");

                TunnelProperties.SshProperties ssh = server.getSsh();
                SshTunnelUtil ret = new SshTunnelUtil(ssh.getHost(), ssh.getPort(), ssh.getUsername(), ssh.getPassword());

                List<TunnelProperties.TunnelItemProperties> tunnels = server.getTunnels();
                if (tunnels != null && !tunnels.isEmpty()) {
                    for (TunnelProperties.TunnelItemProperties item : tunnels) {
                        try {
                            ret.createTunnel(item.getLocalPort(), item.getRemoteHost(), item.getRemotePort());
                            log.info("create tunnel " + item.getName() + ": " + "localhost" + ":" + item.getLocalPort() + " --> " + item.getRemoteHost() + ":" + item.getRemotePort());
                        } catch (Exception e) {
                            log.info("create tunnel " + item.getName() + ": " + "localhost" + ":" + item.getLocalPort() + " --> " + item.getRemoteHost() + ":" + item.getRemotePort(), e);
                        }
                    }
                }
                ret.setup();
            }
        }

        return manager;
    }
}
