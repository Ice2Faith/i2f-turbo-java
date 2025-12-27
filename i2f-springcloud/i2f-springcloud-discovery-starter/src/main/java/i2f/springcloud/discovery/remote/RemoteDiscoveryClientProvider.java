package i2f.springcloud.discovery.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springcloud.discovery.provider.DiscoveryServiceInstance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Ice2Faith
 * @date 2024/6/27 16:42
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.discovery.registry.enable:true}")
@EnableConfigurationProperties(RemoteRegistryProperties.class)
@Component
@Slf4j
@Data
@NoArgsConstructor
public class RemoteDiscoveryClientProvider implements DiscoveryClient {
    protected Map<String, List<ServiceInstance>> holder = new ConcurrentHashMap<>();

    public static final int DEFAULT_APPLICATION_PORT = 8080;
    @Autowired(required = false)
    private ServerProperties server;

    @Autowired
    private Environment environment;

    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

    @Autowired
    private RemoteRegistryProperties remoteRegistryProperties;

    @Autowired(required = false)
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired(required = false)
    private ObjectMapper objectMapper = new ObjectMapper() {
        {
            setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")));
            setLocale(Locale.CHINA);
        }
    };

    @Override
    public String description() {
        return "Remote Discovery Client Provider";
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
        return remoteRegistryProperties.getOrder();
    }

    @PostConstruct
    public void init() {
        try {
            sendHearBeat();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            pullServiceInstances();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        startHeartBeatThread();
        startServicePullThread();
    }

    private int findPort() {
        if (server == null) {
            return DEFAULT_APPLICATION_PORT;
        }
        Integer port = server.getPort();
        if (port == null) {
            return DEFAULT_APPLICATION_PORT;
        }
        if (port <= 0) {
            return DEFAULT_APPLICATION_PORT;
        }
        return port;
    }

    public String makeSign(String payload) throws Exception {
        String secretKey = remoteRegistryProperties.getSecretKey();
        String content = secretKey + "#" + payload;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] arr = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte bt : arr) {
            builder.append(String.format("%02x", (int) (bt & 0x0ff)));
        }
        return builder.toString();
    }

    public String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String combineUrl(String baseUrl, String path) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return baseUrl + path;
    }

    protected void startHeartBeatThread() {
        pool.scheduleAtFixedRate(() -> {
            try {
                sendHearBeat();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }, 0, remoteRegistryProperties.getHeartBeatSeconds(), TimeUnit.SECONDS);
    }

    public void sendHearBeat() throws Exception {
        String url = combineUrl(remoteRegistryProperties.getBaseUrl(), remoteRegistryProperties.getRegistryPath());
        String serviceId = environment.getProperty("spring.application.name", "application");
        int port = findPort();
        Map<String, Object> req = new HashMap<>();
        req.put("serviceId", serviceId);
        req.put("port", port);
        String uid = uuid();
        req.put("uid", uid);
        req.put("sign", makeSign(uid));
        String json = restTemplate.postForObject(url, req, String.class);
        Map<String, Object> resp = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        String selfUrl = (String) resp.get("url");
    }

    protected void startServicePullThread() {
        pool.scheduleAtFixedRate(() -> {
            try {
                pullServiceInstances();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }, 0, remoteRegistryProperties.getPullServiceSeconds(), TimeUnit.SECONDS);
    }

    public void pullServiceInstances() throws Exception {
        String url = combineUrl(remoteRegistryProperties.getBaseUrl(), remoteRegistryProperties.getPullPath());
        Map<String, Object> req = new HashMap<>();
        String uid = uuid();
        req.put("uid", uid);
        req.put("sign", makeSign(uid));
        String json = restTemplate.postForObject(url, req, String.class);
        Map<String, List<String>> resp = objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {
        });

        for (Map.Entry<String, List<String>> entry : resp.entrySet()) {
            List<String> value = entry.getValue();
            List<ServiceInstance> list = new CopyOnWriteArrayList<>();
            for (String item : value) {
                DiscoveryServiceInstance inst = new DiscoveryServiceInstance();
                inst.setServiceId(entry.getKey());
                inst.setUri(new URI(item));
                list.add(inst);
            }
            holder.put(entry.getKey(), list);
        }
    }
}
