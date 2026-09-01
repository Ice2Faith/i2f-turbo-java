package i2f.springboot.ops.dashscope.tool;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsImageWanText2ImageController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTmpFileController;
import i2f.springboot.ops.dashscope.data.DashScopeImageWanText2ImageOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeMeta;
import i2f.springboot.ops.dashscope.data.DashScopeUploadOperateDto;
import i2f.springboot.ops.openai.data.OpenAiMeta;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/1 9:02
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value = "dashscope_t2i", description = "提供基于阿里云的文生图能力"))
@ConditionalOnExpression("${ai.tools.dashscope-t2i.enable:true}")
@Component
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class DashScopeImageWanText2ImageTools {

    @Autowired(required = false)
    private DashScopeOpsImageWanText2ImageController imageWanText2ImageController;

    @Autowired(required = false)
    private DashScopeOpsTmpFileController tmpFileController;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Value("${ai.tools.dashscope-t2i.model:qwen-image-2.0-pro}")
    protected String model="qwen-image-2.0-pro";

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to image, output png image file, Note: support chinese text content."
    )
    public TmpFileTools.FileAttachMessage text_to_image_wan(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        if (imageWanText2ImageController == null) {
            throw new IllegalStateException("system not enable dashscope text2image endpoint.");
        }
        if (tmpFileTools == null) {
            throw new IllegalStateException("system not enable tmp file.");
        }
        String modelName=this.model;
        if(modelName==null || modelName.isEmpty()){
            modelName="qwen-image-2.0-pro";
        }
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        OpenAiMeta meta = req.getMeta();
        String baseUrl = meta.getBaseUrl();
        if (!baseUrl.contains("aliyuncs.com")) {
            throw new IllegalArgumentException("this tool only support run in aliyun provided model.");
        }

        if (reference_image_url != null) {
            // 处理临时文件，转换为阿里云临时文件上传
            if (reference_image_url.startsWith(TmpFileTools.PROTOCOL + "://")) {
                if (tmpFileController == null) {
                    throw new IllegalStateException("system not enable dashscope tmp file endpoint");
                }
                File file = tmpFileTools.getFileByUrl(reference_image_url);
                DashScopeUploadOperateDto dto = new DashScopeUploadOperateDto();
                dto.setModelName(modelName);
                DashScopeMeta dashScopeMeta = new DashScopeMeta();
                dashScopeMeta.setApiKey(meta.getApiKey());
                dto.setMeta(dashScopeMeta);
                reference_image_url = tmpFileController.uploadFile(dto, file);
            }
        }

        DashScopeImageWanText2ImageOperateDto dto = new DashScopeImageWanText2ImageOperateDto();
        dto.setCount(3);
        dto.setExtendPrompt(false);
        dto.setPrompt(content);
        dto.setImageUrl(reference_image_url);
        dto.setSize("2688*1536");
        dto.setWatermark(false);
        dto.setModelName(modelName);
        DashScopeMeta dashScopeMeta = new DashScopeMeta();
        dashScopeMeta.setApiKey(meta.getApiKey());
        dto.setMeta(dashScopeMeta);
        Map<String, Object> resp = imageWanText2ImageController.imageText2Image(dto);

        List<String> downloadUrlList = new ArrayList<>();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
        if (choices == null) {
            choices = new ArrayList<>();
        }

        for (Map<String, Object> choice : choices) {
            Map<String, Object> choiceMessage = (Map<String, Object>) choice.get("message");
            List<Map<String, Object>> choiceMessageContents = (List<Map<String, Object>>) choiceMessage.get("content");
            if (choiceMessageContents == null) {
                continue;
            }
            for (Map<String, Object> choiceMessageContent : choiceMessageContents) {
                Object url = choiceMessageContent.get("image");
                if (url != null) {
                    downloadUrlList.add(String.valueOf(url));
                }
            }
        }

        TmpFileTools.FileAttachMessage ret = new TmpFileTools.FileAttachMessage();
        ret.setContent(null);
        ret.setFiles(new ArrayList<>());

        StringBuilder builder = new StringBuilder();
        builder.append("result image(s) has upload, will send with after user message.\n");

        for (String url : downloadUrlList) {
            try {
                String virtualFileName = "image-" + (ret.getFiles().size() + 1) + ".png";
                TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new URL(url).openStream(), virtualFileName);
                ret.getFiles().add(metadata);

                Map<String, Object> map = metadata.toMap();
                builder.append("--------------\n");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
            } catch (Exception e) {
                log.warn("downloadUrl: "+url, e);
            }
        }

        ret.setContent(builder.toString());
        return ret;
    }
}
