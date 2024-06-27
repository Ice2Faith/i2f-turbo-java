package i2f.springcloud.discovery.provider;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/27 16:27
 * @desc
 */
@Data
public class DiscoveryServiceInstance implements ServiceInstance {
    private URI uri;
    private String host;
    private int port;
    private boolean secure;
    private Map<String, String> metadata = new LinkedHashMap<>();
    private String instanceId;
    private String serviceId;

    public void setUri(URI uri) {
        this.uri = uri;
        this.host = this.uri.getHost();
        this.port = this.uri.getPort();
        String scheme = this.uri.getScheme();
        if ("https".equals(scheme)) {
            this.secure = true;
        }
    }
}
