package i2f.springboot.ops.openai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.openai.model.data.*;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.rag.RagTools;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.spring.web.rest.SpringWebHttpProcessor;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.openai.data.*;
import i2f.springboot.ops.openai.data.message.EchoOpenAiToolMessage;
import i2f.springboot.ops.openai.data.message.OpsOpenAiConsts;
import i2f.springboot.ops.openai.skill.SkillAutoConfiguration;
import i2f.springboot.ops.openai.tool.impl.McpProviderTools;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import i2f.springboot.ops.openai.tool.impl.TruthStoreTools;
import i2f.springboot.ops.openai.tool.impl.a2a.AgentTools;
import i2f.web.servlet.ServletFileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
@ConditionalOnClass(RestTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/open-ai")
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

    private ExecutorService pool = Executors.newWorkStealingPool(Math.min(Math.max(Runtime.getRuntime().availableProcessors() * 4 + 2, 16), 512));

    private ExecutorService toolPool = Executors.newWorkStealingPool(Math.min(Math.max(Runtime.getRuntime().availableProcessors() * 4 + 2, 16), 512));

    @Autowired(required = false)
    private ToolManager toolManager;

    @Autowired(required = false)
    private McpProviderTools mcpProviderTools;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    protected SecureRandom random = new SecureRandom();

    private RestTemplate createRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(5))
                .build();
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
                Map<String, Object> resp = tmpFileTools.saveFile(new FileInputStream(tmpFile), file.getOriginalFilename());
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

    @PostMapping("/tool/tags")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> toolTags(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            Set<String> tags = new TreeSet<>();
            if (toolManager != null) {
                List<ToolDefinition> tools = toolManager.getTools();
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

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody OpsSecureDto reqDto) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        AtomicReference<OpenAiOperateDto> reqRef = new AtomicReference<>();
        AtomicBoolean hasAttachFiles = new AtomicBoolean(false);
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            reqRef.set(req);
            AgentTools.REQUEST_HOLDER.set(req);

            CompletableFuture.runAsync(() -> {
                AgentTools.REQUEST_HOLDER.set(req);
                try {
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
                                OpenAiUserMessage user = item.getUser();
                                List<Map<String, Object>> attachFiles = item.getAttachFiles();
                                if (attachFiles != null && !attachFiles.isEmpty()) {
                                    // 如果有上传文件，则动态追加到用户消息中
                                    user.setContent(user.getContent()
                                            + "\n\n<upload_files>\n"
                                            + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(attachFiles)
                                            + "\n</upload_files>"
                                    );
                                    hasAttachFiles.set(true);
                                }
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

                    // 最后一条是 user/system 消息的时候，允许注入提示词，否则就是 assistant/tool 的时候往往是中间过程，不用注入提示词
                    boolean needInjectSystemPrompt = (injectMsg == null || Arrays.asList(OpenAiConsts.USER, OpenAiConsts.SYSTEM).contains(injectMsg.getType()));
                    if (!needInjectSystemPrompt) {
                        // 保留低概率注入提示词，保证长周期时，也能有提示词引导
                        if (random.nextDouble() < 0.3) {
                            needInjectSystemPrompt = true;
                        }
                    }

                    if (req.isEnableTruth()) {
                        String truthContent = req.getTruthContent();
                        OpenAiSystemMessage system = new OpenAiSystemMessage("# 关键事实\n\n" + truthContent);
                        completion.getMessages().add(0, system);
                    }

                    if (req.isEnableLruTools() && mcpProviderTools != null && needInjectSystemPrompt) {
                        String content = McpProviderTools.SYSTEM_PROMPT;
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

                    if (req.isEnableSkills() && needInjectSystemPrompt) {
                        String content = SkillsHelper.convertSkillDefinitionsAsSystemPrompt(SkillAutoConfiguration.skillDefinitionMap);
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

                    if (req.isEnableTools()) {
                        if (toolManager != null) {
                            List<ToolDefinition> tools = toolManager.getTools();
                            if (tools != null) {
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
                                if (completion.getTools() == null) {
                                    completion.setTools(new ArrayList<>());
                                }
                                for (ToolDefinition tool : tools) {
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
                                    Map<String, ToolDefinition> definitionMap = new HashMap<>();
                                    if (toolManager != null) {
                                        List<ToolDefinition> tools = toolManager.getTools();
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
                                            AgentTools.REQUEST_HOLDER.set(req);
                                            try {
                                                String id = call.getId();
                                                OpenAiToolCallFunction function = call.getFunction();

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
                                                    if (!toolManager.support(toolCallRequest)) {
                                                        throw new IllegalArgumentException("cannot found tool definition: " + toolCallRequest.getName());
                                                    }
                                                    callRet = toolManager.callTool(toolCallRequest);
                                                } catch (Throwable e) {
                                                    callRet = "call tool error! " + e.getClass() + ": " + e.getMessage();
                                                    log.warn(e.getMessage(), e);
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
                                                AgentTools.REQUEST_HOLDER.remove();
                                                latch.countDown();
                                            }
                                        };
                                        toolPool.submit(toolTask);
                                    }

                                    latch.await();
                                    AgentTools.REQUEST_HOLDER.set(req);
                                }
                            }
                        }
                    }

                    if (req.isEnableLruTools() && mcpProviderTools != null) {
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

                        Integer keepSize = req.getLruToolMaxSize();
                        if (keepSize == null || keepSize < 1) {
                            keepSize = 20;
                        }
                        while (unqToolNames.size() > 20) {
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

                        List<OpenAiToolsDefinition> tools = completion.getTools();
                        tools = tools.stream().filter(e -> {
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
                            if (unqToolNames.contains(name)) {
                                return true;
                            }
                            return false;
                        }).collect(Collectors.toList());
                        completion.setTools(tools);
                    }


                    String url = req.getMeta().getBaseUrl();
                    url = extraStandardBaseUrl(url);
                    url = url + "/chat/completions";

                    if (req.isEnableTruth()) {
                        String truthContent = req.getTruthContent();
                        OpenAiSystemMessage system = new OpenAiSystemMessage(truthContent);

                        OpenAiMessageVo dto = new OpenAiMessageVo();
                        dto.setType(OpsOpenAiConsts.ECHO_TRUTH);
                        dto.setEcho_truth(system);

                        String defTruthMsg = objectMapper.writeValueAsString(dto);
                        OpsSecureReturn<?> resp = null;
                        if (req.isEncryptOutput()) {
                            resp = transfer.success(defTruthMsg);
                        } else {
                            resp = OpsSecureReturn.success(defTruthMsg);
                        }
                        resp.withAttr("type", OpsOpenAiConsts.ECHO_TRUTH);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                    }

                    // 合并重排system消息，用于在一些严格的模型要求下，要求system消息只有一条，且只能在第一条的情况
                    if (req.isEnableMergedSystemMsg()) {

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

                    HttpRequest.doPost(url)
                            .set(u -> u::json)
                            .set2(u -> u::addHeader, HttpHeaderConstants.Authorization, HttpHeaderConstants.Bearer + " " + req.getMeta().getApiKey())
                            .set(u -> u::setData, completion)
                            .done()
                            .send(new SpringWebHttpProcessor(restTemplate),
                                    response -> {
                                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), StandardCharsets.UTF_8))) {
                                            String line = null;
                                            while ((line = reader.readLine()) != null) {
                                                if (line.startsWith("data:")) {
                                                    String data = line.substring(5).trim();
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
                                            String respJson = objectMapper.writeValueAsString(OpsSecureReturn.success("[DONE]"));
                                            emitter.send(respJson);
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
                AgentTools.REQUEST_HOLDER.remove();
                // System.out.println("前端 SSE 连接已正常关闭");
            });
            emitter.onTimeout(() -> {
                AgentTools.REQUEST_HOLDER.remove();
                System.out.println("前端 SSE 连接超时");
            });
            emitter.onError((ex) -> {
                AgentTools.REQUEST_HOLDER.remove();
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
}
