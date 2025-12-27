package i2f.springcloud.discovery.server.manager.impl;

import i2f.springcloud.discovery.server.manager.IServiceInstanceManager;
import i2f.springcloud.discovery.server.properties.DiscoveryServerProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/9/25 16:16
 */
@ConditionalOnExpression("${i2f.springcloud.discovery.server.redis-manager.enable:true}")
@ConditionalOnClass(RedisTemplate.class)
@Component
@Data
@NoArgsConstructor
public class RedisServiceInstanceManager implements IServiceInstanceManager {

    @Autowired
    private DiscoveryServerProperties discoveryServerProperties;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public String keyPrefix() {
        String prefix = discoveryServerProperties.getRedisManager().getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return "default:";
        }
        return prefix + ":";
    }

    @Override
    public void saveInstance(String serviceId, String url, Duration ttl) throws Exception {
        // {prefix}:services:{serviceId}:{url}=url
        redisTemplate.opsForValue().set(keyPrefix() + "services:" + serviceId + ":" + URLEncoder.encode(url, "UTF-8"), url, ttl);
    }

    @Override
    public Set<String> getServices() throws Exception {
        Set<Object> keys = redisTemplate.keys(keyPrefix() + "services:*");
        Set<String> ret = new LinkedHashSet<>();
        for (Object item : keys) {
            String key = String.valueOf(item);
            String[] arr = key.split(":services:", 2);
            if (arr.length != 2) {
                continue;
            }
            arr = arr[1].split(":", 2);
            if (arr.length != 2) {
                continue;
            }
            String serviceId = arr[0];
            String url = arr[1];// un-decode url
            ret.add(serviceId);

        }
        return ret;
    }

    @Override
    public List<String> getInstances(String serviceId) throws Exception {
        Set<Object> keys = redisTemplate.keys(keyPrefix() + "services:" + serviceId + ":*");
        List<String> ret = new ArrayList<>();
        for (Object item : keys) {
            String key = String.valueOf(item);
            String[] arr = key.split(":services:", 2);
            if (arr.length != 2) {
                continue;
            }
            arr = arr[1].split(":", 2);
            if (arr.length != 2) {
                continue;
            }
            String url = arr[1];// un-decode url
            ret.add(URLDecoder.decode(url, "UTF-8"));

        }
        return ret;
    }

    @Override
    public Map<String, List<String>> getAllInstances() throws Exception {
        Set<Object> keys = redisTemplate.keys(keyPrefix() + "services:*");
        Map<String, List<String>> ret = new LinkedHashMap<>();
        for (Object item : keys) {
            String key = String.valueOf(item);
            String[] arr = key.split(":services:", 2);
            if (arr.length != 2) {
                continue;
            }
            arr = arr[1].split(":", 2);
            if (arr.length != 2) {
                continue;
            }
            String serviceId = arr[0];
            String url = arr[1];// un-decode url
            List<String> list = ret.computeIfAbsent(serviceId, v -> new ArrayList<>());
            list.add(URLDecoder.decode(url, "UTF-8"));

        }
        return ret;
    }
}
