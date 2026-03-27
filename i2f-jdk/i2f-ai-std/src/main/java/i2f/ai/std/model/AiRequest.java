package i2f.ai.std.model;

import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.tool.ToolRawDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:20
 * @desc
 */
@Data
@NoArgsConstructor
public class AiRequest {
    protected List<AiMessage> messageList;
    protected Map<String, ToolRawDefinition> toolMap;

    public AiRequest messageList(List<AiMessage> messageList) {
        this.messageList = messageList;
        return this;
    }

    public AiRequest toolMap(Map<String, ToolRawDefinition> toolMap) {
        this.toolMap = toolMap;
        return this;
    }

    public AiRequest user(AiMessage msg) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(msg);
        return this;
    }

    public AiRequest user(String content) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(new UserMessage(content));
        return this;
    }

    public AiRequest system(String content) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(new SystemMessage(content));
        return this;
    }

    public AiRequest tool(ToolRawDefinition definition) {
        if (toolMap == null) {
            toolMap = new LinkedHashMap<>();
        }
        toolMap.put(definition.getFunctionName(), definition);
        return this;
    }

    public AiRequest tools(Map<String, ToolRawDefinition> map) {
        if (toolMap == null) {
            toolMap = new LinkedHashMap<>();
        }
        toolMap.putAll(map);
        return this;
    }

    public AiRequest tools(List<ToolRawDefinition> list) {
        if (toolMap == null) {
            toolMap = new LinkedHashMap<>();
        }
        for (ToolRawDefinition definition : list) {
            toolMap.put(definition.getFunctionName(), definition);
        }
        return this;
    }
}
