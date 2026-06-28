package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.dashscope.data.DashScopeBaseOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeUploadOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/4/28 19:09
 * @desc
 */
@ConditionalOnClass(RestTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/dashscope/tmp-file")
public class DashScopeOpsTmpFileController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private DashScopeOpsController controller;


    public Map<String, Object> getUploadPolicy(DashScopeBaseOperateDto req) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());

        HttpEntity<Object> reqEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange("https://dashscope.aliyuncs.com/api/v1/uploads?action=getPolicy&model=" + req.getModelName(),
                HttpMethod.GET,
                reqEntity,
                String.class);
        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> policyMap = (Map<String, Object>) respMap.get("data");
        return policyMap;
    }

    @PostMapping("/get-policy")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> getPolicy(@RequestBody OpsSecureDto reqDto,
                                                   HttpServletRequest request) throws Exception {
        try {
            DashScopeBaseOperateDto req = transfer.recv(reqDto, DashScopeBaseOperateDto.class);

            Map<String, Object> respMap = getUploadPolicy(req);
            return transfer.success(respMap);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    public String uploadFile(DashScopeBaseOperateDto req, File file) throws Exception {
        Map<String, Object> policyMap = getUploadPolicy(req);

        String ossKey = ((String) policyMap.get("upload_dir") + "/" + file.getName());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("OSSAccessKeyId", (String) policyMap.get("oss_access_key_id"));
        body.add("Signature", (String) policyMap.get("signature"));
        body.add("policy", (String) policyMap.get("policy"));
        body.add("x-oss-object-acl", (String) policyMap.get("x_oss_object_acl"));
        body.add("x-oss-forbid-overwrite", (String) policyMap.get("x_oss_forbid_overwrite"));
        body.add("key", ossKey);
        body.add("success_action_status", "200");
        body.add("file", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange((String) policyMap.get("upload_host"),
                HttpMethod.POST,
                reqEntity,
                String.class);

        String ossUrl = "oss://" + ossKey;

        return ossUrl;
    }

    @PostMapping("/upload")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> uploadFile(MultipartFile file, OpsSecureDto reqDto,
                                                    HttpServletRequest request) throws Exception {
        try {
            DashScopeUploadOperateDto req = transfer.recv(reqDto, DashScopeUploadOperateDto.class);

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "upload.data";
            }
            String suffix = "";
            int idx = originalFilename.lastIndexOf(".");
            if (idx > 0) {
                suffix = originalFilename.substring(idx);
            }
            if (suffix.isEmpty()) {
                suffix = ".data";
            }

            File tmpFile = File.createTempFile("upload-" + (UUID.randomUUID().toString().replace("-", "")), suffix);
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                OutputStream os = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[2048];
                int len = 0;
                InputStream is = file.getInputStream();
                while ((len = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, len);
                    os.write(buffer, 0, len);
                }
                os.close();
                byte[] bytes = digest.digest();
                StringBuilder builder = new StringBuilder();
                for (byte bt : bytes) {
                    builder.append(String.format("%02x", (int) (bt & 0x0ff)));
                }
                String calcMd5 = builder.toString();
                if (!calcMd5.equalsIgnoreCase(req.getMd5())) {
                    throw new OpsException("file check sum error");
                }
                String ossUrl = uploadFile(req, tmpFile);
                return transfer.success(ossUrl);
            } finally {
                if (tmpFile != null && tmpFile.exists()) {
                    tmpFile.delete();
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
