package i2f.springboot.ops.dashscope.tool;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsImageKlingText2ImageController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTaskController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTmpFileController;
import i2f.springboot.ops.dashscope.data.DashScopeImageKlingText2ImageOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeMeta;
import i2f.springboot.ops.dashscope.data.DashScopeTaskOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeUploadOperateDto;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import i2f.springboot.ops.openai.async.AsyncTaskMessage;
import i2f.springboot.ops.openai.async.AsyncTaskResolver;
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
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/1 9:02
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value = "kling_t2i", description = "提供基于可灵的文生图能力"))
@Conditional(DashScopeOpsController.DashScopeCondition.class)
@ConditionalOnExpression("${ai.tools.kling-t2i.enable:true}")
@Component
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class DashScopeImageKlingText2ImageTools implements AsyncTaskResolver {
    public static final String TASK_TYPE = "dashscope_kling_text2image";

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static final String DEFAULT_MODEL = "kling/kling-v3-omni-image-generation";

    @Autowired(required = false)
    private DashScopeOpsImageKlingText2ImageController imageKlingText2ImageController;

    @Autowired(required = false)
    private DashScopeOpsTmpFileController tmpFileController;

    @Autowired(required = false)
    private DashScopeOpsTaskController taskController;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Value("${ai.tools.kling-t2i.model:kling/kling-v3-omni-image-generation}")
    protected String model = DEFAULT_MODEL;

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to image, output png image file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public AsyncTaskMessage text_to_image_kling(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode image, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        if (imageKlingText2ImageController == null) {
            throw new IllegalStateException("system not enable dashscope text2image endpoint.");
        }
        if (tmpFileTools == null) {
            throw new IllegalStateException("system not enable tmp file.");
        }
        String modelName = this.model;
        if (modelName == null || modelName.isEmpty()) {
            modelName = DEFAULT_MODEL;
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

        DashScopeImageKlingText2ImageOperateDto dto = new DashScopeImageKlingText2ImageOperateDto();
        dto.setModelName(modelName);
        dto.setInput(new HashMap<>());
        List<Map<String, Object>> contentList = new ArrayList<>();
        Map<String, Object> textContent = new HashMap<>();
        textContent.put("text", content);
        contentList.add(textContent);

        if (reference_image_url != null) {
            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("image", reference_image_url);
            contentList.add(imageContent);
        }

        dto.getInput().put("messages", contentList);

        dto.setParameters(new HashMap<>());
        dto.getParameters().put("n", 3);
        dto.getParameters().put("aspect_ratio", "16:9");
        dto.getParameters().put("resolution", "1k");
        dto.getParameters().put("watermark", false);
        if (portrait_mode) {
            dto.getParameters().put("aspect_ratio", "9:16");
        }


        DashScopeMeta dashScopeMeta = new DashScopeMeta();
        dashScopeMeta.setApiKey(meta.getApiKey());
        dto.setMeta(dashScopeMeta);
        String taskId = imageKlingText2ImageController.imageKling(dto);

        AsyncTaskItem taskItem = new AsyncTaskItem();
        taskItem.setStatus(AsyncTaskItem.Status.PENDING);
        taskItem.setType(TASK_TYPE);
        taskItem.setTaskId(taskId);
        taskItem.setTaskParameters(new HashMap<>());
        taskItem.getTaskParameters().put("model", modelName);
        taskItem.setResultType(AsyncTaskItem.ResultTypes.IMAGE_LIST);
        taskItem.setResult(new ArrayList<>());

        AsyncTaskMessage ret = new AsyncTaskMessage();
        ret.setContent("result images(s) has show to user.");
        ret.setList(new ArrayList<>());
        ret.getList().add(taskItem);
        return ret;
    }

    @Override
    public boolean support(AsyncTaskItem item, OpenAiMeta meta) throws Exception {
        String baseUrl = meta.getBaseUrl();
        if (!baseUrl.contains("aliyuncs.com")) {
            return false;
        }
        if (!TASK_TYPE.equals(item.getType())) {
            return false;
        }
        return true;
    }

    @Override
    public AsyncTaskItem resolve(AsyncTaskItem item, OpenAiMeta meta) throws Exception {
        if (!support(item, meta)) {
            return item;
        }
        DashScopeTaskOperateDto dto = new DashScopeTaskOperateDto();
        DashScopeMeta dashScopeMeta = new DashScopeMeta();
        dashScopeMeta.setApiKey(meta.getApiKey());
        dto.setMeta(dashScopeMeta);

        Map<String, Object> taskParameters = item.getTaskParameters();
        dto.setModelName((String) taskParameters.get("model"));

        dto.setTaskId(item.getTaskId());
        Map<String, Object> resp = taskController.getTaskStatus(dto);

        Map<String, Object> output = (Map<String, Object>) resp.get("output");
        String taskStatus = (String) output.get("task_status");
        if ("PENDING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.PENDING);
        } else if ("RUNNING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.RUNNING);
        } else if ("SUCCEEDED".equals(taskStatus)) {
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

            List<TmpFileTools.UploadTmpFileMetadata> files = new ArrayList<>();
            for (String url : downloadUrlList) {
                String virtualFileName = "image-" + (files.size() + 1) + "-" + (TIME_FORMATTER.format(LocalDateTime.now())) + ".png";
                try {
                    TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new URL(url).openStream(), virtualFileName);
                    files.add(metadata);
                } catch (Exception e) {
                    log.warn("downloadUrl: " + url, e);

                    TmpFileTools.UploadTmpFileMetadata metadata = new TmpFileTools.UploadTmpFileMetadata();
                    metadata.setFileName(virtualFileName);
                    metadata.setFileUrl(url);
                    metadata.setCreateTime(TmpFileTools.CREATE_FORMATTER.format(LocalDateTime.now()));
                    files.add(metadata);
                }
            }

            item.setStatus(AsyncTaskItem.Status.SUCCESS);
            item.setResultType(AsyncTaskItem.ResultTypes.IMAGE_LIST);
            item.setResult(files);
        } else {
            item.setStatus(AsyncTaskItem.Status.FAILURE);
            item.setError(output.get("output") + ": " + output.get("message"));
        }
        return item;
    }
}
