package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.dashscope.data.DashScopeBaseOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeVideoWanDigitalPeopleOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/29
 * @desc 万相 数字人对口型视频生成（wan2.2-s2v）
 */
@ConditionalOnClass(RestTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/dashscope/video/wan/digital-people")
public class DashScopeOpsVideoWanDigitalPeopleController {

    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DashScopeOpsController controller;

    /**
     * 图像检测：检测图片是否满足 wan2.2-s2v 的输入规范
     * API: POST https://dashscope.aliyuncs.com/api/v1/services/aigc/image2video/face-detect
     */
    public Map<String, Object> detectImage(DashScopeBaseOperateDto req, String imageUrl) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "wan2.2-s2v-detect");
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("image_url", imageUrl);
        body.put("input", inputMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());
        if (imageUrl.startsWith("oss://")) {
            headers.add("X-DashScope-OssResourceResolve", "enable");
        }

        HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange(
                "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2video/face-detect",
                HttpMethod.POST,
                reqEntity,
                String.class);

        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });
        return respMap;
    }

    @PostMapping("/detect")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> detectImage(@RequestBody OpsSecureDto reqDto,
                                                     HttpServletRequest request) throws Exception {
        try {
            DashScopeVideoWanDigitalPeopleOperateDto req = transfer.recv(reqDto, DashScopeVideoWanDigitalPeopleOperateDto.class);
            Map<String, Object> result = detectImage(req, req.getImageUrl());
            return transfer.success(result);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    /**
     * 提交数字人视频生成异步任务
     * API: POST https://dashscope.aliyuncs.com/api/v1/services/aigc/image2video/video-synthesis
     * 模型: wan2.2-s2v
     */
    public String generateVideo(DashScopeVideoWanDigitalPeopleOperateDto req) throws Exception {
        String modelName = (req.getModelName() != null && !req.getModelName().isEmpty())
                ? req.getModelName() : "wan2.2-s2v";
        String resolution = (req.getResolution() != null && !req.getResolution().isEmpty())
                ? req.getResolution() : "480P";

        Map<String, Object> body = new HashMap<>();
        body.put("model", modelName);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("image_url", req.getImageUrl());
        inputMap.put("audio_url", req.getAudioUrl());
        body.put("input", inputMap);

        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("resolution", resolution);
        body.put("parameters", parametersMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());
        headers.add("X-DashScope-Async", "enable");

        boolean needOssResolve = (req.getImageUrl() != null && req.getImageUrl().startsWith("oss://"))
                || (req.getAudioUrl() != null && req.getAudioUrl().startsWith("oss://"));
        if (needOssResolve) {
            headers.add("X-DashScope-OssResourceResolve", "enable");
        }

        HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange(
                "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2video/video-synthesis",
                HttpMethod.POST,
                reqEntity,
                String.class);

        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });

        Map<String, Object> outputMap = (Map<String, Object>) respMap.get("output");
        String taskId = (String) outputMap.get("task_id");
        return taskId;
    }

    @PostMapping("/generate")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> generateVideo(@RequestBody OpsSecureDto reqDto,
                                                       HttpServletRequest request) throws Exception {
        try {
            DashScopeVideoWanDigitalPeopleOperateDto req = transfer.recv(reqDto, DashScopeVideoWanDigitalPeopleOperateDto.class);
            String taskId = generateVideo(req);
            return transfer.success(taskId);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
