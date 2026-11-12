package i2f.springboot.ops.openai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.openai.model.data.*;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.prompts.AgentsPrompt;
import i2f.ai.std.rag.RagTools;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.intent.IToolIntent;
import i2f.ai.std.tool.intent.ToolIntentHelper;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.image.common.ImageCompressor;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.resp.ApiResp;
import i2f.spring.web.rest.SpringWebHttpProcessor;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.openai.async.AsyncTaskDispatcher;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import i2f.springboot.ops.openai.async.AsyncTaskMessage;
import i2f.springboot.ops.openai.data.*;
import i2f.springboot.ops.openai.data.message.EchoOpenAiToolMessage;
import i2f.springboot.ops.openai.data.message.OpsOpenAiConsts;
import i2f.springboot.ops.openai.properties.OpenAiOpsProperties;
import i2f.springboot.ops.openai.rag.MemoryTools;
import i2f.springboot.ops.openai.tool.impl.*;
import i2f.springboot.ops.openai.tool.impl.a2a.AgentTools;
import i2f.web.servlet.ServletFileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/30 19:45
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ops.open-ai.enable:false}")
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties(OpenAiOpsProperties.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping(OpsConsts.SPEL_BASE_URL + "/open-ai")
public class OpenAiOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HostIdHelper hostIdHelper;

    @Autowired
    private HostIdProxyHelper hostIdProxyHelper;

    @Autowired(required = false)
    private RagWorker worker;

    private RestTemplate restTemplate = createRestTemplate();

    private ExecutorService pool = createPool(Runtime.getRuntime().availableProcessors() * 2 + 2,
            Runtime.getRuntime().availableProcessors() * 4 + 2,
            16);

    private ExecutorService toolPool = createPool(Runtime.getRuntime().availableProcessors() * 2 + 2,
            Runtime.getRuntime().availableProcessors() * 4 + 2,
            512);

    @Autowired(required = false)
    private ToolManager toolManager;

    @Autowired(required = false)
    private SkillsTools skillsTools;

    @Autowired(required = false)
    private McpProviderTools mcpProviderTools;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Autowired(required = false)
    private AgentTools agentTools;

    @Autowired
    private OpenAiOpsProperties properties;

    @Autowired
    private AsyncTaskDispatcher asyncTaskDispatcher;

    protected SecureRandom random = new SecureRandom();

    private RestTemplate createRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(5))
                .build();
    }

    public static ExecutorService createPool(int coreSize, int maxSize, int queueSize) {
        return new ThreadPoolExecutor(coreSize, maxSize,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Open Ai")
                .subTitle("Open Ai 兼容的 Ai 对话工具")
                .icon("el-icon-cpu")
                .href("./open-ai/index.html")
                .group(OpsHomeMenuGroup.AI)
        );
    }

    public void assertHostId(AppOperationDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    public String extraStandardBaseUrl(String url) {
        if (url == null) {
            return null;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    @PostMapping("/models")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> models(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            String url = req.getMeta().getBaseUrl();
            url = extraStandardBaseUrl(url);
            url = url + "/models";
            String json = restTemplate.execute(url, HttpMethod.GET, request -> {
                request.getHeaders().add("Authorization", "Bearer " + req.getMeta().getApiKey());
                request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }, new ResponseExtractor<String>() {
                @Override
                public String extractData(ClientHttpResponse response) throws IOException {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line).append("\n");
                        }
                    }
                    return builder.toString();
                }
            });
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
            return transfer.success(list);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/tmp-file/upload")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> uploadTmpFile(MultipartFile file, OpsSecureDto reqDto,
                                                       HttpServletRequest request) throws Exception {
        try {
            if (tmpFileTools == null) {
                throw new IllegalStateException("manager not enable tmp file upload feature.");
            }
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);

            File tmpFile = File.createTempFile("upload-" + (UUID.randomUUID().toString().replace("-", "")), ".tmp");
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
                TmpFileTools.UploadTmpFileMetadata resp = tmpFileTools.saveFile(new FileInputStream(tmpFile), file.getOriginalFilename());
                return transfer.success(resp);
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

    @PostMapping("/tmp-file/download")
    public void downloadTmpFile(@RequestBody OpsSecureDto reqDto,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        try {
            if (tmpFileTools == null) {
                throw new IllegalStateException("manager not enable tmp file upload feature.");
            }
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            String fileUrl = req.getFileUrl();
            File file = tmpFileTools.getFileByUrl(fileUrl);
            if (req.isParsedText()) {
                file = tmpFileTools.parseAsTextFile(file);
            }
            String realName = null;
            try {
                realName = tmpFileTools.getRealFileNameByUrl(fileUrl);
            } catch (Throwable e) {

            }
            if (realName == null || realName.isEmpty()) {
                realName = file.getName();
            }
            if (realName == null || realName.isEmpty()) {
                realName = "download.data";
            }
            if (req.isParsedText()) {
                realName = realName + ".parsed.txt";
            }

            ServletFileUtil.responseAsFileAttachment(new FileInputStream(file), true, realName, null, true, response);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }

    @GetMapping("/tmp-file/inline")
    public void inlineTmpFile(@RequestParam("fileUrl") String fileUrl,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        try {
            if (tmpFileTools == null) {
                throw new IllegalStateException("manager not enable tmp file upload feature.");
            }
            File file = tmpFileTools.getFileByUrl(fileUrl);
            String realName = null;
            try {
                realName = tmpFileTools.getRealFileNameByUrl(fileUrl);
            } catch (Throwable e) {

            }
            if (realName == null || realName.isEmpty()) {
                realName = file.getName();
            }
            if (realName == null || realName.isEmpty()) {
                realName = "download.data";
            }

            String suffix = "";
            int idx = realName.lastIndexOf(".");
            if (idx >= 0) {
                suffix = realName.substring(idx).toLowerCase();
            }

            if (Arrays.asList(
                    ".doc", ".dot", ".dotx", ".dotm", ".rtf",
                    ".wps", ".wpt", ".odt", ".ott", ".fodt", ".epub"
            ).contains(suffix) || Arrays.asList(
                    ".xls", ".xlsm", ".xlt", ".xltm", ".tsv",
                    ".ods", ".ots", ".et", ".ett"
            ).contains(suffix) || Arrays.asList(
                    ".ppt", ".pps", ".dps", ".odp", ".otp", ".ppsx"
            ).contains(suffix)) {
                ApiResp<File> outputResp = OfficeFormatUtil.convertOfficeFile(file, false);
                if (outputResp.isSuccess()) {
                    file = outputResp.getData();
                    realName = file.getName();
                }
            }

            ServletFileUtil.responseAsFileAttachment(new FileInputStream(file), true, realName, null, false, response);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }

    @PostMapping("/tool/tags")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> toolTags(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            Set<String> tags = new TreeSet<>();
            if (toolManager != null) {
                List<ToolDefinition> tools = toolManager.listTools();
                if (tools != null) {
                    for (ToolDefinition tool : tools) {
                        Set<String> next = tool.getTags();
                        if (next != null) {
                            tags.addAll(next);
                        }
                    }
                }
            }
            return transfer.success(tags);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/async/task/query")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> queryAsyncTask(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            List<AsyncTaskItem> asyncTasks = req.getAsyncTasks();
            for (int i = 0; i < asyncTasks.size(); i++) {
                AsyncTaskItem asyncTaskItem = asyncTasks.get(i);
                AsyncTaskItem ret = asyncTaskDispatcher.query(asyncTaskItem, req.getMeta());
                asyncTasks.set(i, ret);
            }
            return transfer.success(asyncTasks);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    public OpenAiMessage convertOpenAiUserMessage(OpenAiMessageVo item,
                                                  OpenAiOperateDto req,
                                                  AtomicBoolean hasAttachFiles) throws Exception {
        OpenAiMessage user = item.getUser();

        List<Map<String, Object>> filesList = new ArrayList<>();
        List<TmpFileTools.UploadTmpFileMetadata> attachFiles = item.getAttachFiles();
        if (attachFiles != null && !attachFiles.isEmpty()) {
            if (req.isEnableVisionImage()) {
                // 如果是视觉模型，那就是支持直接识别图片，那就直接进行base64编码图片内嵌就行
                Map<Integer, TmpFileTools.UploadTmpFileMetadata> imageFiles = new HashMap<>();

                for (int i = 0; i < attachFiles.size(); i++) {
                    TmpFileTools.UploadTmpFileMetadata map = attachFiles.get(i);
                    filesList.add(map.toMap());

                    String suffix = "";
                    String fileName = map.getFileName();
                    int idx = fileName.lastIndexOf(".");
                    if (idx >= 0) {
                        suffix = fileName.substring(idx).toLowerCase();
                    }
                    if (Arrays.asList(".png", ".jpg", ".jpeg", ".webp", ".bmp").contains(suffix)) {
                        imageFiles.put(i, map);
                    }
                }

                if (!imageFiles.isEmpty()) {
                    OpenAiRichUserMessage richUser = new OpenAiRichUserMessage(new ArrayList<>());

                    String content = user.content();
                    richUser.getContent().add(new OpenAiUserContentText(content));

                    for (Map.Entry<Integer, TmpFileTools.UploadTmpFileMetadata> entry : imageFiles.entrySet()) {
                        Integer index = entry.getKey();
                        TmpFileTools.UploadTmpFileMetadata metadata = entry.getValue();
                        Map<String, Object> map = filesList.get(index);
                        // 将消息的位置添加到附件内容中，为了模型能够对应图片
                        map.put("imageIndex", richUser.getContent().size());

                        // 对图片进行压缩编码添加到消息中
                        String imageUrl = metadata.getFileUrl();
                        File imageFile = tmpFileTools.getFileByUrl(imageUrl);
                        File outputFile = new File(imageFile.getParentFile(), "compress.jpg");
                        File compressFile = outputFile;
                        // 如果输出图片不存在，那说明没有被压缩过，才进行压缩
                        // 如果输出图片已经存在，那就说明之前已经被压缩过了，直接使用就行
                        if (!outputFile.exists()) {
                            OpenAiOpsProperties.VisionOptions vision = properties.getVision();
                            double quality = ImageCompressor.compressImage(imageFile, outputFile,
                                    vision.getImageMaxSizeKb(), vision.getImageMaxDimension());
                            compressFile = quality < 0 ? imageFile : outputFile;
                        }
                        String dataUrl = ImageCompressor.imageFileToBase64DataUrl(compressFile);
                        richUser.getContent().add(new OpenAiUserContentImageUrl(dataUrl));
                    }

                    user = richUser;
                }
            }

            // 如果有上传文件，则动态追加到用户消息中
            String appendText = "\n\n<upload_files>\n"
                    + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(filesList)
                    + "\n</upload_files>";

            if (user instanceof OpenAiRichUserMessage) {
                OpenAiRichUserMessage msg = (OpenAiRichUserMessage) user;
                List<OpenAiUserContent> list = msg.getContent();
                for (OpenAiUserContent contentItem : list) {
                    if (contentItem instanceof OpenAiUserContentText) {
                        OpenAiUserContentText textContent = (OpenAiUserContentText) contentItem;
                        textContent.setText(textContent.getText()
                                + appendText
                        );
                    }
                }
            } else if (user instanceof OpenAiUserMessage) {
                OpenAiUserMessage msg = (OpenAiUserMessage) user;
                msg.setContent(msg.getContent()
                        + appendText
                );
            }

            hasAttachFiles.set(true);
        }
        return user;
    }

    /**
     * 判断当前请求消息列表是否处于可以插入插话的安全临界点
     * <p>
     * 安全临界点：tool_calls 与 tool 消息成对完整之后，也就是最后一条消息不能是等待工具响应的 assistant(tool_calls)，
     * 否则会造成请求报文非法（assistant.tool_calls 缺少对应的 tool 响应）
     *
     * @param messages 当前的请求消息列表
     * @return true 表示可以插入
     */
    public boolean isInterjectSafeBoundary(List<OpenAiMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return true;
        }
        OpenAiMessage lastMsg = messages.get(messages.size() - 1);
        if (lastMsg instanceof OpenAiAssistantMessage) {
            List<OpenAiToolCall> calls = ((OpenAiAssistantMessage) lastMsg).getTool_calls();
            return calls == null || calls.isEmpty();
        }
        return true;
    }

    /**
     * 判断插话是否可以插入当前请求消息列表
     * <p>
     * 除了安全临界点之外，还要求插话的类型受支持且消息体不为空，
     * 只有可以插入时才回显给前端，避免前端显示了实际并未参与本轮请求的消息
     *
     * @param messages 当前的请求消息列表
     * @param item     插话消息
     * @return true 表示可以插入
     */
    public boolean canAppendInterject(List<OpenAiMessage> messages, OpenAiMessageVo item) {
        if (messages == null || item == null || item.getType() == null || item.getType().isEmpty()) {
            return false;
        }
        boolean supported = false;
        if (OpenAiConsts.USER.equals(item.getType())) {
            supported = item.getUser() != null;
        } else if (OpenAiConsts.SYSTEM.equals(item.getType())) {
            supported = item.getSystem() != null;
        } else if (OpenAiConsts.ASSISTANT.equals(item.getType())) {
            supported = item.getAssistant() != null;
        } else if (OpenAiConsts.TOOL.equals(item.getType())) {
            supported = item.getTool() != null;
        }
        return supported && isInterjectSafeBoundary(messages);
    }

    /**
     * 回显插话消息
     * <p>
     * 由服务端在真正插入插话的时机发送，前端收到 type 为 ECHO_INTERJECT 的消息后，
     * 按插话自身的类型插入到助手占位消息之前，保证 LLM 的答复消息始终是最后一条
     * 注意：必须在 convertOpenAiUserMessage 之前回显原始内容，附件清单由后端转换时拼接，前端不需要显示
     *
     * @param emitter 当前请求的 SSE 输出
     * @param req     本次请求上下文，用于判断是否需要加密输出
     * @param item    即将插入的插话消息，为未被转换的原始内容
     */
    public void echoInterjectMessage(SseEmitter emitter, OpenAiOperateDto req, OpenAiMessageVo item) {
        if (emitter == null || req == null || item == null) {
            return;
        }
        try {
            String echoJson = objectMapper.writeValueAsString(item);
            OpsSecureReturn<?> resp = null;
            if (req.isEncryptOutput()) {
                resp = transfer.success(echoJson);
            } else {
                resp = OpsSecureReturn.success(echoJson);
            }
            resp.withAttr("type", OpsOpenAiConsts.ECHO_INTERJECT);
            String respJson = objectMapper.writeValueAsString(resp);
            emitter.send(respJson);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 在安全临界点插入插话消息
     * <p>
     * 安全临界点：tool_calls 与 tool 消息成对完整之后，也就是最后一条消息不能是等待工具响应的 assistant(tool_calls)
     * 否则会造成请求报文非法（assistant.tool_calls 缺少对应的 tool 响应），
     * 此时放弃本次插入，由下一轮请求继续携带该插话
     *
     * @param messages       当前的请求消息列表，插话成功之后追加在其末尾
     * @param item           插话消息，为 null 或者 type 为空时直接忽略
     * @param req            本次请求上下文，用户消息转换（附件编码）时使用
     * @param hasAttachFiles 是否携带附件，用户消息转换时回填
     * @return true 表示插话已插入；false 表示不在安全边界或者消息类型不支持，未插入
     * @throws Exception 用户消息转换（图片编码等）异常
     */
    public boolean appendInterjectMessage(List<OpenAiMessage> messages, OpenAiMessageVo item,
                                          OpenAiOperateDto req, AtomicBoolean hasAttachFiles) throws Exception {
        if (messages == null || item == null || item.getType() == null || item.getType().isEmpty()) {
            return false;
        }
        if (!isInterjectSafeBoundary(messages)) {
            log.warn("interject skip, last message is assistant with tool_calls, wait for tool response.");
            return false;
        }

        OpenAiMessage appendMsg;
        if (OpenAiConsts.USER.equals(item.getType())) {
            appendMsg = convertOpenAiUserMessage(item, req, hasAttachFiles);
        } else if (OpenAiConsts.SYSTEM.equals(item.getType())) {
            appendMsg = item.getSystem();
        } else if (OpenAiConsts.ASSISTANT.equals(item.getType())) {
            appendMsg = item.getAssistant();
        } else if (OpenAiConsts.TOOL.equals(item.getType())) {
            appendMsg = item.getTool();
        } else {
            return false;
        }
        if (appendMsg == null) {
            return false;
        }
        messages.add(appendMsg);
        return true;
    }

    @FunctionalInterface
    public interface ExFunction<R, T> {
        R apply(T t) throws Exception;
    }

    public static String TOOL_RETURNS_FILE_CONTENT = "here is tool returns files";

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody OpsSecureDto reqDto,
                             HttpServletRequest request) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        AtomicReference<OpenAiOperateDto> reqRef = new AtomicReference<>();
        AtomicBoolean hasAttachFiles = new AtomicBoolean(false);
        CopyOnWriteArrayList<TmpFileTools.FileAttachMessage> toolFileMessages = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<AsyncTaskMessage> asyncTaskMessages = new CopyOnWriteArrayList<>();
        ConcurrentHashMap<String, String> recycleMap = new ConcurrentHashMap<>();
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            reqRef.set(req);
            HttpServletToolContextUtil.fillToolContext(request);
            ToolCallContextHolder.put("req", req);
            if (req.getRecycleMap() != null) {
                for (Map.Entry<String, String> entry : req.getRecycleMap().entrySet()) {
                    String key = entry.getKey();
                    if (key == null) {
                        continue;
                    }
                    String value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    recycleMap.put(key, value);
                }
            }
            req.setRecycleMap(recycleMap);

            ExFunction<Object, String> echoProgress = (content) -> {
                String emitContent = objectMapper.writeValueAsString(content);
                OpsSecureReturn<?> resp = null;
                if (req.isEncryptOutput()) {
                    resp = transfer.success(emitContent);
                } else {
                    resp = OpsSecureReturn.success(emitContent);
                }
                resp.withAttr("type", OpsOpenAiConsts.ECHO_PROGRESS);
                String respJson = objectMapper.writeValueAsString(resp);
                emitter.send(respJson);
                return null;
            };

            echoProgress.apply("请求处理中...");

            Map<String, String> sessionRecordsMap = SessionRecordTools.replaceAllInContextHolder(req.getSessionRecordsMap());
            req.setSessionRecordsMap(sessionRecordsMap);

            ToolCallContextHolder.put(SessionRecordTools.TOOL_CONTEXT_KEY, sessionRecordsMap);

            Map<String, CopyOnWriteArrayList<ToolDefinition>> allToolsMap = new ConcurrentHashMap<>();

            CompletableFuture.runAsync(() -> {
                HttpServletToolContextUtil.fillToolContext(request);
                ToolCallContextHolder.put("req", req);
                ToolCallContextHolder.put(SessionRecordTools.TOOL_CONTEXT_KEY, sessionRecordsMap);

                try {
                    echoProgress.apply("转换消息中...");

                    OpenAiCompletionVo vo = req.getCompletion();
                    OpenAiCompletionDto completion = new OpenAiCompletionDto();
                    List<OpenAiToolApprovalDto> toolApprovalList = req.getToolApprovalList();

                    completion.setModel(vo.getModel());
                    completion.setStream(vo.isStream());
                    completion.setStream_options(vo.getStream_options());
                    completion.setMessages(new ArrayList<>());

                    OpenAiMessageVo injectMsg = null;
                    List<OpenAiMessageVo> voMsgList = vo.getMessages();
                    if (voMsgList != null) {
                        for (OpenAiMessageVo item : voMsgList) {
                            if (OpenAiConsts.USER.equals(item.getType())) {
                                injectMsg = item;
                                OpenAiMessage user = item.getUser();

                                user = convertOpenAiUserMessage(item, req, hasAttachFiles);
                                completion.getMessages().add(user);
                            } else if (OpenAiConsts.SYSTEM.equals(item.getType())) {
                                injectMsg = item;
                                completion.getMessages().add(item.getSystem());
                            } else if (OpenAiConsts.ASSISTANT.equals(item.getType())) {
                                injectMsg = item;
                                completion.getMessages().add(item.getAssistant());
                            } else if (OpenAiConsts.TOOL.equals(item.getType())) {
                                injectMsg = item;
                                completion.getMessages().add(item.getTool());
                            }

                        }
                    }
                    completion.setTools(vo.getTools());

                    echoProgress.apply("系统提示词注入中...");

                    // 最后一条是 user/system 消息的时候，允许注入提示词，否则就是 assistant/tool 的时候往往是中间过程，不用注入提示词
                    boolean needInjectSystemPrompt = (injectMsg == null || Arrays.asList(OpenAiConsts.USER, OpenAiConsts.SYSTEM).contains(injectMsg.getType()));
                    if (!needInjectSystemPrompt) {
                        // 保留低概率注入提示词，保证长周期时，也能有提示词引导
                        if (random.nextDouble() < 0.3) {
                            needInjectSystemPrompt = true;
                        }
                    }

                    // Note: 系统消息动态注入，因为使用的是add(0,)方式，形成了先插入的排在后面
                    // 因此，就要利用好KvCache,保证相对稳定的内容使用排在前面
                    // 最大化的利用好LLM的KvCache能力

                    // 通过系统注入提示词，强化回话记忆
                    if(req.isEnableTools()
                            && req.getSessionRecordsMap()!=null
                            && !req.getSessionRecordsMap().isEmpty()){
                        String content = SessionRecordTools.getSystemPrompt(req.getSessionRecordsMap());
                        if(content!=null && !content.isEmpty()) {
                            OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                            completion.getMessages().add(0, system);

                            OpenAiMessageVo dto = new OpenAiMessageVo();
                            dto.setType(OpsOpenAiConsts.ECHO_SYSTEM_INJECT);
                            dto.setEcho_system_inject(system);

                            String defTruthMsg = objectMapper.writeValueAsString(dto);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defTruthMsg);
                            } else {
                                resp = OpsSecureReturn.success(defTruthMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_SYSTEM_INJECT);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }
                    }

                    // 注入到提示词，事实
                    if (req.isEnableTruth()) {
                        // 事实内容注入
                        String truthContent = req.getTruthContent();
                        if (truthContent != null && !truthContent.isEmpty()) {
                            if (!truthContent.startsWith("# 关键事实\n\n")) {
                                truthContent = "# 关键事实\n\n" + truthContent;
                            }
                            OpenAiSystemMessage system = new OpenAiSystemMessage(truthContent);
                            completion.getMessages().add(0, system);


                            OpenAiMessageVo dto = new OpenAiMessageVo();
                            dto.setType(OpsOpenAiConsts.ECHO_TRUTH_CONTENT);
                            dto.setEcho_truth_content(system);

                            String defTruthMsg = objectMapper.writeValueAsString(dto);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defTruthMsg);
                            } else {
                                resp = OpsSecureReturn.success(defTruthMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_TRUTH_CONTENT);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }
                    }

                    if(req.isEnableAgentsMarkdown() && needInjectSystemPrompt){
                        String content = AgentsPrompt.getAgentsMarkdown();
                        if(content!=null){
                            content=content.trim();
                        }
                        if(content!=null && !content.isEmpty()) {
                            OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                            completion.getMessages().add(0, system);

                            OpenAiMessageVo dto = new OpenAiMessageVo();
                            dto.setType(OpsOpenAiConsts.ECHO_SYSTEM_INJECT);
                            dto.setEcho_system_inject(system);

                            String defSkillMsg = objectMapper.writeValueAsString(dto);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defSkillMsg);
                            } else {
                                resp = OpsSecureReturn.success(defSkillMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_SYSTEM_INJECT);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }
                    }


                    if (req.isEnableLoopEngineering() && needInjectSystemPrompt) {
                        String content = LoopEngineeringTools.convertSystemPrompt();
                        OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                        completion.getMessages().add(0, system);

                        OpenAiMessageVo dto = new OpenAiMessageVo();
                        dto.setType(OpsOpenAiConsts.ECHO_LOOP_ENGINEERING);
                        dto.setEcho_loop_engineering(system);

                        String defSkillMsg = objectMapper.writeValueAsString(dto);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(defSkillMsg);
                        } else {
                            resp = OpsSecureReturn.success(defSkillMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_LOOP_ENGINEERING);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (req.isEnableTruth() && needInjectSystemPrompt) {
                        // 事实系统使用方式
                        String content = TruthStoreTools.convertSystemPrompt();
                        if (content != null && !content.isEmpty()) {
                            OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                            completion.getMessages().add(0, system);

                            OpenAiMessageVo dto = new OpenAiMessageVo();
                            dto.setType(OpsOpenAiConsts.ECHO_TRUTH_PROMPT);
                            dto.setEcho_truth_prompt(system);

                            String defSkillMsg = objectMapper.writeValueAsString(dto);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defSkillMsg);
                            } else {
                                resp = OpsSecureReturn.success(defSkillMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_TRUTH_PROMPT);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }

                    }

                    if (req.isEnableSkills() && needInjectSystemPrompt) {
                        String content = SkillsHelper.convertSkillDefinitionsAsSystemPrompt(skillsTools.getSkillDefinitions());
                        if (content != null && !content.isEmpty()) {
                            OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                            completion.getMessages().add(0, system);

                            OpenAiMessageVo dto = new OpenAiMessageVo();
                            dto.setType(OpsOpenAiConsts.ECHO_SKILL);
                            dto.setEcho_skill(system);

                            String defSkillMsg = objectMapper.writeValueAsString(dto);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defSkillMsg);
                            } else {
                                resp = OpsSecureReturn.success(defSkillMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_SKILL);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }
                    }

                    if (req.isEnableTools()
                            && req.isEnableLruTools()
                            && mcpProviderTools != null
                            && needInjectSystemPrompt) {
                        String content = McpProviderTools.SYSTEM_PROMPT;
                        OpenAiSystemMessage system = new OpenAiSystemMessage(content);
                        completion.getMessages().add(0, system);

                        OpenAiMessageVo dto = new OpenAiMessageVo();
                        dto.setType(OpsOpenAiConsts.ECHO_DYNAMIC_TOOL);
                        dto.setEcho_dynamic_tool(system);

                        String defSkillMsg = objectMapper.writeValueAsString(dto);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(defSkillMsg);
                        } else {
                            resp = OpsSecureReturn.success(defSkillMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_DYNAMIC_TOOL);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (req.isEnableTools()) {
                        echoProgress.apply("工具注入中...");

                        if (toolManager != null) {
                            List<ToolDefinition> tools = toolManager.listTools();
                            if (tools != null) {
                                tools = filterRequestTools(req, tools);
                                if (completion.getTools() == null) {
                                    completion.setTools(new ArrayList<>());
                                }
                                for (ToolDefinition tool : tools) {
                                    allToolsMap.computeIfAbsent(tool.getName(), k -> new CopyOnWriteArrayList<>())
                                            .add(tool);

                                    OpenAiToolDefinitionDto dto = new OpenAiToolDefinitionDto();
                                    dto.setName(tool.getName());
                                    dto.setDescription(tool.getDescription());
                                    ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(tool);
                                    if (rawTool != null) {

                                        dto.setParameterNames(rawTool.getParameterNames());
                                        dto.setTags(rawTool.getTags());
                                        if (rawTool.getBindClass() != null) {
                                            dto.setBindClass(rawTool.getBindClass().getSimpleName());
                                        }
                                        if (rawTool.getBindMethod() != null) {
                                            dto.setBindMethod(rawTool.getBindMethod().getName());
                                        }
                                    }
                                    String defToolMsg = objectMapper.writeValueAsString(dto);
                                    OpsSecureReturn<?> resp = null;
                                    if (req.isEncryptOutput()) {
                                        resp = transfer.success(defToolMsg);
                                    } else {
                                        resp = OpsSecureReturn.success(defToolMsg);
                                    }
                                    resp.withAttr("type", OpsOpenAiConsts.DEFINITION_TOOL);
                                    String respJson = objectMapper.writeValueAsString(resp);
                                    emitter.send(respJson);

                                    completion.getTools().add(new OpenAiToolsDefinition(tool.getJsonSchema()));
                                }
                            }
                        }

                        List<OpenAiMessage> messages = completion.getMessages();
                        if (messages != null && !messages.isEmpty()) {
                            OpenAiMessage lastMsg = messages.get(messages.size() - 1);
                            // 处理 tools-call-request, 最后一条是工具调用，需要判断授权
                            if (lastMsg instanceof OpenAiAssistantMessage) {
                                OpenAiAssistantMessage assistantMessage = (OpenAiAssistantMessage) lastMsg;
                                List<OpenAiToolCall> calls = assistantMessage.getTool_calls();

                                if (calls != null && !calls.isEmpty()) {
                                    echoProgress.apply("工具调用中...");

                                    Map<String, ToolDefinition> definitionMap = new HashMap<>();
                                    if (toolManager != null) {
                                        List<ToolDefinition> tools = toolManager.listTools();
                                        tools = filterRequestTools(req, tools);
                                        for (ToolDefinition tool : tools) {
                                            definitionMap.put(tool.getName(), tool);
                                        }
                                    }
                                    Map<String, OpenAiToolApprovalDto> approvalMap = new HashMap<>();
                                    if (toolApprovalList != null) {
                                        for (OpenAiToolApprovalDto item : toolApprovalList) {
                                            approvalMap.put(item.getTool_call_id(), item);
                                        }
                                    }
                                    CountDownLatch latch = new CountDownLatch(calls.size());
                                    for (OpenAiToolCall call : calls) {
                                        if (req.isEnableLruTools() && mcpProviderTools != null) {
                                            if (req.getLruToolNames() == null) {
                                                req.setLruToolNames(new ArrayList<>());
                                            }
                                            List<String> lruToolNames = req.getLruToolNames();

                                            OpenAiToolCallFunction f = call.getFunction();
                                            String name = f.getName();
                                            lruToolNames.add(0, name);
                                        }
                                        Runnable toolTask = () -> {
                                            HttpServletToolContextUtil.fillToolContext(request);
                                            ToolCallContextHolder.put("req", req);
                                            ToolCallContextHolder.put(SessionRecordTools.TOOL_CONTEXT_KEY, sessionRecordsMap);
                                            try {
                                                String id = call.getId();
                                                OpenAiToolCallFunction function = call.getFunction();

                                                echoProgress.apply(function.getName() + " 工具调用中...");

                                                ToolCallRequest toolCallRequest = new ToolCallRequest().toMutator()
                                                        .cast(ToolCallRequest.class)
                                                        .set(u -> u::setId, id)
                                                        .set(u -> u::setName, function.getName())
                                                        .set(u -> u::setArguments, function.getArguments())
                                                        .set(u -> u::setRawRequest, call)
                                                        .done();

                                                Object callRet = null;
                                                try {
                                                    OpenAiToolApprovalDto approvalDto = approvalMap.get(id);
                                                    if (approvalDto != null) {
                                                        if (approvalDto.isReject()) {
                                                            String rejectReason = approvalDto.getRejectReason();
                                                            if (rejectReason == null || rejectReason.isEmpty()) {
                                                                rejectReason = "";
                                                            } else {
                                                                rejectReason = ", reason is : " + rejectReason;
                                                            }
                                                            throw new IllegalStateException("user reject tool execute" + rejectReason);
                                                        }
                                                    }

                                                    boolean isWebjsResolved = false;
                                                    List<OpenAiWebjsToolResult> webjsToolResults = req.getWebjsToolResults();
                                                    if (webjsToolResults != null && !webjsToolResults.isEmpty()) {
                                                        for (OpenAiWebjsToolResult result : webjsToolResults) {
                                                            if (Objects.equals(id, result.getTool_call_id())) {
                                                                callRet = result.getContent();
                                                                isWebjsResolved = true;
                                                            }
                                                        }
                                                    }

                                                    if (!isWebjsResolved) {
                                                        if (!toolManager.support(toolCallRequest)) {
                                                            throw new IllegalArgumentException("cannot found tool definition: " + toolCallRequest.getName());
                                                        }
                                                        callRet = toolManager.callTool(toolCallRequest);
                                                    }
                                                } catch (Throwable e) {
                                                    callRet = "call tool error! " + e.getClass() + ": " + e.getMessage();
                                                    log.warn(e.getMessage(), e);
                                                }
                                                if (callRet instanceof TmpFileTools.FileAttachMessage) {
                                                    TmpFileTools.FileAttachMessage fileMsg = (TmpFileTools.FileAttachMessage) callRet;
                                                    toolFileMessages.add(fileMsg);
                                                    callRet = fileMsg.getContent();
                                                }
                                                if (callRet instanceof AsyncTaskMessage) {
                                                    AsyncTaskMessage taskMsg = (AsyncTaskMessage) callRet;
                                                    asyncTaskMessages.add(taskMsg);
                                                    callRet = taskMsg.getContent();
                                                }
                                                if (callRet instanceof CharSequence) {
                                                    callRet = String.valueOf(callRet);
                                                } else {
                                                    callRet = objectMapper.writeValueAsString(callRet);
                                                }
                                                OpenAiToolMessage toolMsg = new OpenAiToolMessage().toMutator()
                                                        .set(u -> u::setTool_call_id, id)
                                                        .set(u -> u::setContent, String.valueOf(callRet))
                                                        .done();

                                                if (toolMsg != null) {
                                                    EchoOpenAiToolMessage toolEchoMsg = new EchoOpenAiToolMessage().toMutator()
                                                            .set(u -> u::setMessage, toolMsg)
                                                            .set(u -> u::setFunction, function)
                                                            .done();
                                                    toolEchoMsg.createContent();
                                                    OpenAiMessageVo toolEchoVo = new OpenAiMessageVo().toMutator()
                                                            .set(u -> u::setType, OpsOpenAiConsts.ECHO_TOOL)
                                                            .set(u -> u::setEcho_tool, toolEchoMsg)
                                                            .done();
                                                    String emitToolMsg = objectMapper.writeValueAsString(toolEchoVo);
                                                    OpsSecureReturn<?> resp = null;
                                                    if (req.isEncryptOutput()) {
                                                        resp = transfer.success(emitToolMsg);
                                                    } else {
                                                        resp = OpsSecureReturn.success(emitToolMsg);
                                                    }
                                                    resp.withAttr("type", OpsOpenAiConsts.ECHO_TOOL);
                                                    String respJson = objectMapper.writeValueAsString(resp);
                                                    emitter.send(respJson);
                                                }
                                                if (toolMsg != null) {
                                                    OpenAiMessageVo toolEchoVo = new OpenAiMessageVo().toMutator()
                                                            .set(u -> u::setType, OpenAiConsts.TOOL)
                                                            .set(u -> u::setTool, toolMsg)
                                                            .done();
                                                    String emitToolMsg = objectMapper.writeValueAsString(toolEchoVo);
                                                    OpsSecureReturn<?> resp = null;
                                                    if (req.isEncryptOutput()) {
                                                        resp = transfer.success(emitToolMsg);
                                                    } else {
                                                        resp = OpsSecureReturn.success(emitToolMsg);
                                                    }
                                                    resp.withAttr("type", OpenAiConsts.TOOL);
                                                    String respJson = objectMapper.writeValueAsString(resp);
                                                    emitter.send(respJson);
                                                }

                                                messages.add(toolMsg);
                                            } catch (Exception e) {
                                                log.warn(e.getMessage(), e);
                                            } finally {
                                                ToolCallContextHolder.clear();
                                                latch.countDown();
                                            }
                                        };
                                        toolPool.submit(toolTask);
                                    }

                                    latch.await();
                                    HttpServletToolContextUtil.fillToolContext(request);
                                    ToolCallContextHolder.put("req", req);
                                    ToolCallContextHolder.put(SessionRecordTools.TOOL_CONTEXT_KEY, sessionRecordsMap);

                                    echoProgress.apply("工具调用完成...");
                                }
                            }
                        }
                    }

                    // 插话不在这一步插入，必须等到本轮所有工具调用以及工具产生的附加消息都插入完成之后
                    // 也就是下面工具回传文件、异步任务等消息的处理之后的安全临界点，见 appendInterjectMessage

                    // 回显给前端，工具调用可能变更了内容，事实
                    if (req.isEnableTruth()) {
                        // 事实内容注入
                        String truthContent = req.getTruthContent();
                        OpenAiSystemMessage system = new OpenAiSystemMessage(truthContent);

                        OpenAiMessageVo dto = new OpenAiMessageVo();
                        dto.setType(OpsOpenAiConsts.ECHO_TRUTH_SYNC);
                        dto.setEcho_truth_sync(system);

                        String defTruthMsg = objectMapper.writeValueAsString(dto);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(defTruthMsg);
                        } else {
                            resp = OpsSecureReturn.success(defTruthMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_TRUTH_SYNC);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (!toolFileMessages.isEmpty()) {
                        List<TmpFileTools.UploadTmpFileMetadata> sendToUserAttachFiles = new ArrayList<>();
                        List<TmpFileTools.UploadTmpFileMetadata> sendToLLmAttachFiles = new ArrayList<>();
                        for (TmpFileTools.FileAttachMessage item : toolFileMessages) {
                            List<TmpFileTools.UploadTmpFileMetadata> files = item.getFiles();
                            if (files != null) {
                                if (item.isSendToLlm()) {
                                    sendToLLmAttachFiles.addAll(files);
                                } else {
                                    sendToUserAttachFiles.addAll(files);
                                }
                            }
                        }

                        if (!sendToUserAttachFiles.isEmpty()) {
                            OpenAiMessageVo toolUserMsg = new OpenAiMessageVo().toMutator()
                                    .set(u -> u::setType, OpsOpenAiConsts.ECHO_ATTACH_FILES)
                                    .set(u -> u::setEcho_attach_files, new OpenAiSystemMessage("tool response files"))
                                    .set(u -> u::setAttachFiles, sendToUserAttachFiles)
                                    .done();

                            // 这里echo回前端
                            String defSkillMsg = objectMapper.writeValueAsString(toolUserMsg);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defSkillMsg);
                            } else {
                                resp = OpsSecureReturn.success(defSkillMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.ECHO_ATTACH_FILES);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);
                        }

                        if (!sendToLLmAttachFiles.isEmpty()) {
                            OpenAiMessageVo toolUserMsg = new OpenAiMessageVo().toMutator()
                                    .set(u -> u::setType, OpenAiConsts.USER)
                                    .set(u -> u::setUser, new OpenAiUserMessage(TOOL_RETURNS_FILE_CONTENT))
                                    .set(u -> u::setAttachFiles, sendToLLmAttachFiles)
                                    .done();

                            // 这里先echo回前端，再添加，因为下面convert会重写原始的user.content,为了保持前端显示清洁，这里就要提前echo
                            String defSkillMsg = objectMapper.writeValueAsString(toolUserMsg);
                            OpsSecureReturn<?> resp = null;
                            if (req.isEncryptOutput()) {
                                resp = transfer.success(defSkillMsg);
                            } else {
                                resp = OpsSecureReturn.success(defSkillMsg);
                            }
                            resp.withAttr("type", OpsOpenAiConsts.USER);
                            String respJson = objectMapper.writeValueAsString(resp);
                            emitter.send(respJson);

                            OpenAiMessage user = convertOpenAiUserMessage(toolUserMsg, req, hasAttachFiles);
                            completion.getMessages().add(user);
                        }

                    }

                    // 插话：安全临界点插入
                    // 此时 tool_calls 与 tool 消息已经成对完整，工具回传的附加消息也已经插入
                    // 插话作为本轮最后一条消息追加，用于引导模型接下来的走向
                    OpenAiMessageVo interject = req.getInterject();
                    if (OpsOpenAiConsts.ACTION_SUMMARY_HISTORY.equals(req.getAction())) {
                        // 会话总结轮次独占：插话等其他消息搁置到下一轮，避免总结内容被干扰
                        if (interject != null) {
                            log.info("interject postpone, current round is summary-history, wait for next round.");
                        }
                    } else {
                        if (canAppendInterject(completion.getMessages(), interject)) {
                            // 先回显再插入：convert 会重写 user.content（追加附件清单），
                            // 先回显原始内容才能保证前端显示的插话清洁
                            echoInterjectMessage(emitter, req, interject);
                        }
                        appendInterjectMessage(completion.getMessages(), interject, req, hasAttachFiles);
                    }

                    if (!asyncTaskMessages.isEmpty()) {
                        List<AsyncTaskItem> asyncTasks = new ArrayList<>();
                        for (AsyncTaskMessage msg : asyncTaskMessages) {
                            List<AsyncTaskItem> list = msg.getList();
                            if (list != null) {
                                asyncTasks.addAll(list);
                            }
                        }
                        OpenAiMessageVo toolUserMsg = new OpenAiMessageVo().toMutator()
                                .set(u -> u::setType, OpsOpenAiConsts.ECHO_ASYNC_TASKS)
                                .set(u -> u::setEcho_async_tasks, new OpenAiSystemMessage("tool response async tasks"))
                                .set(u -> u::setAsyncTasks, asyncTasks)
                                .done();

                        // 这里echo回前端
                        String defSkillMsg = objectMapper.writeValueAsString(toolUserMsg);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(defSkillMsg);
                        } else {
                            resp = OpsSecureReturn.success(defSkillMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_ASYNC_TASKS);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (req.isEnableTools()
                            && req.isEnableLruTools()
                            && mcpProviderTools != null) {
                        Integer keepSize = req.getLruToolMaxSize();
                        if (keepSize == null || keepSize < 1) {
                            keepSize = 20;
                        }

                        LinkedList<String> unqToolNames = new LinkedList<>();

                        List<String> lruToolNames = req.getLruToolNames();
                        if (lruToolNames != null) {
                            for (String name : lruToolNames) {
                                if (!unqToolNames.contains(name)) {
                                    unqToolNames.add(name);
                                }
                            }
                        }
                        List<ToolDefinition> loadedTools = req.getLoadedTools();
                        if (loadedTools != null) {
                            for (ToolDefinition item : loadedTools) {
                                if (unqToolNames.contains(item.getName())) {
                                    unqToolNames.remove(item.getName());
                                }
                                unqToolNames.add(0, item.getName());
                            }
                        }

                        List<OpenAiToolsDefinition> tools = completion.getTools();

                        if (req.isEnableToolRecommendByIntentRecognize()) {
                            // 这里可以通过意图识别，进行变更 unqToolNames 列表，实现工具只能推荐
                            String userMsgContent = null;
                            if (completion.getMessages() != null && !completion.getMessages().isEmpty()) {
                                OpenAiMessage msg = completion.getMessages().get(completion.getMessages().size() - 1);
                                if (msg instanceof OpenAiUserMessage
                                        || msg instanceof OpenAiRichUserMessage) {
                                    userMsgContent = msg.content();
                                }
                            }
                            String cmpUserMsgContent = userMsgContent;
                            if (cmpUserMsgContent != null) {
                                cmpUserMsgContent = cmpUserMsgContent.trim();
                            }
                            boolean hasAssitantMsg = false;
                            for (OpenAiMessage msg : completion.getMessages()) {
                                if (msg instanceof OpenAiAssistantMessage) {
                                    hasAssitantMsg = true;
                                }
                            }
                            boolean needIntentRecognize = hasAssitantMsg ? (random.nextDouble() < 0.3) : true;

                            if (cmpUserMsgContent != null && !cmpUserMsgContent.isEmpty()
                                    && !TOOL_RETURNS_FILE_CONTENT.equals(cmpUserMsgContent)
                                    && needIntentRecognize) {
                                echoProgress.apply("工具意图识别推荐中...");

                                Map<String, Set<String>> labelToolNameMap = new LinkedHashMap<>();

                                List<AgentTools.IntentItem> intentItems = new ArrayList<>();
                                for (OpenAiToolsDefinition e : tools) {
                                    boolean resolved = false;
                                    CopyOnWriteArrayList<ToolDefinition> definitions = allToolsMap.get(e.getFunction().getName());
                                    if (definitions != null && !definitions.isEmpty()) {
                                        for (ToolDefinition definition : definitions) {
                                            ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(definition);
                                            if (rawTool == null) {
                                                continue;
                                            }
                                            Method bindMethod = rawTool.getBindMethod();
                                            if (bindMethod == null) {
                                                continue;
                                            }
                                            Map<String, IToolIntent> parse = ToolIntentHelper.parse(bindMethod);
                                            for (Map.Entry<String, IToolIntent> entry : parse.entrySet()) {
                                                AgentTools.IntentItem item = new AgentTools.IntentItem();

                                                item.setLabel(entry.getKey());
                                                item.setDescription(entry.getValue().description());
                                                intentItems.add(item);
                                                labelToolNameMap.computeIfAbsent(item.getLabel(), k -> new LinkedHashSet<>())
                                                        .add(e.getFunction().getName());
                                                resolved = true;
                                            }
                                        }
                                    }
                                    if (!resolved) {
                                        AgentTools.IntentItem item = new AgentTools.IntentItem();

                                        item.setLabel(e.getFunction().getName());
                                        item.setDescription(e.getFunction().getName());
                                        intentItems.add(item);
                                        labelToolNameMap.computeIfAbsent(item.getLabel(), k -> new LinkedHashSet<>())
                                                .add(e.getFunction().getName());
                                        resolved = true;
                                    }
                                }

                                AgentTools agentTools = this.agentTools;
                                if (agentTools == null) {
                                    agentTools = new AgentTools();
                                }
                                AgentTools.IntentResult intentResult = agentTools.intent_recognize(userMsgContent, intentItems);

                                if (intentResult != null) {
                                    // 工具意图推荐
                                    String content = "" + intentResult.getPrompt() + "\n\n"
                                            + "# raw result \n\n"
                                            + intentResult.getRawResult() + "\n\n"
                                            + "# final result \n\n"
                                            + intentResult.getResult();
                                    OpenAiSystemMessage system = new OpenAiSystemMessage(content);

                                    OpenAiMessageVo dto = new OpenAiMessageVo();
                                    dto.setType(OpsOpenAiConsts.ECHO_TOOL_INTENT_RECOMMEND);
                                    dto.setEcho_tool_intent_recommend(system);

                                    String defTruthMsg = objectMapper.writeValueAsString(dto);
                                    OpsSecureReturn<?> resp = null;
                                    if (req.isEncryptOutput()) {
                                        resp = transfer.success(defTruthMsg);
                                    } else {
                                        resp = OpsSecureReturn.success(defTruthMsg);
                                    }
                                    resp.withAttr("type", OpsOpenAiConsts.ECHO_TOOL_INTENT_RECOMMEND);
                                    String respJson = objectMapper.writeValueAsString(resp);
                                    emitter.send(respJson);
                                }

                                Set<String> labels = intentResult.getResult();

                                if (!labels.isEmpty()) {
                                    // 保留个数
                                    int headSize = (int) (keepSize * 0.25);
                                    headSize = Math.max(headSize, 3);

                                    List<String> removeList = new ArrayList<>();
                                    while (unqToolNames.size() > headSize) {
                                        removeList.add(unqToolNames.removeLast());
                                    }

                                    // 先添加推荐的
                                    List<String> labelToolList = new ArrayList<>();
                                    for (String label : labels) {
                                        Set<String> names = labelToolNameMap.get(label);
                                        if (names != null) {
                                            labelToolList.addAll(names);
                                        }
                                    }
                                    for (String name : labelToolList) {
                                        if (!unqToolNames.contains(name)) {
                                            unqToolNames.add(name);
                                        }
                                    }

                                    // 在添加原来被移除的
                                    for (String removeName : removeList) {
                                        if (!unqToolNames.contains(removeName)) {
                                            unqToolNames.add(removeName);
                                        }
                                    }

                                }
                            }
                        }

                        // 根据LRU最大窗口大小，移除最旧的
                        while (unqToolNames.size() > keepSize) {
                            unqToolNames.removeLast();
                        }

                        String emitToolMsg = objectMapper.writeValueAsString(unqToolNames);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(emitToolMsg);
                        } else {
                            resp = OpsSecureReturn.success(emitToolMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_LRU_TOOLS);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);

                        // 固定加载的工具
                        tools.stream().filter(e -> {
                            FunctionJsonSchema function = e.getFunction();
                            String name = function.getName();
                            if (true) {
                                Set<String> checkNames = McpProviderTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (true) {
                                Set<String> checkNames = SessionRecordTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (hasAttachFiles.get()) {
                                Set<String> checkNames = TmpFileTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (req.isEnableTruth()) {
                                Set<String> checkNames = TruthStoreTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (req.isEnableMemories()) {
                                Set<String> checkNames = MemoryTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (req.isEnableRecycleToolMessage() || req.isEnableRecycleToolArgument()) {
                                Set<String> checkNames = RecycleTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            if (req.isEnableLoopEngineering()) {
                                Set<String> checkNames = LoopEngineeringTools.toolNames();
                                for (String checkName : checkNames) {
                                    if (name.contains(checkName)) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }).forEach(e -> {
                            if (!unqToolNames.contains(e.getFunction().getName())) {
                                unqToolNames.add(e.getFunction().getName());
                            }
                        });

                        // 真正的工具
                        List<OpenAiToolsDefinition> filteredTools = tools.stream().filter(e -> {
                            return unqToolNames.contains(e.getFunction().getName());
                        }).collect(Collectors.toList());

                        completion.setTools(filteredTools);
                    }


                    String url = req.getMeta().getBaseUrl();
                    url = extraStandardBaseUrl(url);
                    url = url + "/chat/completions";

                    // 合并重排system消息，用于在一些严格的模型要求下，要求system消息只有一条，且只能在第一条的情况
                    if (req.isEnableMergedSystemMsg()) {
                        echoProgress.apply("重排系统消息中...");

                        List<OpenAiMessage> messages = completion.getMessages();
                        completion.setMessages(new ArrayList<>());

                        List<OpenAiSystemMessage> systemMessages = new ArrayList<>();
                        for (OpenAiMessage item : messages) {
                            if (item instanceof OpenAiSystemMessage) {
                                OpenAiSystemMessage sys = (OpenAiSystemMessage) item;
                                systemMessages.add(sys);
                            } else {
                                completion.getMessages().add(item);
                            }
                        }

                        if (!systemMessages.isEmpty()) {
                            String mergedSystem = systemMessages.stream()
                                    .map(OpenAiSystemMessage::getContent)
                                    .map(e -> "<system_scope>\n" + e + "</system_scope>")
                                    .collect(Collectors.joining("\n\n"));
                            OpenAiSystemMessage sys = new OpenAiSystemMessage(mergedSystem);
                            completion.getMessages().add(0, sys);
                        }
                    }

                    // 回收较久之前的Tool消息内容，减少输入Token量，因为早起的工具调用结果，对接下来的推理影响已经不大了
                    // 仅保留已被回收，提示如有需要，重新执行工具即可
                    if (req.isEnableRecycleToolMessage() || req.isEnableRecycleToolArgument()) {
                        int toolCount=0;

                        int recycleToolMessageLastKeepCount = req.getRecycleToolMessageLastKeepCount() == null ? 10 : req.getRecycleToolMessageLastKeepCount();
                        if (recycleToolMessageLastKeepCount <= 0) {
                            recycleToolMessageLastKeepCount = 10;
                        }
                        int recycleToolMessageMinLength=req.getRecycleToolMessageMinLength()==null?512:req.getRecycleToolMessageMinLength();
                        if(recycleToolMessageMinLength<=0){
                            recycleToolMessageMinLength=512;
                        }

                        int recycleToolArgumentLastKeepCount = req.getRecycleToolArgumentLastKeepCount() == null ? 10 : req.getRecycleToolArgumentLastKeepCount();
                        if (recycleToolArgumentLastKeepCount <= 0) {
                            recycleToolArgumentLastKeepCount = 10;
                        }
                        int recycleToolArgumentMinLength = req.getRecycleToolArgumentMinLength() == null ? 512 : req.getRecycleToolArgumentMinLength();
                        if (recycleToolArgumentMinLength <= 0) {
                            recycleToolArgumentMinLength = 512;
                        }

                        int size = completion.getMessages().size();

                        for (int i = size-1; i >=0; i--) {
                            OpenAiMessage msg = completion.getMessages().get(i);
                            if(msg instanceof OpenAiToolMessage){
                                toolCount++;
                            }

                            if (toolCount > recycleToolMessageLastKeepCount) {
                                if(msg instanceof OpenAiToolMessage){
                                    OpenAiToolMessage toolMsg = (OpenAiToolMessage) msg;
                                    String content = toolMsg.getContent();
                                    if (content.length() > recycleToolMessageMinLength) {
                                        String rep = (String) RecycleTools.recycleArguments(content, recycleToolMessageMinLength, recycleMap);
                                        toolMsg.setContent(rep);
                                        //String rep = "[recycled] tool result recycled; re-call if needed.";
                                        //if(rep.length()<content.length()) {
                                        //
                                        //    toolMsg.setContent(rep);
                                        //}
                                    }
                                }
                            }

                            if (toolCount > recycleToolArgumentLastKeepCount) {
                                if (msg instanceof OpenAiAssistantMessage) {
                                    OpenAiAssistantMessage assMsg = (OpenAiAssistantMessage) msg;
                                    List<OpenAiToolCall> calls = assMsg.getTool_calls();
                                    if (calls != null && !calls.isEmpty()) {
                                        for (OpenAiToolCall call : calls) {
                                            OpenAiToolCallFunction func = call.getFunction();
                                            if (func == null) {
                                                continue;
                                            }
                                            String arguments = func.getArguments();
                                            if (arguments != null && arguments.length() > recycleToolArgumentMinLength) {
                                                try {
                                                    Map<String, Object> argsMap = objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {
                                                    });
                                                    RecycleTools.recycleArguments(argsMap, recycleToolArgumentMinLength, recycleMap);
                                                    arguments = objectMapper.writeValueAsString(argsMap);
                                                    func.setArguments(arguments);
                                                } catch (Exception e) {
                                                    log.warn("recycle tool call arguments error: " + e.getMessage(), e);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (sessionRecordsMap != null) {
                        String emitSessionRecordsMsg = objectMapper.writeValueAsString(sessionRecordsMap);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(emitSessionRecordsMsg);
                        } else {
                            resp = OpsSecureReturn.success(emitSessionRecordsMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_SESSION_RECORDS_MAP);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (!recycleMap.isEmpty()) {
                        String emitRecycleMapMsg = objectMapper.writeValueAsString(recycleMap);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(emitRecycleMapMsg);
                        } else {
                            resp = OpsSecureReturn.success(emitRecycleMapMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_RECYCLE_MAP);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    if (req.isEnableEchoRequestPayload()) {
                        String emitPayloadMsg = objectMapper.writeValueAsString(completion);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(emitPayloadMsg);
                        } else {
                            resp = OpsSecureReturn.success(emitPayloadMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_REQUEST_PAYLOAD);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    echoProgress.apply("模型推理中...");

                    HttpRequest.doPost(url)
                            .set(u -> u::json)
                            .set2(u -> u::addHeader, HttpHeaderConstants.Authorization, HttpHeaderConstants.Bearer + " " + req.getMeta().getApiKey())
                            .set(u -> u::setData, completion)
                            .done()
                            .send(new SpringWebHttpProcessor(restTemplate),
                                    response -> {
                                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), StandardCharsets.UTF_8))) {
                                            String line = null;
                                            boolean hasDone = false;
                                            while ((line = reader.readLine()) != null) {
                                                if (line.startsWith("data:")) {
                                                    String data = line.substring(5).trim();
                                                    if ("[DONE]".equals(data)) {
                                                        hasDone = true;
                                                    }
                                                    OpsSecureReturn<?> resp = null;
                                                    if (req.isEncryptOutput()) {
                                                        resp = transfer.success(data);
                                                    } else {
                                                        resp = OpsSecureReturn.success(data);
                                                    }
                                                    resp.withAttr("type", OpenAiConsts.ASSISTANT);
                                                    String respJson = objectMapper.writeValueAsString(resp);
                                                    emitter.send(respJson);
                                                } else if ("[DONE]".equals(line.trim())) {
                                                    break;
                                                }
                                            }
                                            if (!hasDone) {
                                                String respJson = objectMapper.writeValueAsString(OpsSecureReturn.success("[DONE]"));
                                                emitter.send(respJson);
                                            }
                                            echoProgress.apply("模型推理完成...");
                                        } catch (Exception e) {
                                            try {
                                                OpsSecureReturn<?> resp = null;
                                                if (req.isEncryptOutput()) {
                                                    resp = transfer.error(e);
                                                } else {
                                                    resp = OpsSecureReturn.error(e);
                                                }
                                                String respJson = objectMapper.writeValueAsString(resp);
                                                emitter.send(respJson);
                                                emitter.complete();
                                            } catch (Exception ex) {
                                                emitter.completeWithError(ex);
                                            }
                                        }
                                        return null;
                                    });

                    echoProgress.apply("");
                    emitter.complete();
                } catch (Exception e) {
                    try {
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.error(e);
                        } else {
                            resp = OpsSecureReturn.error(e);
                        }
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                }
            }, pool);

            emitter.onCompletion(() -> {
                ToolCallContextHolder.clear();
                // System.out.println("前端 SSE 连接已正常关闭");
            });
            emitter.onTimeout(() -> {
                ToolCallContextHolder.clear();
                System.out.println("前端 SSE 连接超时");
            });
            emitter.onError((ex) -> {
                ToolCallContextHolder.clear();
                System.err.println("前端 SSE 发生错误: " + ex.getMessage());
            });

        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            OpsSecureReturn<?> resp = null;
            OpenAiOperateDto req = reqRef.get();
            if (req != null && req.isEncryptOutput()) {
                resp = transfer.error(e);
            } else {
                resp = OpsSecureReturn.error(e);
            }
            String respJson = objectMapper.writeValueAsString(resp);
            emitter.send(respJson);
            emitter.complete();
        }
        return emitter;
    }

    public static List<ToolDefinition> filterRequestTools(OpenAiOperateDto req, List<ToolDefinition> tools) {
        if (!req.isEnableMemories()) {
            tools = tools.stream()
                    .filter(e -> {
                        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(e);
                        if (rawTool != null) {
                            if (MemoryTools.class.isAssignableFrom(rawTool.getBindClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        if (!req.isEnableTruth()) {
            tools = tools.stream()
                    .filter(e -> {
                        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(e);
                        if (rawTool != null) {
                            if (TruthStoreTools.class.isAssignableFrom(rawTool.getBindClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        if (!req.isEnableSkills()) {
            tools = tools.stream()
                    .filter(e -> {
                        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(e);
                        if (rawTool != null) {
                            if (SkillsTools.class.isAssignableFrom(rawTool.getBindClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        if (!req.isEnableRags()) {
            tools = tools.stream()
                    .filter(e -> {
                        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(e);
                        if (rawTool != null) {
                            if (RagTools.class.isAssignableFrom(rawTool.getBindClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        if (!req.isEnableLruTools()) {
            tools = tools.stream()
                    .filter(e -> {
                        ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(e);
                        if (rawTool != null) {
                            if (McpProviderTools.class.isAssignableFrom(rawTool.getBindClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
        return tools;
    }
}
