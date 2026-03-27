package i2f.extension.ai.openai.model;


import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.*;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.extension.ai.openai.impl.OpenAiAi;
import i2f.extension.ai.openai.tool.OpenAiToolDefinition;
import i2f.extension.ai.openai.tool.OpenAiToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 10:41
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiModel implements AiModel {
    protected OpenAIClient client;
    protected String model = OpenAiAi.DEFAULT_MODEL;

    public OpenAiModel(OpenAIClient client) {
        this.client = client;
    }

    public static AiAgent agent(OpenAiModel model) {
        return new AiAgent()
                .model(model)
                .jsonSerializer(OpenAiJsonSerializer.INSTANCE);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder();
        protected String model;

        public Builder baseUrl(String baseUrl) {
            builder.baseUrl(baseUrl);
            return this;
        }

        public Builder apiKey(String apiKey) {
            builder.apiKey(apiKey);
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public OpenAiModel build() {
            OpenAiModel ret = new OpenAiModel(builder.build());
            if (model != null && !model.isEmpty()) {
                ret.model(model);
            }
            return ret;
        }
    }

    public OpenAiModel model(String model) {
        this.model = model;
        return this;
    }

    @Override
    public AssistantMessage generate(AiRequest req) {
        List<ChatCompletionMessageParam> messageList = new ArrayList<>();
        List<AiMessage> rawMessage = req.getMessageList();
        for (AiMessage rawItem : rawMessage) {
            if (rawItem instanceof UserMessage) {
                UserMessage rawMsg = (UserMessage) rawItem;
                ChatCompletionMessageParam item = null;
                if (rawMsg.getRawMessage() instanceof ChatCompletionMessageParam) {
                    item = (ChatCompletionMessageParam) rawMsg.getRawMessage();
                } else {
                    item = ChatCompletionMessageParam.ofUser(ChatCompletionUserMessageParam.builder()
                            .content(rawMsg.text())
                            .build());

                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof SystemMessage) {
                SystemMessage rawMsg = (SystemMessage) rawItem;
                ChatCompletionMessageParam item = null;
                if (rawMsg.getRawMessage() instanceof ChatCompletionMessageParam) {
                    item = (ChatCompletionMessageParam) rawMsg.getRawMessage();
                } else {
                    item = ChatCompletionMessageParam.ofSystem(ChatCompletionSystemMessageParam.builder()
                            .content(rawMsg.text())
                            .build());
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof ToolMessage) {
                ToolMessage rawMsg = (ToolMessage) rawItem;
                ChatCompletionMessageParam item = null;
                if (rawMsg.getRawMessage() instanceof ChatCompletionMessageParam) {
                    item = (ChatCompletionMessageParam) rawMsg.getRawMessage();
                } else {
                    item = ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder()
                            .content(rawMsg.getText())
                            .toolCallId(rawMsg.getId())
                            .build());
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof AssistantMessage) {
                AssistantMessage rawMsg = (AssistantMessage) rawItem;
                ChatCompletionMessageParam item = null;
                if (rawMsg.getRawMessage() instanceof ChatCompletionMessageParam) {
                    item = (ChatCompletionMessageParam) rawMsg.getRawMessage();
                } else {
                    List<ToolCallRequest> rawToolCallList = rawMsg.getToolCallRequestList();
                    List<ChatCompletionMessageToolCall> toolExecList = new ArrayList<>();
                    if (rawToolCallList != null && !rawToolCallList.isEmpty()) {
                        for (ToolCallRequest rawCall : rawToolCallList) {
                            ChatCompletionMessageToolCall request = null;
                            if (rawCall.getRawRequest() instanceof ChatCompletionMessageToolCall) {
                                request = (ChatCompletionMessageToolCall) rawCall.getRawRequest();
                            } else {

                                ChatCompletionMessageToolCall func = ChatCompletionMessageToolCall.ofFunction(ChatCompletionMessageFunctionToolCall.builder()
                                        .id(rawCall.getId())
                                        .function(ChatCompletionMessageFunctionToolCall.Function.builder()
                                                .name(rawCall.getName())
                                                .arguments(rawCall.getArguments())
                                                .build())
                                        .build());

                                request = func;
                            }
                            rawCall.setRawRequest(request);
                            toolExecList.add(request);
                        }
                    }


                    item = ChatCompletionMessageParam.ofAssistant(ChatCompletionAssistantMessageParam.builder()
                            .toolCalls(toolExecList)
                            .content(rawMsg.getText())
                            .build());
                    rawMsg.setRawMessage(item);
                }
                messageList.add(item);
            }
        }

        Map<String, OpenAiToolDefinition> toolMap = new LinkedHashMap<>();
        Map<String, ToolRawDefinition> toolRawMap = req.getToolMap();

        for (Map.Entry<String, ToolRawDefinition> entry : toolRawMap.entrySet()) {
            toolMap.put(entry.getKey(), OpenAiToolHelper.fromRaw(entry.getValue()));
        }

        ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                .model(model) // 推荐使用支持 tool 的模型
                .messages(messageList)
                .tools(OpenAiToolHelper.convertTools(toolMap))
                .n(1); // 只返回一个 choice

        ChatCompletion call = client.chat().completions().create(paramsBuilder.build());
        List<ChatCompletion.Choice> choices = call.choices();

        AssistantMessage ret = new AssistantMessage();
        ret.setRawMessage(call);

        if (choices == null || choices.isEmpty()) {
            ret.setFinishReason(AssistantMessage.FinishReason.STOP);
            ret.setText("");
            return ret;
        } else {
            List<String> textList = new ArrayList<>();
            List<ToolCallRequest> list = new ArrayList<>();
            for (ChatCompletion.Choice choice : choices) {
                ChatCompletion.Choice.FinishReason finishReason = choice.finishReason();
                ChatCompletionMessage message = choice.message();
                if (ChatCompletion.Choice.FinishReason.TOOL_CALLS.equals(finishReason)) {
                    List<ChatCompletionMessageToolCall> toolCalls = message.toolCalls().orElse(new ArrayList<>());
                    for (ChatCompletionMessageToolCall toolCall : toolCalls) {
                        if (toolCall.isFunction()) {
                            ChatCompletionMessageFunctionToolCall callFunction = toolCall.asFunction();
                            ChatCompletionMessageFunctionToolCall.Function function = callFunction.function();

                            ToolCallRequest vo = new ToolCallRequest();
                            vo.setId(callFunction.id());
                            vo.setName(function.name());
                            vo.setArguments(function.arguments());
                            vo.setRawRequest(callFunction);
                            list.add(vo);
                        }
                    }
                } else {
                    textList.add(message.content().orElse(""));
                }
            }

            ret.setToolCallRequestList(list);
            ret.setFinishReason(list.isEmpty() ? AssistantMessage.FinishReason.STOP : AssistantMessage.FinishReason.TOOL_CALL);
            ret.setText(textList.isEmpty() ? "" : String.join("\n\n", textList));
        }

        return ret;
    }
}
