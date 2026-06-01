package i2f.springboot.ops.openai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawDefinitionsProvider;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.openai.data.OpenAiCompletionDto;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
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
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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

    private RestTemplate restTemplate=createRestTemplate();

    private ExecutorService pool= Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors()*4+2);

    @Autowired(required = false)
    private ToolRawDefinitionsProvider toolDefinitionProvider;
    @Autowired
    private RouterFunctionMapping routerFunctionMapping;

    private RestTemplate createRestTemplate(){
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

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody OpsSecureDto reqDto) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        AtomicReference<OpenAiOperateDto> reqRef=new AtomicReference<>();
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            reqRef.set(req);

            CompletableFuture.runAsync(() -> {
                try {
                    OpenAiCompletionDto completion = req.getCompletion();
                    if (req.isEnableTools()) {
                        if (toolDefinitionProvider != null) {
                            List<ToolRawDefinition> tools = toolDefinitionProvider.getTools();
                            if (tools != null) {
                                if (completion.getTools() == null) {
                                    completion.setTools(new ArrayList<>());
                                }
                                for (ToolRawDefinition tool : tools) {
                                    Map<String, Object> function = new HashMap<>();
                                    function.put("type", "function");
                                    function.put("function", tool.getJsonSchema());
                                    completion.getTools().add(function);
                                }
                            }
                        }

                        List<Map<String, Object>> messages = completion.getMessages();
                        if (messages != null && !messages.isEmpty()) {
                            Map<String, Object> lastMsg = messages.get(messages.size() - 1);
                            // TODO 处理 tools-call-request, 最后一条是工具调用，或者是授权调用
                            if (Objects.equals("assistant", lastMsg.get("role"))) {
                                List<Map<String, Object>> calls = (List<Map<String, Object>>) lastMsg.get("tool_calls");
                                if (calls != null && !calls.isEmpty()) {
                                    List<ToolRawDefinition> tools = toolDefinitionProvider.getTools();
                                    Map<String, ToolRawDefinition> definitionMap = new HashMap<>();
                                    for (ToolRawDefinition tool : tools) {
                                        definitionMap.put(tool.getName(), tool);
                                    }
                                    for (Map<String, Object> call : calls) {
                                        String id = (String) call.get("id");
                                        Map<String, Object> funcMap = (Map<String, Object>) call.get("function");
                                        String name = (String) funcMap.get("name");
                                        String arguments = (String) funcMap.get("arguments");
                                        ToolRawDefinition definition = definitionMap.get(name);
                                        Map<String, Object> argumentsMap = objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {
                                        });
                                        Object callRet = null;
                                        try {
                                            callRet = ToolRawHelper.invokeTool(definition, argumentsMap);
                                        } catch (Throwable e) {
                                            callRet = "call tool error! " + e.getClass() + ": " + e.getMessage();
                                        }
                                        if (callRet instanceof CharSequence) {
                                            callRet = String.valueOf(callRet);
                                        } else {
                                            callRet = objectMapper.writeValueAsString(callRet);
                                        }
                                        Map<String, Object> toolMsg = new HashMap<>();
                                        toolMsg.put("role", "tool");
                                        toolMsg.put("tool_call_id", id);
                                        toolMsg.put("content", callRet);

                                        Map<String, Object> emitToolMsgMap = new HashMap<>(toolMsg);
                                        emitToolMsgMap.put("function", funcMap);
                                        String emitToolMsg = objectMapper.writeValueAsString(emitToolMsgMap);
                                        OpsSecureReturn<?> resp = null;
                                        if (req.isEncryptOutput()) {
                                            resp = transfer.success(emitToolMsg);
                                        } else {
                                            resp = OpsSecureReturn.success(emitToolMsg);
                                        }
                                        String respJson = objectMapper.writeValueAsString(resp);
                                        emitter.send(respJson);

                                        messages.add(toolMsg);
                                    }
                                }
                            }
                        }
                    }


                    restTemplate.execute(req.getMeta().getBaseUrl(), HttpMethod.POST, request->{
                        request.getHeaders().add("Authorization","Bearer "+req.getMeta().getApiKey());
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
                                    System.out.println("line==>" + line);
                                    if (line.startsWith("data:")) {
                                        String data = line.substring(5).trim();
                                        OpsSecureReturn<?> resp = null;
                                        if(req.isEncryptOutput()){
                                            resp=transfer.success(data);
                                        }else{
                                            resp=OpsSecureReturn.success(data);
                                        }
                                        String respJson = objectMapper.writeValueAsString(resp);
                                        emitter.send(respJson);
                                    }
                                }
                            } catch (Exception e) {
                                try {
                                    OpsSecureReturn<?> resp = null;
                                    if(req.isEncryptOutput()) {
                                        resp=transfer.error(e);
                                    }else{
                                        resp=OpsSecureReturn.error(e);
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
                        if(req.isEncryptOutput()) {
                            resp=transfer.error(e);
                        }else{
                            resp=OpsSecureReturn.error(e);
                        }
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                }
            },pool);

//            emitter.onCompletion(() -> System.out.println("前端 SSE 连接已正常关闭"));
            emitter.onTimeout(() -> System.out.println("前端 SSE 连接超时"));
            emitter.onError((ex) -> System.err.println("前端 SSE 发生错误: " + ex.getMessage()));

        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            OpsSecureReturn<?> resp = null;
            OpenAiOperateDto req = reqRef.get();
            if(req!=null && req.isEncryptOutput()) {
                resp=transfer.error(e);
            }else{
                resp=OpsSecureReturn.error(e);
            }
            String respJson = objectMapper.writeValueAsString(resp);
            emitter.send(respJson);
            emitter.complete();
        }
        return emitter;
    }
}
