package i2f.springboot.ops.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/29 9:02
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class HostIdProxyHelper {
    @Autowired
    protected Environment environment;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected HostIdHelper hostIdHelper;

    @Autowired
    private OpsSecureTransfer transfer;

    public OpsSecureReturn<OpsSecureDto> proxyHostId(Object reqDto, String hostId, HttpServletRequest originRequest) throws Exception {
        String targetPath = originRequest.getRequestURI();
        return proxyHostId(reqDto, hostId, targetPath);
    }

    public OpsSecureReturn<OpsSecureDto> proxyHostId(Object reqDto, String hostId, String targetPath) throws Exception {
        String baseUrl = hostIdHelper.getHostIdBaseUrl(hostId);
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

    public void proxyHostIdDownload(Object reqDto, String hostId, HttpServletRequest originRequest, HostIdHelper.ExConsumer<ClientHttpResponse> responseConsumer) throws Exception {
        String targetPath = originRequest.getRequestURI();
        proxyHostIdDownload(reqDto, hostId, targetPath, responseConsumer);
    }

    public void proxyHostIdDownload(Object reqDto, String hostId, String targetPath, HostIdHelper.ExConsumer<ClientHttpResponse> responseConsumer) throws Exception {
        String baseUrl = hostIdHelper.getHostIdBaseUrl(hostId);
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
        String baseUrl = hostIdHelper.getHostIdBaseUrl(hostId);
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
