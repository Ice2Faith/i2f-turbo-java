package i2f.ai.rest.openai.model;

import i2f.ai.rest.openai.model.data.*;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.net.http.rest.impl.HttpProcessorRestClient;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:33
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiAiModel implements AiModel {
    protected IRestClient restClient = new HttpProcessorRestClient();
    protected String baseUrl;
    protected String apiKey;
    protected String model;

    public String getChatCompletionsUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/chat/completions";
    }


    @Override
    public AssistantMessage generate(AiRequest req) {

        OpenAiCompletionReqDto reqDto = new OpenAiCompletionReqDto();
        // 不开启流
        reqDto.setStream(null);
        reqDto.setStream_options(null);
        reqDto.setModel(model);
        reqDto.setTools(new ArrayList<>());
        reqDto.setMessages(new ArrayList<>());

        Map<String, ToolRawDefinition> toolMap = req.getToolMap();
        if (toolMap != null) {
            for (Map.Entry<String, ToolRawDefinition> entry : toolMap.entrySet()) {
                ToolRawDefinition tool = entry.getValue();
                OpenAiToolsDefinition def = new OpenAiToolsDefinition(tool.getJsonSchema());
                reqDto.getTools().add(def);
            }
        }

        reqDto.getMessages().addAll(OpenAiMessageHelper.toOpenAiMessages(req.getMessageList()));

        OpenAiCompletionRespDto resp = completion(reqDto);

        List<OpenAiCompletionChoice> choiceList = resp.getChoices();
        OpenAiCompletionChoice choice = choiceList.get(0);

        OpenAiAssistantMessageRespDto message = choice.getMessage();
        AssistantMessage ret = OpenAiMessageHelper.fromOpenAiAssistantMessage(message);
        return ret;
    }

    public OpenAiCompletionRespDto completion(OpenAiCompletionReqDto req) {
        try {
            // 不开启流
            req.setStream(null);
            req.setStream_options(null);
            // openai 标准有强制的字段校验，因此空字段需要移除掉
            Map<String, Object> reqMap = new HashMap<>();
            ReflectResolver.bean2map(req, reqMap);
            Set<String> removeKeys = new HashSet<>();
            for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    removeKeys.add(entry.getKey());
                } else if (value instanceof Collection) {
                    Collection<?> col = (Collection<?>) value;
                    if (col.isEmpty()) {
                        removeKeys.add(entry.getKey());
                    }
                } else if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    if (map.isEmpty()) {
                        removeKeys.add(entry.getKey());
                    }
                }
            }
            for (String key : removeKeys) {
                reqMap.remove(key);
            }
            RestHttpResponse<OpenAiCompletionRespDto> resp = restClient.rest(new RestHttpRequest().toBuilder()
                            .set(u -> u::setUrl, getChatCompletionsUrl())
                            .set(u -> u::setMethod, HttpMethodConstants.POST)
                            .set(u -> u::setHeaders, HttpHeaders.create()
                                    .apply(headers -> {
                                        if (apiKey != null && !apiKey.isEmpty()) {
                                            headers.add("Authorization", "Bearer " + apiKey);
                                        }
                                    })
                            )
                            .set(u -> u::setBody, reqMap)
                            .build(),
                    OpenAiCompletionRespDto.class);
            OpenAiCompletionRespDto ret = resp.getBody();
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
