package i2f.ai.rest.openai.model;

import i2f.ai.rest.openai.model.data.*;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:33
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpOpenAiAiModel implements AiModel {
    protected IRestClient restClient;
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

        List<AiMessage> messageList = req.getMessageList();
        if (messageList != null) {
            for (AiMessage item : messageList) {
                if (item instanceof UserMessage) {
                    UserMessage msg = (UserMessage) item;
                    reqDto.getMessages().add(new OpenAiUserMessage(msg.getText()));
                } else if (item instanceof SystemMessage) {
                    SystemMessage msg = (SystemMessage) item;
                    reqDto.getMessages().add(new OpenAiSystemMessage(msg.getText()));
                } else if (item instanceof AssistantMessage) {
                    AssistantMessage msg = (AssistantMessage) item;
                    OpenAiAssistantMessage dto = new OpenAiAssistantMessage(msg.getText());
                    dto.setTool_calls(new ArrayList<>());

                    List<ToolCallRequest> toolCallRequestList = msg.getToolCallRequestList();
                    if (toolCallRequestList != null) {
                        for (ToolCallRequest toolCallRequest : toolCallRequestList) {
                            OpenAiToolCall toolCall = new OpenAiToolCall();
                            toolCall.setId(toolCallRequest.getId());
                            toolCall.setFunction(new OpenAiToolCallFunction(toolCallRequest.getName(), toolCallRequest.getArguments()));
                            dto.getTool_calls().add(toolCall);
                        }
                    }

                    reqDto.getMessages().add(dto);
                } else if (item instanceof ToolMessage) {
                    ToolMessage msg = (ToolMessage) item;
                    reqDto.getMessages().add(new OpenAiToolMessage(msg.getId(), msg.getText()));
                }
            }
        }

        OpenAiCompletionRespDto resp = doPostHttp(reqDto);

        List<OpenAiCompletionChoice> choiceList = resp.getChoices();
        OpenAiCompletionChoice choice = choiceList.get(0);

        AssistantMessage ret = new AssistantMessage();
        ret.setFinishReason(AssistantMessage.FinishReason.STOP);
        String finishReason = choice.getFinish_reason();
        if (OpenAiConsts.TOOL_CALLS.equalsIgnoreCase(finishReason)) {
            ret.setFinishReason(AssistantMessage.FinishReason.TOOL_CALL);
        }

        OpenAiAssistantMessage message = choice.getMessage();
        ret.setText(message.getContent());
        ret.setToolCallRequestList(new ArrayList<>());

        List<OpenAiToolCall> toolCalls = message.getTool_calls();
        if (toolCalls != null && !toolCalls.isEmpty()) {
            ret.setFinishReason(AssistantMessage.FinishReason.TOOL_CALL);

            for (OpenAiToolCall item : toolCalls) {
                OpenAiToolCallFunction func = item.getFunction();

                ToolCallRequest dto = new ToolCallRequest();
                dto.setId(item.getId());
                dto.setName(func.getName());
                dto.setArguments(func.getArguments());
                ret.getToolCallRequestList().add(dto);
            }
        }

        return ret;
    }

    public OpenAiCompletionRespDto doPostHttp(OpenAiCompletionReqDto req) {
        try {
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
            RestHttpResponse<OpenAiCompletionRespDto> resp = restClient.rest(RestHttpRequest.builder()
                            .url(getChatCompletionsUrl())
                            .method(HttpMethodConstants.POST)
                            .headers(HttpHeaders.create()
                                    .apply(headers -> {
                                        if (apiKey != null && !apiKey.isEmpty()) {
                                            headers.add("Authorization", "Bearer " + apiKey);
                                        }
                                    })
                            )
                            .body(reqMap)
                            .build(),
                    OpenAiCompletionRespDto.class);
            OpenAiCompletionRespDto ret = resp.getBody();
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
