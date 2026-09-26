package i2f.springboot.ai.mcp.server.properties;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.server.data.HttpSimpleMcpAppItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/21 15:42
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.server.simple-server")
public class HttpSimpleMcpServerProperties {
    protected long expireWindowMinutes = 30;
    protected String hmacName = HttpSimpleMcpConstants.DEFAULT_HMAC_NAME;
    protected List<HttpSimpleMcpAppItem> appList;

}
