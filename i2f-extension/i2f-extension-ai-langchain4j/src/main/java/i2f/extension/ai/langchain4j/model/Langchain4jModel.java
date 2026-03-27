package i2f.extension.ai.langchain4j.model;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolDefinition;
import i2f.extension.ai.langchain4j.tool.Langchain4jToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/25 17:44
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4jModel implements AiModel {
    protected ChatModel chatModel;

    public Langchain4jModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public static AiAgent agent(Langchain4jModel model) {
        return new AiAgent()
                .model(model)
                .jsonSerializer(Langchain4jJsonSerializer.INSTANCE);
    }

    public static Langchain4jOpenAiModel.OpenAiBuilder openai() {
        return new Langchain4jOpenAiModel.OpenAiBuilder();
    }


    @Override
    public AssistantMessage generate(AiRequest req) {
        List<ChatMessage> messageList = new ArrayList<>();
        List<i2f.ai.std.model.message.AiMessage> rawMessage = req.getMessageList();
        for (i2f.ai.std.model.message.AiMessage rawItem : rawMessage) {
            if (rawItem instanceof UserMessage) {
                UserMessage rawMsg = (UserMessage) rawItem;
                dev.langchain4j.data.message.UserMessage item = null;
                if (rawMsg.getRawMessage() instanceof dev.langchain4j.data.message.UserMessage) {
                    item = (dev.langchain4j.data.message.UserMessage) rawMsg.getRawMessage();
                } else {
                    item = new dev.langchain4j.data.message.UserMessage(rawMsg.text());
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof SystemMessage) {
                SystemMessage rawMsg = (SystemMessage) rawItem;
                dev.langchain4j.data.message.SystemMessage item = null;
                if (rawMsg.getRawMessage() instanceof dev.langchain4j.data.message.SystemMessage) {
                    item = (dev.langchain4j.data.message.SystemMessage) rawMsg.getRawMessage();
                } else {
                    item = new dev.langchain4j.data.message.SystemMessage(rawMsg.text());
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof ToolMessage) {
                ToolMessage rawMsg = (ToolMessage) rawItem;
                dev.langchain4j.data.message.ToolExecutionResultMessage item = null;
                if (rawMsg.getRawMessage() instanceof dev.langchain4j.data.message.ToolExecutionResultMessage) {
                    item = (dev.langchain4j.data.message.ToolExecutionResultMessage) rawMsg.getRawMessage();
                } else {
                    item = new dev.langchain4j.data.message.ToolExecutionResultMessage(rawMsg.getId(), rawMsg.getRequest().getName(), rawMsg.text());
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof AssistantMessage) {
                AssistantMessage rawMsg = (AssistantMessage) rawItem;
                AiMessage item = null;
                if (rawMsg.getRawMessage() instanceof AiMessage) {
                    item = (AiMessage) rawMsg.getRawMessage();
                } else {
                    String text = rawMsg.getText();
                    List<ToolCallRequest> rawToolCallList = rawMsg.getToolCallRequestList();
                    List<ToolExecutionRequest> toolExecList = new ArrayList<>();
                    if (rawToolCallList != null && !rawToolCallList.isEmpty()) {
                        for (ToolCallRequest rawCall : rawToolCallList) {
                            ToolExecutionRequest request = null;
                            if (rawCall.getRawRequest() instanceof ToolExecutionRequest) {
                                request = (ToolExecutionRequest) rawCall.getRawRequest();
                            } else {
                                request = ToolExecutionRequest.builder()
                                        .id(rawCall.getId())
                                        .name(rawCall.getName())
                                        .arguments(rawCall.getArguments())
                                        .build();
                            }
                            rawCall.setRawRequest(request);
                            toolExecList.add(request);
                        }
                    }

                    if (text != null && !toolExecList.isEmpty()) {
                        item = new AiMessage(text, toolExecList);
                    } else if (!toolExecList.isEmpty()) {
                        item = new AiMessage(toolExecList);
                    } else {
                        item = new AiMessage(text);
                    }
                    rawMsg.setRawMessage(item);
                }
                messageList.add(item);
            }
        }

        Map<String, Langchain4jToolDefinition> toolMap = new LinkedHashMap<>();
        Map<String, ToolRawDefinition> toolRawMap = req.getToolMap();

        for (Map.Entry<String, ToolRawDefinition> entry : toolRawMap.entrySet()) {
            toolMap.put(entry.getKey(), Langchain4jToolHelper.fromRaw(entry.getValue()));
        }

        ChatResponse resp = chatModel.chat(ChatRequest.builder()
                .messages(messageList)
                .toolSpecifications(Langchain4jToolHelper.convertTools(toolMap))
                .build());

        AssistantMessage ret = new AssistantMessage();
        FinishReason finishReason = resp.finishReason();
        AiMessage message = resp.aiMessage();

        ret.setRawMessage(message);
        ret.setFinishReason(finishReason == FinishReason.TOOL_EXECUTION ? AssistantMessage.FinishReason.TOOL_CALL : AssistantMessage.FinishReason.STOP);
        ret.setText(message.text());

        List<ToolExecutionRequest> toolExecutionRequests = message.toolExecutionRequests();
        if (toolExecutionRequests != null && !toolExecutionRequests.isEmpty()) {
            List<ToolCallRequest> list = new ArrayList<>();
            for (ToolExecutionRequest item : toolExecutionRequests) {
                ToolCallRequest vo = new ToolCallRequest();
                vo.setId(item.id());
                vo.setName(item.name());
                vo.setArguments(item.arguments());
                vo.setRawRequest(item);
                list.add(vo);
            }
            ret.setToolCallRequestList(list);
        }

        return ret;
    }
}
