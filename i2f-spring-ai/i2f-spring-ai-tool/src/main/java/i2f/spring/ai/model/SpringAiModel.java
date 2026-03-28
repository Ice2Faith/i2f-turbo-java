package i2f.spring.ai.model;


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
import i2f.spring.ai.tool.SpringAiToolDefinition;
import i2f.spring.ai.tool.SpringAiToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/27 10:41
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringAiModel implements AiModel {
    public static final String DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    public static final String DEFAULT_MODEL = "qwen-plus";

    protected ChatModel client;
    protected String model = DEFAULT_MODEL;

    public SpringAiModel(ChatModel client) {
        this.client = client;
    }

    public static AiAgent agent(SpringAiModel model) {
        return new AiAgent()
                .model(model)
                .jsonSerializer(SpringAiJsonSerializer.INSTANCE);
    }

    public static SpringAiOpenAiModel.OpenAiBuilder openai() {
        return SpringAiOpenAiModel.openai();
    }

    public SpringAiModel model(String model) {
        this.model = model;
        return this;
    }

    @Override
    public AssistantMessage generate(AiRequest req) {
        List<Message> messageList = new ArrayList<>();
        List<AiMessage> rawMessage = req.getMessageList();
        for (AiMessage rawItem : rawMessage) {
            if (rawItem instanceof UserMessage) {
                UserMessage rawMsg = (UserMessage) rawItem;
                org.springframework.ai.chat.messages.UserMessage item = null;
                if (rawMsg.getRawMessage() instanceof org.springframework.ai.chat.messages.UserMessage) {
                    item = (org.springframework.ai.chat.messages.UserMessage) rawMsg.getRawMessage();
                } else {
                    item = org.springframework.ai.chat.messages.UserMessage.builder()
                            .text(rawMsg.text())
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof SystemMessage) {
                SystemMessage rawMsg = (SystemMessage) rawItem;
                org.springframework.ai.chat.messages.SystemMessage item = null;
                if (rawMsg.getRawMessage() instanceof org.springframework.ai.chat.messages.SystemMessage) {
                    item = (org.springframework.ai.chat.messages.SystemMessage) rawMsg.getRawMessage();
                } else {
                    item = org.springframework.ai.chat.messages.SystemMessage.builder()
                            .text(rawMsg.text())
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof ToolMessage) {
                ToolMessage rawMsg = (ToolMessage) rawItem;
                ToolResponseMessage item = null;
                if (rawMsg.getRawMessage() instanceof ToolResponseMessage) {
                    item = (ToolResponseMessage) rawMsg.getRawMessage();
                } else {
                    item = ToolResponseMessage.builder()
                            .responses(new ArrayList<>(Collections.singletonList(
                                    new ToolResponseMessage.ToolResponse(rawMsg.getId(), rawMsg.getDefinition().getFunctionName(), rawItem.text())
                            )))
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof AssistantMessage) {
                AssistantMessage rawMsg = (AssistantMessage) rawItem;
                org.springframework.ai.chat.messages.AssistantMessage item = null;
                if (rawMsg.getRawMessage() instanceof org.springframework.ai.chat.messages.AssistantMessage) {
                    item = (org.springframework.ai.chat.messages.AssistantMessage) rawMsg.getRawMessage();
                } else {
                    List<ToolCallRequest> rawToolCallList = rawMsg.getToolCallRequestList();
                    List<org.springframework.ai.chat.messages.AssistantMessage.ToolCall> toolExecList = new ArrayList<>();
                    if (rawToolCallList != null && !rawToolCallList.isEmpty()) {
                        for (ToolCallRequest rawCall : rawToolCallList) {
                            org.springframework.ai.chat.messages.AssistantMessage.ToolCall request = null;
                            if (rawCall.getRawRequest() instanceof org.springframework.ai.chat.messages.AssistantMessage.ToolCall) {
                                request = (org.springframework.ai.chat.messages.AssistantMessage.ToolCall) rawCall.getRawRequest();
                            } else {

                                request = new org.springframework.ai.chat.messages.AssistantMessage.ToolCall(
                                        rawCall.getId(),
                                        "function",
                                        rawCall.getName(),
                                        rawCall.getArguments());
                            }
                            rawCall.setRawRequest(request);
                            toolExecList.add(request);
                        }
                    }

                    item = org.springframework.ai.chat.messages.AssistantMessage.builder()
                            .content(rawMsg.text())
                            .toolCalls(toolExecList)
                            .build();
                    rawMsg.setRawMessage(item);
                }
                messageList.add(item);
            }
        }

        Map<String, SpringAiToolDefinition> toolMap = new LinkedHashMap<>();
        Map<String, ToolRawDefinition> toolRawMap = req.getToolMap();

        for (Map.Entry<String, ToolRawDefinition> entry : toolRawMap.entrySet()) {
            toolMap.put(entry.getKey(), SpringAiToolHelper.fromRaw(entry.getValue()));
        }

        ChatResponse call = client.call(Prompt.builder()
                .messages(messageList)
                .chatOptions(ToolCallingChatOptions.builder()
                        .toolCallbacks(SpringAiToolHelper.convertTools(toolMap))
                        .internalToolExecutionEnabled(false)
                        .model(model)
                        .build())
                .build());


        List<Generation> choices = call.getResults();

        AssistantMessage ret = new AssistantMessage();
        ret.setRawMessage(call);

        if (choices == null || choices.isEmpty()) {
            ret.setFinishReason(AssistantMessage.FinishReason.STOP);
            ret.setText("");
            return ret;
        } else {
            List<String> textList = new ArrayList<>();
            List<ToolCallRequest> list = new ArrayList<>();
            for (Generation choice : choices) {
                ChatGenerationMetadata metadata = choice.getMetadata();
                String finishReason = metadata.getFinishReason();
                org.springframework.ai.chat.messages.AssistantMessage message = choice.getOutput();
                if (message.hasToolCalls()) {
                    List<org.springframework.ai.chat.messages.AssistantMessage.ToolCall> toolCalls = message.getToolCalls();
                    for (org.springframework.ai.chat.messages.AssistantMessage.ToolCall toolCall : toolCalls) {

                        ToolCallRequest vo = new ToolCallRequest();
                        vo.setId(toolCall.id());
                        vo.setName(toolCall.name());
                        vo.setArguments(toolCall.arguments());
                        vo.setRawRequest(toolCall);
                        list.add(vo);

                    }
                } else {
                    textList.add(message.getText());
                }
            }

            ret.setToolCallRequestList(list);
            ret.setFinishReason(list.isEmpty() ? AssistantMessage.FinishReason.STOP : AssistantMessage.FinishReason.TOOL_CALL);
            ret.setText(textList.isEmpty() ? "" : String.join("\n\n", textList));
        }

        return ret;
    }
}
