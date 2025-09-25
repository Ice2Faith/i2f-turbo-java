package i2f.springcloud.discovery.server.manager;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2025/9/25 16:09
 */
public interface IServiceInstanceManager {
    void saveInstance(String serviceId, String url, Duration ttl) throws Exception;

    Set<String> getServices() throws Exception;

    List<String> getInstances(String serviceId) throws Exception;

    Map<String, List<String>> getAllInstances() throws Exception;
}
