package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.dashscope.data.DashScopeImageText2ImageOperateDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/ops/dashscope/image/text2image")
public class DashScopeOpsImageText2ImageController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private DashScopeOpsController controller;

    public Map<String, Object> imageText2Image(DashScopeImageText2ImageOperateDto req) throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("model", req.getModelName());
        Map<String, Object> inputMap = new HashMap<>();
        body.put("input", inputMap);


        List<Map<String,Object>> messages=new ArrayList<>();
        inputMap.put("messages",messages);

        Map<String,Object> messageMap=new HashMap<>();
        messages.add(messageMap);

        messageMap.put("role","user");

        List<Map<String,Object>> contents=new ArrayList<>();
        messageMap.put("content",contents);

        Map<String,Object> contentMap=new HashMap<>();
        contents.add(contentMap);
        contentMap.put("text",req.getPrompt());

        String imageUrl = req.getImageUrl();
        if(imageUrl!=null){
            imageUrl=imageUrl.trim();
        }

        if(imageUrl!=null && !imageUrl.isEmpty()){
            Map<String,Object> imageContentMap=new HashMap<>();
            contents.add(imageContentMap);
            imageContentMap.put("image",imageUrl);
        }

        Map<String, Object> parametersMap = new HashMap<>();
        body.put("parameters", parametersMap);

        String negativePrompt = req.getNegativePrompt();
        if(negativePrompt!=null){
            negativePrompt=negativePrompt.trim();
            if(!negativePrompt.isEmpty()){
                parametersMap.put("negative_prompt",negativePrompt);
            }
        }
        parametersMap.put("prompt_extend",req.isExtendPrompt());
        parametersMap.put("watermark",req.isWatermark());
        parametersMap.put("size",req.getSize());
        parametersMap.put("n",req.getCount());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + req.getMeta().getApiKey());
        if (imageUrl!=null && imageUrl.startsWith("oss://")) {
            headers.add("X-DashScope-OssResourceResolve", "enable");
        }

        HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> respEntity = controller.getRestTemplate().exchange("https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation",
                HttpMethod.POST,
                reqEntity,
                String.class);


        String respJson = respEntity.getBody();
        Map<String, Object> respMap = objectMapper.readValue(respJson, new TypeReference<Map<String, Object>>() {
        });

        Map<String, Object> outputMap = (Map<String, Object>) respMap.get("output");
        if(outputMap==null){
            throw new IllegalArgumentException(respMap.get("code")+": "+ respMap.get("message"));
        }

        return outputMap;
    }

    @PostMapping("/generate")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> imageText2Image(@RequestBody OpsSecureDto reqDto,
                                                            HttpServletRequest request) throws Exception {
        try {
            DashScopeImageText2ImageOperateDto req = transfer.recv(reqDto, DashScopeImageText2ImageOperateDto.class);

            Map<String, Object> output = imageText2Image(req);
            return transfer.success(output);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
