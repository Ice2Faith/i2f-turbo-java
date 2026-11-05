package i2f.springboot.ops.dashscope.tool;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.io.stream.StreamUtil;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTaskController;
import i2f.springboot.ops.dashscope.controller.DashScopeOpsTmpFileController;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import i2f.springboot.ops.openai.async.AsyncTaskMessage;
import i2f.springboot.ops.openai.async.AsyncTaskResolver;
import i2f.springboot.ops.openai.data.OpenAiMeta;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.properties.OpenAiOpsProperties;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import i2f.springboot.ops.openai.tts.qwen.QwenAudioTtsWebSocket;
import i2f.springboot.ops.openai.util.OpenAiOptionsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/11/5 14:29
 * @desc
 */
@McpServerExpose(false)
@ToolIntent(items = @ToolIntentItem(value = "qwen_tts", description = "提供基于阿里云的TTS文字语音合成能力"))
@Conditional(DashScopeOpsController.DashScopeCondition.class)
@ConditionalOnExpression("${ai.tools.qwen_tts.enable:true}")
@Component
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class DashScopeQwenTtsTools implements AsyncTaskResolver {
    public static final String TASK_TYPE = "dashscope_qwen_tts";

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static final String DEFAULT_MODEL = "qwen-audio-3.0-tts-plus";
    public static final String DEFAULT_VOICE = "longanlingxin";

    @Autowired
    private OpenAiOpsProperties openAiOpsProperties;

    @Autowired(required = false)
    private DashScopeOpsTmpFileController tmpFileController;

    @Autowired(required = false)
    private DashScopeOpsTaskController taskController;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Value("${ai.tools.qwen_tts.model:qwen-audio-3.0-tts-plus}")
    protected String model = DEFAULT_MODEL;

    @Value("${ai.tools.qwen_tts.voice:longanlingxin}")
    protected String voice = DEFAULT_MODEL;

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to voice, output mp3 audio file, Note: support chinese text content."
    )
    public AsyncTaskMessage text_to_audio_qwen(
            @ToolParam(value = "content", description = "the content, support markdown format")
            String content
    ) throws Exception {
        if (tmpFileTools == null) {
            throw new IllegalStateException("system not enable tmp file.");
        }
        String modelName = this.model;
        if (modelName == null || modelName.isEmpty()) {
            modelName = DEFAULT_MODEL;
        }
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        OpenAiOpsProperties.OpenAiOptions endpoint = OpenAiOptionsUtil.getOrDefaultDashScopeEndpoint(req, openAiOpsProperties);
        if (endpoint == null || !endpoint.isEnable()) {
            throw new IllegalStateException("not dashscope openai endpoint config!");
        }

        String baseUrl = endpoint.getBaseUrl();
        if (!baseUrl.contains("aliyuncs.com")) {
            throw new IllegalArgumentException("this tool only support run in aliyun provided model.");
        }

        String taskId = UUID.randomUUID().toString().replace("-", "");

        TmpFileTools.UploadTmpFileMetadata statusMetadata = tmpFileTools.saveFile(new ByteArrayInputStream(
                        getTaskStatusJson(
                                "PENDING", "", ""
                        ).getBytes(StandardCharsets.UTF_8)),
                "status.txt");

        File statusFile = tmpFileTools.getFileByUrl(statusMetadata.getFileUrl());
        Thread thread = new Thread(() -> {
            try {
                StreamUtil.writeString(getTaskStatusJson(
                        "RUNNING", "", ""
                ), statusFile);

                List<String> inputs = Arrays.asList(content.split("\n+"));
                QwenAudioTtsWebSocket socket = QwenAudioTtsWebSocket.create(endpoint.getBaseUrl(),
                        endpoint.getApiKey(),
                        inputs);
                String model = this.model;
                if (model != null && !model.isEmpty()) {
                    socket.setModel(model);
                }

                String voice = this.voice;
                if (voice != null && !voice.isEmpty()) {
                    socket.setVoice(voice);
                }

                File mp3File = socket.start()
                        .await();
                TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new FileInputStream(mp3File), "audio.mp3");

                StreamUtil.writeString(getTaskStatusJson(
                        "SUCCEEDED", metadata.getFileUrl(), ""
                ), statusFile);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    StreamUtil.writeString(getTaskStatusJson(
                            "FAILURE", "", e.getMessage()
                    ), statusFile);
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("qwen-tts-generator");
        thread.start();


        AsyncTaskItem taskItem = new AsyncTaskItem();
        taskItem.setDescription("Qwen-语音合成");
        taskItem.setStatus(AsyncTaskItem.Status.PENDING);
        taskItem.setType(TASK_TYPE);
        taskItem.setTaskId(taskId);
        taskItem.setTaskParameters(new HashMap<>());
        taskItem.getTaskParameters().put("query", statusMetadata.getFileUrl());
        taskItem.setResultType(AsyncTaskItem.ResultTypes.AUDIO);
        taskItem.setResult(new ArrayList<>());

        AsyncTaskMessage ret = new AsyncTaskMessage();
        ret.setContent("result audio has show to user.");
        ret.setList(new ArrayList<>());
        ret.getList().add(taskItem);
        return ret;
    }

    public String getTaskStatusJson(String status, String file, String msg) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        map.put("file", file);
        map.put("msg", msg);
        return tmpFileTools.getJsonSerializer().serialize(map);
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

        Map<String, Object> parameters = item.getTaskParameters();
        String statusFileUrl = (String) parameters.get("query");

        File statusFile = tmpFileTools.getFileByUrl(statusFileUrl);
        String json = StreamUtil.readString(statusFile);

        Map<String, Object> statusMap = tmpFileTools.getJsonSerializer().deserializeAsMap(json);
        String taskStatus = (String) statusMap.get("status");
        taskStatus = taskStatus.trim();

        if ("PENDING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.PENDING);
        } else if ("RUNNING".equals(taskStatus)) {
            item.setStatus(AsyncTaskItem.Status.RUNNING);
        } else if ("SUCCEEDED".equals(taskStatus)) {
            List<TmpFileTools.UploadTmpFileMetadata> files = new ArrayList<>();

            String audioFileUrl = (String) statusMap.get("file");
            TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.getFileMetadataByUrl(audioFileUrl);

            files.add(metadata);

            item.setStatus(AsyncTaskItem.Status.SUCCESS);
            item.setResultType(AsyncTaskItem.ResultTypes.AUDIO);
            item.setResult(files.isEmpty() ? null : files.get(0));
        } else {
            item.setStatus(AsyncTaskItem.Status.FAILURE);

            String msg = (String) statusMap.get("msg");
            item.setError(msg);
        }

        return item;
    }

}
