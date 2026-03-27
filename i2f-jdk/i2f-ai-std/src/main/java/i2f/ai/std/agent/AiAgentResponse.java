package i2f.ai.std.agent;

import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.tool.ToolRawDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:20
 * @desc
 */
@Data
@NoArgsConstructor
public class AiAgentResponse {
    protected List<AiMessage> messageList;
    protected Map<String, ToolRawDefinition> toolMap;
    protected AiAgentContext context;

    public AiMessage last(){
        if(messageList==null || messageList.isEmpty()){
            return null;
        }
        return messageList.get(messageList.size()-1);
    }
}
