package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.dashscope.data.DashScopeTaskOperateDto;
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
@RequestMapping("/ops/dashscope/task")
public class DashScopeOpsTaskController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DashScopeOpsController controller;


    public Map<String, Object> getTaskStatus(DashScopeTaskOperateDto req) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());

        HttpEntity<Object> reqEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange("https://dashscope.aliyuncs.com/api/v1/tasks/" + req.getTaskId(),
                HttpMethod.GET,
                reqEntity,
                String.class);
        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });
        return respMap;
    }

    @PostMapping("/status")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> getPolicy(@RequestBody OpsSecureDto reqDto,
                                                   HttpServletRequest request) throws Exception {
        try {
            DashScopeTaskOperateDto req = transfer.recv(reqDto, DashScopeTaskOperateDto.class);

            Map<String, Object> respMap = getTaskStatus(req);
            return transfer.success(respMap);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
