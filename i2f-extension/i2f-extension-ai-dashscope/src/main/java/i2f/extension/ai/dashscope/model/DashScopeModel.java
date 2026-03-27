package i2f.extension.ai.dashscope.model;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.tools.ToolCallBase;
import com.alibaba.dashscope.tools.ToolCallFunction;
import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.extension.ai.dashscope.impl.DashScopeAi;
import i2f.extension.ai.dashscope.tool.DashScopeToolDefinition;
import i2f.extension.ai.dashscope.tool.DashScopeToolHelper;
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
public class DashScopeModel implements AiModel {
    public static final String TOOL_CALLS = "tool_calls";
    protected Generation gen = new Generation();
    protected String apiKey;
    protected String model = DashScopeAi.DEFAULT_MODEL;

    public DashScopeModel gen(Generation gen) {
        this.gen = gen;
        return this;
    }

    public DashScopeModel apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public DashScopeModel model(String model) {
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
                Message item = null;
                if (rawMsg.getRawMessage() instanceof Message) {
                    item = (Message) rawMsg.getRawMessage();
                } else {
                    item = Message.builder()
                            .role(Role.USER.getValue())
                            .content(rawMsg.text())
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof SystemMessage) {
                SystemMessage rawMsg = (SystemMessage) rawItem;
                Message item = null;
                if (rawMsg.getRawMessage() instanceof Message) {
                    item = (Message) rawMsg.getRawMessage();
                } else {
                    item = Message.builder()
                            .role(Role.SYSTEM.getValue())
                            .content(rawMsg.text())
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof ToolMessage) {
                ToolMessage rawMsg = (ToolMessage) rawItem;
                Message item = null;
                if (rawMsg.getRawMessage() instanceof Message) {
                    item = (Message) rawMsg.getRawMessage();
                } else {
                    item = Message.builder()
                            .role(Role.TOOL.getValue())
                            .content(rawMsg.getText())
                            .toolCallId(rawMsg.getId())
                            .build();
                }
                rawMsg.setRawMessage(item);
                messageList.add(item);
            } else if (rawItem instanceof AssistantMessage) {
                AssistantMessage rawMsg = (AssistantMessage) rawItem;
                Message item = null;
                if (rawMsg.getRawMessage() instanceof Message) {
                    item = (Message) rawMsg.getRawMessage();
                } else {
                    List<ToolCallRequest> rawToolCallList = rawMsg.getToolCallRequestList();
                    List<ToolCallBase> toolExecList = new ArrayList<>();
                    if (rawToolCallList != null && !rawToolCallList.isEmpty()) {
                        for (ToolCallRequest rawCall : rawToolCallList) {
                            ToolCallBase request = null;
                            if (rawCall.getRawRequest() instanceof ToolCallBase) {
                                request = (ToolCallBase) rawCall.getRawRequest();
                            } else {
                                ToolCallFunction func = new ToolCallFunction();
                                func.setId(rawCall.getId());
                                func.setType("function");
                                ToolCallFunction.CallFunction cfunc = func.new CallFunction();
                                cfunc.setName(rawCall.getName());
                                cfunc.setArguments(rawCall.getArguments());
                                cfunc.setOutput("json");
                                func.setFunction(cfunc);

                                request = func;
                            }
                            rawCall.setRawRequest(request);
                            toolExecList.add(request);
                        }
                    }

                    item = Message.builder()
                            .role(Role.ASSISTANT.getValue())
                            .content(rawMsg.getText())
                            .toolCalls(toolExecList)
                            .build();
                    rawMsg.setRawMessage(item);
                }
                messageList.add(item);
            }
        }

        Map<String, DashScopeToolDefinition> toolMap = new LinkedHashMap<>();
        Map<String, ToolRawDefinition> toolRawMap = req.getToolMap();

        for (Map.Entry<String, ToolRawDefinition> entry : toolRawMap.entrySet()) {
            toolMap.put(entry.getKey(), DashScopeToolHelper.fromRaw(entry.getValue()));
        }

        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model(model)
                .messages(messageList)
                .tools(DashScopeToolHelper.convertTools(toolMap))
                .n(1) // 只返回一个 choice
                .build();
        GenerationResult call = null;
        try {
            call = gen.call(param);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        AssistantMessage ret = new AssistantMessage();
        ret.setRawMessage(call);


        List<GenerationOutput.Choice> choices = call.getOutput().getChoices();
        if (choices == null || choices.isEmpty()) {
            ret.setFinishReason(AssistantMessage.FinishReason.STOP);
            ret.setText(call.getOutput().getText());
            return ret;
        } else {
            List<ToolCallRequest> list = new ArrayList<>();
            for (GenerationOutput.Choice choice : choices) {
                String reason = choice.getFinishReason();
                Message message = choice.getMessage();
                if (TOOL_CALLS.equals(reason)) {
                    List<ToolCallBase> toolCalls = message.getToolCalls();
                    for (ToolCallBase toolCall : toolCalls) {
                        if (toolCall instanceof ToolCallFunction) {
                            ToolCallFunction callFunction = (ToolCallFunction) toolCall;
                            ToolCallFunction.CallFunction function = callFunction.getFunction();

                            ToolCallRequest vo = new ToolCallRequest();
                            vo.setId(callFunction.getId());
                            vo.setName(function.getName());
                            vo.setArguments(function.getArguments());
                            vo.setRawRequest(callFunction);
                            list.add(vo);
                        }
                    }
                }
            }

            ret.setToolCallRequestList(list);
            ret.setFinishReason(AssistantMessage.FinishReason.TOOL_CALL);
            ret.setText(call.getOutput().getText());
        }

        return ret;
    }
}
