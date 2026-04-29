package i2f.springboot.ops.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.util.NetworkUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/14 8:43
 */
@Data
@NoArgsConstructor
@Component
public class HostIdHelper {
    @Autowired
    protected Environment environment;

    protected AtomicReference<String> hostIdHolder = new AtomicReference<>();
    protected AtomicLong hostIdUpdateTs = new AtomicLong(0L);

    protected AtomicInteger postHolder = new AtomicInteger(0);

    public int getAppPort() {
        int port = postHolder.get();
        if (port > 0) {
            return port;
        }
        String property = environment.getProperty("server.port", "8080");
        try {
            port = Integer.parseInt(property);
        } catch (Exception e) {

        }
        postHolder.set(port);
        return port;
    }

    public String getHostId() {
        long cts = System.currentTimeMillis();
        if (cts - hostIdUpdateTs.get() < 5 * 60 * 1000) {
            String ret = hostIdHolder.get();
            if (ret != null) {
                return ret;
            }
        }
        List<Map.Entry<InetAddress, NetworkInterface>> list = NetworkUtil.getUsefulAddresses();
        int port = getAppPort();
        int count = 0;
        String hostId = port + "@";
        for (Map.Entry<InetAddress, NetworkInterface> entry : list) {
            if (count > 0) {
                hostId += "|";
            }
            hostId += "[" + entry.getKey().getHostAddress() + "#" + entry.getValue().getName() + "]";
            count++;
            if (count == 3) {
                break;
            }
        }
        hostIdHolder.set(hostId);
        hostIdUpdateTs.set(cts);
        return hostIdHolder.get();
    }

    public boolean canAcceptHostId(String reqHostId) {
        if (reqHostId == null || reqHostId.isEmpty()) {
            return true;
        }
        String currHostId = getHostId();
        if (Objects.equals(currHostId, reqHostId)) {
            return true;
        }
        return false;
    }

    public String getHostIdBaseUrl(String hostId) {
        List<String> list = getHostIdBaseUrls(hostId, 1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<String> getHostIdBaseUrls(String hostId) {
        return getHostIdBaseUrls(hostId, -1);
    }

    public List<String> getHostIdBaseUrls(String hostId, int limit) {
        List<String> ret = new ArrayList<>();
        if (hostId == null || hostId.isEmpty()) {
            return ret;
        }
        String[] arr = hostId.split("@", 2);
        String port = arr[0];
        String[] addrs = arr[1].split("\\|");
        int count = 0;
        for (String addr : addrs) {
            addr = addr.substring(1, addr.length() - 1);
            addr = addr.split("#", 2)[0];
            if (addr.contains(":")) {
                // ipv6
                String baseUrl = "http://[" + addr + "]:" + port;
                ret.add(baseUrl);
            } else {
                String baseUrl = "http://" + addr + ":" + port;
                ret.add(baseUrl);
            }
            count++;
            if (limit >= 0 && count >= limit) {
                break;
            }
        }
        return ret;
    }

    @FunctionalInterface
    public static interface ExConsumer<T> {
        void accept(T obj) throws Exception;
    }

}
