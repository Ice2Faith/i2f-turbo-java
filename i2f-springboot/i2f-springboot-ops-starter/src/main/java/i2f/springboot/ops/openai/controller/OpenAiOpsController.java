package i2f.springboot.ops.openai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
public class OpenAiOpsController  implements IOpsProvider {
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
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);

            CompletableFuture.runAsync(() -> {
                try {
                    restTemplate.execute(req.getMeta().getBaseUrl(), HttpMethod.POST, request->{
                        request.getHeaders().add("Authorization","Bearer "+req.getMeta().getApiKey());
                        request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                        OutputStream body = request.getBody();
                        objectMapper.writeValue(body,req.getCompletion());
                        body.close();
                    }, new ResponseExtractor<Void>() {
                        @Override
                        public Void extractData(ClientHttpResponse response) throws IOException {
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    if (line.startsWith("data:")) {
                                        String data = line.substring(5).trim();
                                        OpsSecureReturn<OpsSecureDto> resp = transfer.success(data);
                                        String respJson = objectMapper.writeValueAsString(resp);
                                        emitter.send(respJson);
                                    }
                                }
                            } catch (Exception e) {
                                try {
                                    OpsSecureReturn<OpsSecureDto> resp = transfer.error(e);
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
                        OpsSecureReturn<OpsSecureDto> resp = transfer.error(e);
                        String respJson = objectMapper.writeValueAsString(resp);
                        emitter.send(respJson);
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                }
            });

//            emitter.onCompletion(() -> System.out.println("前端 SSE 连接已正常关闭"));
            emitter.onTimeout(() -> System.out.println("前端 SSE 连接超时"));
            emitter.onError((ex) -> System.err.println("前端 SSE 发生错误: " + ex.getMessage()));

        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            OpsSecureReturn<OpsSecureDto> resp = transfer.error(e);
            String respJson = objectMapper.writeValueAsString(resp);
            emitter.send(respJson);
            emitter.complete();
        }
        return emitter;
    }
}
