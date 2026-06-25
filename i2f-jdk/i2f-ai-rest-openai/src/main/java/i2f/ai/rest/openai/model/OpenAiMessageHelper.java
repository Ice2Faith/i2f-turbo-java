package i2f.ai.rest.openai.model;

import i2f.ai.rest.openai.model.data.*;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/25 15:37
 * @desc
 */
public class OpenAiMessageHelper {

    public static List<OpenAiMessage> toOpenAiMessages(List<AiMessage> messageList) {
        List<OpenAiMessage> ret = new ArrayList<>();
        if (messageList == null) {
            return ret;
        }
        for (AiMessage item : messageList) {
            if (item instanceof UserMessage) {
                UserMessage msg = (UserMessage) item;
                ret.add(toOpenAiUserMessage(msg));
            } else if (item instanceof SystemMessage) {
                SystemMessage msg = (SystemMessage) item;
                ret.add(toOpenAiSystemMessage(msg));
            } else if (item instanceof AssistantMessage) {
                AssistantMessage msg = (AssistantMessage) item;
                ret.add(toOpenAiAssistantMessage(msg));
            } else if (item instanceof ToolMessage) {
                ToolMessage msg = (ToolMessage) item;
                ret.add(toOpenAiToolMessage(msg));
            }
        }

        return ret;
    }

    public static OpenAiMessage toOpenAiMessage(AiMessage item) {
        if (item == null) {
            return null;
        }
        if (item instanceof UserMessage) {
            UserMessage msg = (UserMessage) item;
            return toOpenAiUserMessage(msg);
        } else if (item instanceof SystemMessage) {
            SystemMessage msg = (SystemMessage) item;
            return toOpenAiSystemMessage(msg);
        } else if (item instanceof AssistantMessage) {
            AssistantMessage msg = (AssistantMessage) item;
            return toOpenAiAssistantMessage(msg);
        } else if (item instanceof ToolMessage) {
            ToolMessage msg = (ToolMessage) item;
            return toOpenAiToolMessage(msg);
        } else {
            throw new IllegalArgumentException("un-support type: " + item.getClass());
        }
    }

    public static OpenAiUserMessage toOpenAiUserMessage(UserMessage msg) {
        return new OpenAiUserMessage(msg.getText());
    }

    public static OpenAiSystemMessage toOpenAiSystemMessage(SystemMessage msg) {
        return new OpenAiSystemMessage(msg.getText());
    }

    public static OpenAiAssistantMessage toOpenAiAssistantMessage(AssistantMessage msg) {
        OpenAiAssistantMessage dto = new OpenAiAssistantMessage(msg.getText());
        dto.setTool_calls(new ArrayList<>());

        List<ToolCallRequest> toolCallRequestList = msg.getToolCallRequestList();
        if (toolCallRequestList != null && !toolCallRequestList.isEmpty()) {
            for (ToolCallRequest toolCallRequest : toolCallRequestList) {
                OpenAiToolCall toolCall = new OpenAiToolCall();
                toolCall.setId(toolCallRequest.getId());
                toolCall.setFunction(new OpenAiToolCallFunction(toolCallRequest.getName(), toolCallRequest.getArguments()));
                dto.getTool_calls().add(toolCall);
            }
        }
        return dto;
    }

    public static OpenAiToolMessage toOpenAiToolMessage(ToolMessage msg) {
        return new OpenAiToolMessage(msg.getId(), msg.getText());
    }


    public static List<AiMessage> fromOpenAiMessages(List<OpenAiMessage> messageList) {
        List<AiMessage> ret = new ArrayList<>();
        if (messageList == null) {
            return ret;
        }
        for (OpenAiMessage item : messageList) {
            if (item instanceof OpenAiUserMessage) {
                OpenAiUserMessage msg = (OpenAiUserMessage) item;
                ret.add(fromOpenAiUserMessage(msg));
            } else if (item instanceof OpenAiSystemMessage) {
                OpenAiSystemMessage msg = (OpenAiSystemMessage) item;
                ret.add(fromOpenAiSystemMessage(msg));
            } else if (item instanceof OpenAiAssistantMessage) {
                OpenAiAssistantMessage msg = (OpenAiAssistantMessage) item;
                ret.add(fromOpenAiAssistantMessage(msg));
            } else if (item instanceof OpenAiToolMessage) {
                OpenAiToolMessage msg = (OpenAiToolMessage) item;
                ret.add(fromOpenAiToolMessage(msg));
            }
        }

        return ret;
    }

    public static AiMessage fromOpenAiMessage(OpenAiMessage item) {
        if (item == null) {
            return null;
        }
        if (item instanceof OpenAiUserMessage) {
            OpenAiUserMessage msg = (OpenAiUserMessage) item;
            return fromOpenAiUserMessage(msg);
        } else if (item instanceof OpenAiSystemMessage) {
            OpenAiSystemMessage msg = (OpenAiSystemMessage) item;
            return fromOpenAiSystemMessage(msg);
        } else if (item instanceof OpenAiAssistantMessage) {
            OpenAiAssistantMessage msg = (OpenAiAssistantMessage) item;
            return fromOpenAiAssistantMessage(msg);
        } else if (item instanceof OpenAiToolMessage) {
            OpenAiToolMessage msg = (OpenAiToolMessage) item;
            return fromOpenAiToolMessage(msg);
        } else {
            throw new IllegalArgumentException("un-support type: " + item.getClass());
        }
    }

    public static UserMessage fromOpenAiUserMessage(OpenAiUserMessage msg) {
        UserMessage dto = new UserMessage(msg.getContent());
        dto.setRawMessage(msg);
        return dto;
    }

    public static SystemMessage fromOpenAiSystemMessage(OpenAiSystemMessage msg) {
        SystemMessage dto = new SystemMessage(msg.getContent());
        dto.setRawMessage(msg);
        return dto;
    }

    public static AssistantMessage fromOpenAiAssistantMessage(OpenAiAssistantMessage msg) {
        AssistantMessage dto = new AssistantMessage(msg.getContent());
        dto.setRawMessage(msg);
        dto.setFinishReason(AssistantMessage.FinishReason.STOP);
        dto.setToolCallRequestList(new ArrayList<>());

        List<OpenAiToolCall> toolCallRequestList = msg.getTool_calls();
        if (toolCallRequestList != null && !toolCallRequestList.isEmpty()) {
            dto.setFinishReason(AssistantMessage.FinishReason.TOOL_CALL);

            for (OpenAiToolCall toolCallRequest : toolCallRequestList) {
                ToolCallRequest toolCall = new ToolCallRequest();
                toolCall.setId(toolCallRequest.getId());
                toolCall.setName(toolCallRequest.getFunction().getName());
                toolCall.setArguments(toolCallRequest.getFunction().getArguments());
                toolCall.setRawRequest(toolCallRequest);
                dto.getToolCallRequestList().add(toolCall);
            }
        }
        return dto;
    }

    public static ToolMessage fromOpenAiToolMessage(OpenAiToolMessage msg) {
        ToolMessage dto = new ToolMessage(msg.getTool_call_id(), msg.getContent());
        dto.setRawMessage(msg);
        return dto;
    }
}
