package i2f.springboot.ops.app.data.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/21 14:10
 */
@Data
@NoArgsConstructor
public class AppServiceInstanceDto {
    protected String instanceId;
    protected String serviceId;
    protected String uri;
    protected String scheme;
    protected String host;
    protected int port;
    protected Map<String, String> metadata = new HashMap<>();

    public static AppServiceInstanceDto of(ServiceInstance instance) {
        AppServiceInstanceDto ret = new AppServiceInstanceDto();
        ret.setServiceId(instance.getServiceId());
        ret.setInstanceId(instance.getInstanceId());
        ret.setUri(instance.getUri().toString());
        ret.setScheme(instance.getScheme());
        ret.setHost(instance.getHost());
        ret.setPort(instance.getPort());
        ret.setMetadata(instance.getMetadata());
        return ret;
    }
}
