package i2f.springboot.ai.mcp.server.netty.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/7/21 15:55
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.server.netty")
public class NettySimpleMcpServerProperties {
    protected int port = 23745;
    private int bossThread = 4;
    private int workerThread = 0;
    private int maxContentLength = 65536;
}
