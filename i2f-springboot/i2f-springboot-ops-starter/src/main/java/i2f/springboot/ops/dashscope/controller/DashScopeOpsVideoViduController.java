package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.dashscope.data.DashScopeVideoViduOperateDto;
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
 * @date 2026/4/28 19:09
 * @desc
 */
@ConditionalOnClass(RestTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/dashscope/video/vidu")
public class DashScopeOpsVideoViduController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private DashScopeOpsController controller;

    public String videoHappyHorse(DashScopeVideoViduOperateDto req) throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("model", req.getModelName());
        body.put("input", req.getInput());
        body.put("parameters", req.getParameters());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());
        headers.add("X-DashScope-Async", "enable");
        headers.add("X-DashScope-OssResourceResolve", "enable");


        HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange("https://dashscope.aliyuncs.com/api/v1/services/aigc/video-generation/video-synthesis",
                HttpMethod.POST,
                reqEntity,
                String.class);


        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });

        Map<String, Object> outputMap = (Map<String, Object>) respMap.get("output");
        String taskStatus = (String) outputMap.get("task_status");
        String taskId = (String) outputMap.get("task_id");

        return taskId;
    }

    @PostMapping("/generate")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> videoInsteadPeople(@RequestBody OpsSecureDto reqDto,
                                                            HttpServletRequest request) throws Exception {
        try {
            DashScopeVideoViduOperateDto req = transfer.recv(reqDto, DashScopeVideoViduOperateDto.class);

            String taskId = videoHappyHorse(req);
            return transfer.success(taskId);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
