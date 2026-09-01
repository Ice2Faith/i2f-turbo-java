package i2f.springboot.ops.dashscope.tool;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTaskController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTmpFileController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsVideoPixVerseController;
import i2f.springboot.ops.dashscope.data.DashScopeMeta;
import i2f.springboot.ops.dashscope.data.DashScopeTaskOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeUploadOperateDto;
import i2f.springboot.ops.dashscope.data.DashScopeVideoPixVerseOperateDto;
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
@ToolIntent(items = @ToolIntentItem(value = "pixverse_t2v", description = "提供基于爱诗（抖音）的文生视频能力"))
@Conditional(DashScopeOpsController.DashScopeCondition.class)
@ConditionalOnExpression("${ai.tools.pixverse-t2v.enable:true}")
@Component
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class DashScopeVideoPixVerseText2VideoTools implements AsyncTaskResolver {
    public static final String TASK_TYPE = "dashscope_pixverse_text2video";

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static final String DEFAULT_MODEL = "pixverse/pixverse-v6-t2v";
    public static final String DEFAULT_MODEL_I2V = "pixverse/pixverse-v6-it2v";

    @Autowired(required = false)
    private DashScopeOpsVideoPixVerseController videoPixVerseController;

    @Autowired(required = false)
    private DashScopeOpsTmpFileController tmpFileController;

    @Autowired(required = false)
    private DashScopeOpsTaskController taskController;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Value("${ai.tools.pixverse-t2v.model:pixverse/pixverse-v6-t2v}")
    protected String model = DEFAULT_MODEL;

    @Value("${ai.tools.pixverse-t2v.model-i2v:pixverse/pixverse-v6-it2v}")
    protected String modelImage2Video = DEFAULT_MODEL;

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to video, output mp4 video file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public AsyncTaskMessage text_to_video_pixverse(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode video, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        if (videoPixVerseController == null) {
            throw new IllegalStateException("system not enable dashscope text2video endpoint.");
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

        if (reference_image_url != null && !reference_image_url.isEmpty()) {
            modelName = this.modelImage2Video;
            if (modelName == null || modelName.isEmpty()) {
                modelName = DEFAULT_MODEL_I2V;
            }
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


        DashScopeVideoPixVerseOperateDto dto = new DashScopeVideoPixVerseOperateDto();
        dto.setModelName(modelName);
        dto.setInput(new HashMap<>());

        dto.getInput().put("prompt", content);
        if (reference_image_url != null && !reference_image_url.isEmpty()) {
            List<Map<String, Object>> mediaList = new ArrayList<>();
            dto.getInput().put("media", mediaList);

            Map<String, Object> firstFrameMedia = new HashMap<>();
            mediaList.add(firstFrameMedia);

            firstFrameMedia.put("type", "image_url");
            firstFrameMedia.put("url", reference_image_url);
        }

        dto.setParameters(new HashMap<>());
        if (reference_image_url != null && !reference_image_url.isEmpty()) {
            dto.getParameters().put("duration", 10);
            dto.getParameters().put("watermark", false);
            dto.getParameters().put("audio", true);
            dto.getParameters().put("resolution", "720P");

        } else {
            dto.getParameters().put("duration", 10);
            dto.getParameters().put("watermark", false);
            dto.getParameters().put("audio", true);
            dto.getParameters().put("size", "1280*720");
            if (portrait_mode) {
                dto.getParameters().put("size", "720*1280");
            }
        }


        DashScopeMeta dashScopeMeta = new DashScopeMeta();
        dashScopeMeta.setApiKey(meta.getApiKey());
        dto.setMeta(dashScopeMeta);
        String taskId = videoPixVerseController.videoPixVerse(dto);

        AsyncTaskItem taskItem = new AsyncTaskItem();
        taskItem.setDescription("爱诗-视频生成");
        taskItem.setStatus(AsyncTaskItem.Status.PENDING);
        taskItem.setType(TASK_TYPE);
        taskItem.setTaskId(taskId);
        taskItem.setTaskParameters(new HashMap<>());
        taskItem.getTaskParameters().put("model", modelName);
        taskItem.setResultType(AsyncTaskItem.ResultTypes.VIDEO);
        taskItem.setResult(new ArrayList<>());

        AsyncTaskMessage ret = new AsyncTaskMessage();
        ret.setContent("result video has show to user.");
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
        item.setRawResult(resp);

        Map<String, Object> output = (Map<String, Object>) resp.get("output");
        String taskStatus = (String) output.get("task_status");
        if ("PENDING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.PENDING);
        } else if ("RUNNING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.RUNNING);
        } else if ("SUCCEEDED".equals(taskStatus)) {
            List<String> downloadUrlList = new ArrayList<>();

            Object taskUrl = output.get("video_url");
            if (taskUrl != null) {
                downloadUrlList.add(String.valueOf(taskUrl));
            }


            List<TmpFileTools.UploadTmpFileMetadata> files = new ArrayList<>();
            for (String url : downloadUrlList) {
                String virtualFileName = "pixverse-video-" + (files.size() + 1) + "-" + (TIME_FORMATTER.format(LocalDateTime.now())) + ".png";
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
            item.setResultType(AsyncTaskItem.ResultTypes.VIDEO);
            item.setResult(files.isEmpty() ? null : files.get(0));
        } else {
            item.setStatus(AsyncTaskItem.Status.FAILURE);
            item.setError(output.get("output") + ": " + output.get("message"));
        }
        return item;
    }
}
