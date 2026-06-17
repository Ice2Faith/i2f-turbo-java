package i2f.springboot.ops.openai.tool.impl.a2a;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.annotations.Tools;
import i2f.springboot.ops.openai.data.OpenAiCompletionDto;
import i2f.springboot.ops.openai.data.OpenAiMeta;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author Ice2Faith
 * @date 2026/6/17 14:06
 * @desc
 */
@ConditionalOnExpression("${ai.tools.a2a.enable:true}")
@Component
@Tools
public class AgentTools {
    private RestTemplate restTemplate = createRestTemplate();

    private RestTemplate createRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(5))
                .build();
    }

    @Autowired
    private ObjectMapper objectMapper;

    public static InheritableThreadLocal<OpenAiOperateDto> REQUEST_HOLDER = new InheritableThreadLocal<>();

    public String generate(OpenAiMeta meta, OpenAiCompletionDto completion) {
        // 强制关闭流式输出
        completion.setStream(false);

        return restTemplate.execute(meta.getBaseUrl(), HttpMethod.POST, request -> {
            request.getHeaders().add("Authorization", "Bearer " + meta.getApiKey());
            request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            OutputStream body = request.getBody();
            objectMapper.writeValue(body, completion);
            body.close();
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
    }


}
