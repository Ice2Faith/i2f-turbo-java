package i2f.springcloud.discovery.server.controller;

import i2f.springcloud.discovery.server.manager.IServiceInstanceManager;
import i2f.springcloud.discovery.server.properties.DiscoveryServerProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/9/25 15:59
 */
@ConditionalOnExpression("${i2f.springcloud.discovery.server.api.enable:true}")
@Data
@NoArgsConstructor
@RestController
@RequestMapping("/api")
public class DiscoverServerController {

    @Autowired
    private DiscoveryServerProperties discoveryServerProperties;

    @Autowired
    private IServiceInstanceManager serviceInstanceManager;

    public String makeSign(String payload) throws Exception {
        String secretKey = discoveryServerProperties.getSecretKey();
        String content = secretKey + "#" + payload;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] arr = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte bt : arr) {
            builder.append(String.format("%02x", (int) (bt & 0x0ff)));
        }
        return builder.toString();
    }

    @PostMapping("/api/registry")
    public Map<String, Object> registry(@RequestBody Map<String, Object> body, HttpServletRequest request) throws Exception {
        String uid = (String) body.get("uid");
        String sign = (String) body.get("sign");
        String calcSign = makeSign(uid);
        if (!calcSign.equalsIgnoreCase(sign)) {
            throw new IllegalArgumentException("sign not correct!");
        }
        String serviceId = (String) body.get("serviceId");
        Integer port = (Integer) body.get("port");
        String host = request.getRemoteAddr();

        String url = "http://" + host + ":" + port;

        serviceInstanceManager.saveInstance(serviceId, url, Duration.ofSeconds(discoveryServerProperties.getKeepaliveSeconds()));

        Map<String, Object> ret = new HashMap<>();
        ret.put("url", url);
        return ret;
    }

    @PostMapping("/api/services")
    public Map<String, List<String>> services(@RequestBody Map<String, Object> body) throws Exception {
        String uid = (String) body.get("uid");
        String sign = (String) body.get("sign");
        String calcSign = makeSign(uid);
        if (!calcSign.equalsIgnoreCase(sign)) {
            throw new IllegalArgumentException("sign not correct!");
        }

        Map<String, List<String>> ret = serviceInstanceManager.getAllInstances();

        return ret;
    }
}
