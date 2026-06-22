package i2f.springboot.ops.openai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawDefinitionsProvider;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.openai.data.*;
import i2f.springboot.ops.openai.data.message.*;
import i2f.springboot.ops.openai.skill.SkillAutoConfiguration;
import i2f.springboot.ops.openai.tool.impl.a2a.AgentTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
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

    private RestTemplate restTemplate = createRestTemplate();

    private ExecutorService pool = Executors.newWorkStealingPool(Math.min(Math.max(Runtime.getRuntime().availableProcessors() * 4 + 2, 16), 512));

    private ExecutorService toolPool = Executors.newWorkStealingPool(Math.min(Math.max(Runtime.getRuntime().availableProcessors() * 4 + 2, 16), 512));

    @Autowired(required = false)
    private ToolRawDefinitionsProvider toolDefinitionProvider;
    @Autowired
    private SkillsTools skillsTools;

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
        String completionsSuffix = "/chat/completions";
        if (url.endsWith(completionsSuffix)) {
            url = url.substring(0, url.length() - completionsSuffix.length());
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

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody OpsSecureDto reqDto) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        AtomicReference<OpenAiOperateDto> reqRef = new AtomicReference<>();
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


                    List<OpenAiMessageVo> voMsgList = vo.getMessages();
                    if (voMsgList != null) {
                        for (OpenAiMessageVo item : voMsgList) {
                            if (OpenAiConsts.USER.equals(item.getType())) {
                                completion.getMessages().add(item.getUser());
                            } else if (OpenAiConsts.SYSTEM.equals(item.getType())) {
                                completion.getMessages().add(item.getSystem());
                            } else if (OpenAiConsts.ASSISTANT.equals(item.getType())) {
                                completion.getMessages().add(item.getAssistant());
                            } else if (OpenAiConsts.TOOL.equals(item.getType())) {
                                completion.getMessages().add(item.getTool());
                            }
                        }
                    }
                    completion.setTools(vo.getTools());

                    if (req.isEnableSkills()) {
                        completion.getMessages().add(new OpenAiSystemMessage(SkillsHelper.convertSkillDefinitionsAsSystemPrompt(SkillAutoConfiguration.skillDefinitionMap)));
                    }

                    if (req.isEnableTools()) {
                        if (toolDefinitionProvider != null) {
                            List<ToolRawDefinition> tools = toolDefinitionProvider.getTools();
                            if (tools != null) {
                                if (!req.isEnableSkills()) {
                                    tools = tools.stream()
                                            .filter(e -> !SkillsTools.class.isAssignableFrom(e.getBindClass()))
                                            .collect(Collectors.toList());
                                }
                                if (completion.getTools() == null) {
                                    completion.setTools(new ArrayList<>());
                                }
                                for (ToolRawDefinition tool : tools) {
                                    OpenAiToolDefinitionDto dto = new OpenAiToolDefinitionDto();
                                    dto.setName(tool.getName());
                                    dto.setDescription(tool.getDescription());
                                    dto.setParameterNames(tool.getParameterNames());
                                    dto.setTags(tool.getTags());
                                    if (tool.getBindClass() != null) {
                                        dto.setBindClass(tool.getBindClass().getSimpleName());
                                    }
                                    if (tool.getBindMethod() != null) {
                                        dto.setBindMethod(tool.getBindMethod().getName());
                                    }
                                    String defToolMsg = objectMapper.writeValueAsString(dto);
                                    OpsSecureReturn<?> resp = null;
                                    if (req.isEncryptOutput()) {
                                        resp = transfer.success(defToolMsg);
                                    } else {
                                        resp = OpsSecureReturn.success(defToolMsg);
                                    }
                                    resp.withAttr("type", OpenAiConsts.DEFINITION_TOOL);
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
                                    Map<String, ToolRawDefinition> definitionMap = new HashMap<>();
                                    if (toolDefinitionProvider != null) {
                                        List<ToolRawDefinition> tools = toolDefinitionProvider.getTools();
                                        if (!req.isEnableSkills()) {
                                            tools = tools.stream()
                                                    .filter(e -> !SkillsTools.class.isAssignableFrom(e.getBindClass()))
                                                    .collect(Collectors.toList());
                                        }
                                        for (ToolRawDefinition tool : tools) {
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
                                        Runnable toolTask = () -> {
                                            AgentTools.REQUEST_HOLDER.set(req);
                                            try {
                                                String id = call.getId();
                                                OpenAiToolCallFunction function = call.getFunction();
                                                String name = function.getName();
                                                String arguments = function.getArguments();
                                                ToolRawDefinition definition = definitionMap.get(name);
                                                Map<String, Object> argumentsMap = objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {
                                                });
                                                Object callRet = null;
                                                try {
                                                    OpenAiToolApprovalDto approvalDto = approvalMap.get(id);
                                                    if (approvalDto != null) {
                                                        if (approvalDto.isReject()) {
                                                            String rejectReason = approvalDto.getRejectReason();
                                                            if (rejectReason == null) {
                                                                rejectReason = "";
                                                            } else {
                                                                rejectReason = ", reason is : " + rejectReason;
                                                            }
                                                            throw new IllegalStateException("user reject tool execute" + rejectReason);
                                                        }
                                                    }
                                                    if (definition == null) {
                                                        throw new IllegalArgumentException("cannot found tool definition: " + name);
                                                    }
                                                    callRet = ToolRawHelper.invokeTool(definition, argumentsMap);
                                                } catch (Throwable e) {
                                                    callRet = "call tool error! " + e.getClass() + ": " + e.getMessage();
                                                }
                                                if (callRet instanceof CharSequence) {
                                                    callRet = String.valueOf(callRet);
                                                } else {
                                                    callRet = objectMapper.writeValueAsString(callRet);
                                                }
                                                OpenAiToolMessage toolMsg = OpenAiToolMessage.builder()
                                                        .tool_call_id(id)
                                                        .content(String.valueOf(callRet))
                                                        .build();

                                                if (toolMsg != null) {
                                                    EchoOpenAiToolMessage toolEchoMsg = EchoOpenAiToolMessage.builder()
                                                            .message(toolMsg)
                                                            .function(function)
                                                            .build();
                                                    toolEchoMsg.createContent();
                                                    OpenAiMessageVo toolEchoVo = OpenAiMessageVo.builder()
                                                            .type(OpenAiConsts.ECHO_TOOL)
                                                            .echo_tool(toolEchoMsg)
                                                            .build();
                                                    String emitToolMsg = objectMapper.writeValueAsString(toolEchoVo);
                                                    OpsSecureReturn<?> resp = null;
                                                    if (req.isEncryptOutput()) {
                                                        resp = transfer.success(emitToolMsg);
                                                    } else {
                                                        resp = OpsSecureReturn.success(emitToolMsg);
                                                    }
                                                    resp.withAttr("type", OpenAiConsts.ECHO_TOOL);
                                                    String respJson = objectMapper.writeValueAsString(resp);
                                                    emitter.send(respJson);
                                                }
                                                if (toolMsg != null) {
                                                    OpenAiMessageVo toolEchoVo = OpenAiMessageVo.builder()
                                                            .type(OpenAiConsts.TOOL)
                                                            .tool(toolMsg)
                                                            .build();
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


                    restTemplate.execute(req.getMeta().getBaseUrl(), HttpMethod.POST, request -> {
                        request.getHeaders().add("Authorization", "Bearer " + req.getMeta().getApiKey());
                        request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                        OutputStream body = request.getBody();
                        objectMapper.writeValue(body, completion);
                        body.close();
                    }, new ResponseExtractor<Void>() {
                        @Override
                        public Void extractData(ClientHttpResponse response) throws IOException {
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
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
                                    }
                                }
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
                        }
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
