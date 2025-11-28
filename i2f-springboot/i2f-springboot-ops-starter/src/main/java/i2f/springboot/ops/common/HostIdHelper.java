package i2f.springboot.ops.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.util.NetworkUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired(required = false)
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    protected ObjectMapper objectMapper;

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

    public OpsSecureReturn<OpsSecureDto> proxyHostId(Object reqDto, String hostId, HttpServletRequest originRequest) throws Exception {
        String targetPath = originRequest.getRequestURI();
        return proxyHostId(reqDto, hostId, targetPath);
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostId(Object reqDto, String hostId, String targetPath) throws Exception {
        String baseUrl = getHostIdBaseUrl(hostId);
        if (!targetPath.startsWith("/")) {
            targetPath = "/" + targetPath;
        }
        String targetUrl = baseUrl + targetPath;
        OpsSecureDto req = transfer.send(reqDto);
        String respJson = restTemplate.postForObject(targetUrl, req, String.class);
        OpsSecureReturn<OpsSecureDto> resp = objectMapper.readValue(respJson, new TypeReference<OpsSecureReturn<OpsSecureDto>>() {
        });
        return resp;
    }

    public void proxyHostIdDownload(Object reqDto, String hostId, HttpServletRequest originRequest, ExConsumer<ClientHttpResponse> responseConsumer) throws Exception {
        String targetPath = originRequest.getRequestURI();
        proxyHostIdDownload(reqDto, hostId, targetPath, responseConsumer);
    }

    public void proxyHostIdDownload(Object reqDto, String hostId, String targetPath, ExConsumer<ClientHttpResponse> responseConsumer) throws Exception {
        String baseUrl = getHostIdBaseUrl(hostId);
        if (!targetPath.startsWith("/")) {
            targetPath = "/" + targetPath;
        }
        String targetUrl = baseUrl + targetPath;
        OpsSecureDto req = transfer.send(reqDto);
        restTemplate.execute(targetUrl, HttpMethod.POST, request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            objectMapper.writeValue(request.getBody(), req);
        }, clientHttpResponse -> {
            try {
                responseConsumer.accept(clientHttpResponse);
            } catch (Exception e) {
                throw new RestClientException(e.getMessage());
            }
            return null;
        });
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostIdUpload(Object reqDto, String hostId, HttpServletRequest originRequest, MultipartFile multipartFile) throws Exception {
        String targetPath = originRequest.getRequestURI();
        return proxyHostIdUpload(reqDto, hostId, targetPath, multipartFile.getOriginalFilename(), multipartFile.getSize(), multipartFile.getInputStream());
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostIdUpload(Object reqDto, String hostId, String targetPath, MultipartFile multipartFile) throws Exception {
        return proxyHostIdUpload(reqDto, hostId, targetPath, multipartFile.getOriginalFilename(), multipartFile.getSize(), multipartFile.getInputStream());
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostIdUpload(Object reqDto, String hostId, HttpServletRequest originRequest, String virtualFileName, long fileSize, InputStream fileIs) throws Exception {
        String targetPath = originRequest.getRequestURI();
        return proxyHostIdUpload(reqDto, hostId, targetPath, virtualFileName, fileSize, fileIs);
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostIdUpload(Object reqDto, String hostId, String targetPath, String virtualFileName, long fileSize, InputStream fileIs) throws Exception {
        String baseUrl = getHostIdBaseUrl(hostId);
        if (!targetPath.startsWith("/")) {
            targetPath = "/" + targetPath;
        }
        String targetUrl = baseUrl + targetPath;
        OpsSecureDto req = transfer.send(reqDto);

        File file = new File(virtualFileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new VirtualFileInputStreamResource(fileIs, file.getName(), fileSize));
        String json = objectMapper.writeValueAsString(req);
        Map<String, Object> form = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            body.set(entry.getKey(), entry.getValue());
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, entity, String.class);
        String respJson = response.getBody();

        OpsSecureReturn<OpsSecureDto> resp = objectMapper.readValue(respJson, new TypeReference<OpsSecureReturn<OpsSecureDto>>() {
        });
        return resp;
    }

    public class VirtualFileInputStreamResource extends InputStreamResource {
        private String fileName;
        private long length;

        public VirtualFileInputStreamResource(InputStream inputStream, String fileName, long length) {
            super(inputStream);
            this.fileName = fileName;
            this.length = length;
        }

        @Override
        public long contentLength() throws IOException {
            return length > 0 ? length : -1;
        }

        @Override
        public String getFilename() {
            return fileName;
        }
    }
}
