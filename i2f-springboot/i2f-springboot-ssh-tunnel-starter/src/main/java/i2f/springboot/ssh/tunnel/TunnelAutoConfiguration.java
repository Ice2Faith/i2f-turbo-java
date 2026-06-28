package i2f.springboot.ssh.tunnel;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2025/8/26 16:36
 */
@ConditionalOnExpression("${i2f.springboot.ssh.tunnel.enable:true}")
@Slf4j
@Data
@Configuration
public class TunnelAutoConfiguration {

    @Bean
    public TunnelProperties tunnelProperties() {
        return TunnelHolder.tunnelProperties;
    }

    @Bean
    public SshTunnelManager sshTunnelManager() {
        return TunnelHolder.sshTunnelManager;
    }

}
