package i2f.springcloud.discovery.provider;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/6/27 16:42
 * @desc
 */
@Data
@NoArgsConstructor
public class DiscoveryClientProvider implements DiscoveryClient {
    protected int order = 0;
    protected Map<String, List<ServiceInstance>> holder = new ConcurrentHashMap<>();

    @Override
    public String description() {
        return "Discovery Client Provider";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        List<ServiceInstance> ret = new ArrayList<>();
        List<ServiceInstance> list = holder.get(serviceId);
        if (list != null) {
            ret.addAll(list);
        }

        return ret;
    }

    @Override
    public List<String> getServices() {
        return new ArrayList<>(holder.keySet());
    }

    @Override
    public int getOrder() {
        return order;
    }
}
